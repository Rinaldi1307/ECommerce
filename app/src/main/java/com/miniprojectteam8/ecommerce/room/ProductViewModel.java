package com.miniprojectteam8.ecommerce.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private final ProductRepository productRepository;
    private LiveData<List<Product>> products;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepository = new ProductRepository(application);
        productRepository.insertProductsToDatabase();
    }

    public void setProductsToAllProducts(){
        products = productRepository.getAllProducts();
    }

    public void setProductsByCategory(String category){
        products = productRepository.getProductsByCategory(category);
    }

    public LiveData<List<Product>> getProducts(){
        return products;
    }
}