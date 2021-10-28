package com.miniprojectteam8.ecommerce.ui.productCatalog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.miniprojectteam8.ecommerce.room.Product;
import com.miniprojectteam8.ecommerce.room.ProductRepository;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private final ProductRepository productRepository;
    private LiveData<List<Product>> products;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepository = new ProductRepository(application);
        productRepository.insertProductsToDatabase();
    }

    public void setProductsToAllProducts() {
        products = productRepository.getAllProducts();
    }

    public void setProductsByCategory(String category) {
        products = productRepository.getProductsByCategory(category);
    }

    public void setProductsQueryTitle(String query) {
        products = productRepository.getProductsQueryTitle(query);
    }

    public void setProductsQueryTitleInWishlist(String query) {
        products = productRepository.getProductsQueryTitleInWishlist(query);
    }

    public void setProductsInWishlist() {
        products = productRepository.getProductsInWishlist();
    }
  
    public void deleteProductFromWishlist(Product product) {
        products = productRepository.deleteProductFromWishlist(product.getId());
    }
  
    public LiveData<List<Product>> getProducts() {
        return products;
    }
}