package com.miniprojectteam8.ecommerce.ui.productCatalog;

import android.view.View;

import com.miniprojectteam8.ecommerce.room.Product;

public interface ProductClickableCallback {
    void onClick(View view, Product product);
}
