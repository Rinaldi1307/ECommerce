package com.miniprojectteam8.ecommerce.ui.productDetail;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miniprojectteam8.ecommerce.R;
import com.miniprojectteam8.ecommerce.room.Product;
import com.miniprojectteam8.ecommerce.room.ProductRepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductDetailFragment extends Fragment {

    private final Product currentProduct;
    private boolean isInWishlist;
    private final ProductRepository productRepository;

    private final ExecutorService networkExecutor = Executors.newFixedThreadPool(4);
    private final Executor mainThread = new Executor() {
        private Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    };


    public ProductDetailFragment(Product product, Application application) {
        currentProduct = product;
        isInWishlist = product.getIsInWishlist();

        productRepository = new ProductRepository(application);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.search);
        if(item!=null) item.setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_detail_fragment, container, false);

        ImageView ivImage = view.findViewById(R.id.product_detail_image_view_image);
        networkExecutor.execute(() -> {
            try {
                InputStream is = new URL(currentProduct.getImageUrl()).openStream();
                Bitmap image = BitmapFactory.decodeStream(is);
                mainThread.execute(() -> ivImage.setImageBitmap(image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        TextView tvTitle = view.findViewById(R.id.product_detail_text_view_title);
        tvTitle.setText(currentProduct.getTitle());
        tvTitle.setTypeface(null, Typeface.BOLD);

        TextView tvDescription = view.findViewById(R.id.product_detail_text_view_description);
        tvDescription.setText(currentProduct.getDescription());

        TextView tvPrice = view.findViewById(R.id.product_detail_text_view_price);
        tvPrice.setText("$ " + currentProduct.getPrice());

        ResourcesCompat.getDrawable(getResources(), R.drawable.image_heart_empty_png, null);

        Drawable wishlistIconAddDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.image_heart_empty_png, null);
        Drawable wishlistIconRemoveDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.image_heart_full_png, null);

        FloatingActionButton wishlistFab = view.findViewById(R.id.product_detail_wishlist_fab);
        if (isInWishlist) wishlistFab.setImageDrawable(wishlistIconRemoveDrawable);
        else wishlistFab.setImageDrawable(wishlistIconAddDrawable);
        wishlistFab.setOnClickListener(v -> {
            productRepository.toogleIsInWishlist(currentProduct.getId());
            isInWishlist = !isInWishlist;
            if (isInWishlist) {
                wishlistFab.setImageDrawable(wishlistIconRemoveDrawable);
                Toast.makeText(getContext(),"Added to Wishlist",Toast.LENGTH_SHORT).show();
            }
            else {
                wishlistFab.setImageDrawable(wishlistIconAddDrawable);
                Toast.makeText(getContext(),"Removed from Wishlist",Toast.LENGTH_SHORT).show();
            }
        });

        Button buyButton = view.findViewById(R.id.product_detail_buy_button);
        buyButton.setOnClickListener(v -> {
            DialogFragment dialogFragment = new ProductDetailOrderDialog(getParentFragmentManager(), currentProduct);
            dialogFragment.show(getChildFragmentManager(), null);
        });

        return view;
    }
}