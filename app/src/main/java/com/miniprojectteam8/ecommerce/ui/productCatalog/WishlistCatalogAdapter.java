package com.miniprojectteam8.ecommerce.ui.productCatalog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.miniprojectteam8.ecommerce.R;
import com.miniprojectteam8.ecommerce.room.Product;
import com.miniprojectteam8.ecommerce.room.ProductRepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WishlistCatalogAdapter extends ListAdapter<Product, WishlistCatalogAdapter.ViewHolder> {

    private final ProductClickableCallback productClickableCallback;
    private final RemoveWishlistClickableCallback removeWishlistClickableCallback;

    private final ExecutorService networkExecutor = Executors.newFixedThreadPool(4);

    private final Executor mainThread = new Executor() {
        private final Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    };

    protected WishlistCatalogAdapter(@NonNull ProductClickableCallback productClickableCallback, @NonNull RemoveWishlistClickableCallback removeWishlistClickableCallback) {
        super(new AsyncDifferConfig.Builder<>(new DiffUtil.ItemCallback<Product>() {
            @Override
            public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
                return oldItem.id == newItem.id;
            }
        }).build());
        this.productClickableCallback = productClickableCallback;
        this.removeWishlistClickableCallback = removeWishlistClickableCallback;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.wishlist_catalog_recycler_view_item, parent, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvPrice;
        ImageView deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.catalog_item_image_view_image);
            tvTitle = itemView.findViewById(R.id.catalog_item_text_view_title);
            tvPrice = itemView.findViewById(R.id.catalog_item_text_view_price);
            itemView.setOnClickListener(this);
            deleteButton = itemView.findViewById(R.id.delete_wishlist);

        }

        @Override
        public void onClick(View v) {
            int position = getAbsoluteAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Product product = getItem(position);
                productClickableCallback.onClick(v, product);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Set tampilan product pada catalog
        Product product = getItem(position);

        //Set image view default null, lalu Download image dan setImage secara asynchronous
        holder.ivImage.setImageBitmap(null);
        downloadImage(holder.ivImage, product.getImageUrl());

        holder.tvTitle.setText(product.getTitle());
        holder.tvPrice.setText("$ ".concat(product.getPrice()));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeWishlistClickableCallback.onClick(view, product);
            }
        });
    }

    private void downloadImage(ImageView imageView, String url) {
        networkExecutor.execute(() -> {
            try {
                InputStream is = new URL(url).openStream();
                Bitmap image = BitmapFactory.decodeStream(is);
                mainThread.execute(() -> imageView.setImageBitmap(image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
