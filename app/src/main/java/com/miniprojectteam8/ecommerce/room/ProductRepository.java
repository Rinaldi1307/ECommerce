package com.miniprojectteam8.ecommerce.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.miniprojectteam8.ecommerce.api.productRetrofit.ProductRetrofit;
import com.miniprojectteam8.ecommerce.api.productRetrofit.ProductRetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private final ProductDAO productDAO;

    public ProductRepository(Application application){
        ProductDatabase database = ProductDatabase.getDatabase(application);
        productDAO = database.productDao();
    }

    public void insertProductsToDatabase() {
        ProductRetrofitInstance retrofitInstance = new ProductRetrofitInstance();

        retrofitInstance
                .getAPI()
                .getAllProducts()
                .enqueue(new Callback<List<ProductRetrofit>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ProductRetrofit>> call, @NonNull Response<List<ProductRetrofit>> response) {
                        if (response.body() != null) {
                            for (ProductRetrofit pf : response.body()) {
                                Product p = new Product(
                                        pf.getId(),
                                        pf.getTitle(),
                                        String.valueOf(pf.getPrice()),
                                        pf.getDescription(),
                                        pf.getCategory(),
                                        pf.getImageUrl(),
                                        String.valueOf(pf.getProductRating().getRate()),
                                        pf.getProductRating().getCount(),
                                        false);

                                ProductDatabase.databaseWriteExecutor.execute(() -> productDAO.insert(p));
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ProductRetrofit>> call, @NonNull Throwable t) {

                    }
                });
    }

    public LiveData<List<Product>> getAllProducts(){
        return productDAO.getAllProducts();
    }

    public LiveData<List<Product>> getProductsByCategory(String category){
        return productDAO.getProductsByCategory(category);
    }

    public void addProductToWishlist(int id) {
        ProductDatabase.databaseWriteExecutor.execute(() -> productDAO.addProductToWishlist(id));
    }

    public void removeProductFromWishlist(int id) {
        ProductDatabase.databaseWriteExecutor.execute(() -> productDAO.removeProductFromWishlist(id));
    }
}
