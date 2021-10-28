package com.miniprojectteam8.ecommerce.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.miniprojectteam8.ecommerce.api.productRetrofit.ProductRetrofit;
import com.miniprojectteam8.ecommerce.api.productRetrofit.ProductRetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private final ProductDAO productDAO;

    private final MutableLiveData<List<Product>> products = new MutableLiveData<>();
    private final MutableLiveData<Product> product = new MutableLiveData<>();

    public ProductRepository(Application application) {
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

    public LiveData<List<Product>> getAllProducts() {
        ProductDatabase.databaseWriteExecutor.execute(() -> products.postValue(productDAO.getAllProducts()));
        return products;
    }

    public LiveData<List<Product>> getProductsInWishlist() {
        ProductDatabase.databaseWriteExecutor.execute(() -> products.postValue(productDAO.getProductsInWishlist(true)));
        return products;
    }

    public LiveData<List<Product>> getProductsByCategory(String category) {
        ProductDatabase.databaseWriteExecutor.execute(() -> products.postValue(productDAO.getProductsByCategory(category)));
        return products;
    }

    public LiveData<List<Product>> getProductsQueryTitle(String query) {
        ProductDatabase.databaseWriteExecutor.execute(() -> products.postValue(productDAO.getProductsQueryTitle(query.toLowerCase())));
        return products;
    }

    public LiveData<List<Product>> getProductsQueryTitleInWishlist(String query) {
        ProductDatabase.databaseWriteExecutor.execute(() -> products.postValue(productDAO.getProductsQueryTitleInWishlist(query.toLowerCase(), true)));
        return products;
    }

    public LiveData<Product> getProductById(int productId) {
        ProductDatabase.databaseWriteExecutor.execute(() -> product.postValue(productDAO.getProductById(productId)));
        return product;
    }


    public void toogleIsInWishlist(int productId) {
        ProductDatabase.databaseWriteExecutor.execute(() -> productDAO.toggleIsInWishlist(productId));
    }

    public LiveData<List<Product>> deleteProductFromWishlist(int productId) {
        ProductDatabase.databaseWriteExecutor.execute(() -> {
            productDAO.toggleIsInWishlist(productId);
            products.postValue(productDAO.getProductsInWishlist(true));
        });
        return products;
    }
}
