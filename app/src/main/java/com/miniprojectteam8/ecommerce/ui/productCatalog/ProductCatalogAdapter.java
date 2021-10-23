package com.miniprojectteam8.ecommerce.ui.productCatalog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.miniprojectteam8.ecommerce.R;
import com.miniprojectteam8.ecommerce.room.Product;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductCatalogAdapter extends ListAdapter<Product, ProductCatalogAdapter.ViewHolder> {

    private final ProductClickableCallback productClickableCallback;

    private final ExecutorService networkExecutor =
            Executors.newFixedThreadPool(4);
    private final Executor mainThread = new Executor() {
        private Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    };

    protected ProductCatalogAdapter(@NonNull ProductClickableCallback productClickableCallback) {
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
    }



    @NonNull
    @Override
    public ProductCatalogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.product_catalog_recycler_view_item, parent, false);

        return new ProductCatalogAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.catalog_item_image_view_image);
            tvTitle = itemView.findViewById(R.id.catalog_item_text_view_title);
            tvPrice = itemView.findViewById(R.id.catalog_item_text_view_price);
            itemView.setOnClickListener(this);
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
    public void onBindViewHolder(@NonNull ProductCatalogAdapter.ViewHolder holder, int position) {
        //Set tampilan product pada catalog
        Product product = getItem(position);

        //Set image view default null, lalu Download image dan setImage secara asynchronous
        holder.ivImage.setImageBitmap(null);
        downloadImage(holder.ivImage, product.getImageUrl());

        holder.tvTitle.setText(product.getTitle());
        holder.tvPrice.setText("$" + product.getPrice());
    }

    private void downloadImage(ImageView imageView, String url) {
        networkExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = new URL(url).openStream();
                    Bitmap image = BitmapFactory.decodeStream(is);
                    mainThread.execute(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(image);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
