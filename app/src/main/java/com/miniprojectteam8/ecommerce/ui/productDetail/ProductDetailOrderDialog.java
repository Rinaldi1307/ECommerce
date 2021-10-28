package com.miniprojectteam8.ecommerce.ui.productDetail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.miniprojectteam8.ecommerce.R;
import com.miniprojectteam8.ecommerce.room.Product;
import com.miniprojectteam8.ecommerce.ui.TransactionSuccessFragment;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductDetailOrderDialog extends DialogFragment {
    private final FragmentManager fragmentManager;
    private final Product currentProduct;
    private TextView tvQuantityValue;
    private TextView tvTotalValue;
    private Button btnMinus;
    private Button btnPlus;

    private int quantity = 1;
    private final int minQuantity = 1;
    private final int maxQuantity = 10;

    private final ExecutorService networkExecutor = Executors.newFixedThreadPool(4);
    private final Executor mainThread = new Executor() {
        private final Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    };

    public ProductDetailOrderDialog (FragmentManager fragmentManager, Product product) {
        this.fragmentManager = fragmentManager;
        currentProduct = product;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.product_detail_order_dialog, null);

        ImageView ivImage = dialogView.findViewById(R.id.product_detail_order_dialog_image_view);
        networkExecutor.execute(() -> {
            try {
                InputStream is = new URL(currentProduct.getImageUrl()).openStream();
                Bitmap image = BitmapFactory.decodeStream(is);
                mainThread.execute(() -> ivImage.setImageBitmap(image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        TextView tvTitle = dialogView.findViewById(R.id.product_detail_order_dialog_text_view_title);
        tvTitle.setText(currentProduct.getTitle());

        TextView tvPriceValue = dialogView.findViewById(R.id.product_detail_order_dialog_text_view_price_value);
        tvPriceValue.setText("$ ".concat(currentProduct.getPrice()));

        TextView tvQuantity =  dialogView.findViewById(R.id.product_detail_order_dialog_text_view_quantity);
        tvQuantity.setText(
                "Quantity ("
                        .concat(String.valueOf(minQuantity))
                        .concat("-")
                        .concat(String.valueOf(maxQuantity))
                        .concat(")")
        );

        tvQuantityValue = dialogView.findViewById(R.id.product_detail_order_dialog_text_view_quantity_value);
        tvQuantityValue.setText(String.valueOf(quantity));

        tvTotalValue = dialogView.findViewById(R.id.product_detail_order_dialog_text_view_total_value);
        tvTotalValue.setText("$ ".concat(calculateTotal()));

        btnMinus = dialogView.findViewById(R.id.product_detail_order_dialog_button_minus);
        btnMinus.setEnabled(false);
        btnMinus.setOnClickListener(v -> {
            quantity--;
            tvQuantityValue.setText(String.valueOf(quantity));
            tvTotalValue.setText("$ ".concat(calculateTotal()));

            if (quantity < maxQuantity) btnPlus.setEnabled(true);
            if (quantity == minQuantity) btnMinus.setEnabled(false);
        });

        btnPlus = dialogView.findViewById(R.id.product_detail_order_dialog_button_plus);
        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantityValue.setText(String.valueOf(quantity));
            tvTotalValue.setText("$ ".concat(calculateTotal()));

            if (quantity > minQuantity) btnMinus.setEnabled(true);
            if (quantity == maxQuantity) btnPlus.setEnabled(false);
        });

        dialog.setView(dialogView);

        dialog
                .setPositiveButton("BUY", (dialogInterface, i) -> {
                    SystemClock.sleep(1000);
                    TransactionSuccessFragment transactionSuccessFragment = new TransactionSuccessFragment();
                    fragmentManager.beginTransaction().replace(R.id.container, transactionSuccessFragment)
                            .commitNow();
                })
                .setNegativeButton("CANCEL", (dialogInterface, i) -> Toast.makeText(getActivity(), "Order Canceled", Toast.LENGTH_LONG).show());
        return dialog.create();
    }

    private String calculateTotal () {
        float input = Float.parseFloat(currentProduct.getPrice()) * quantity;
        BigDecimal bigDecimal = new BigDecimal(input).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros();

        return bigDecimal.toString();
    }
}
