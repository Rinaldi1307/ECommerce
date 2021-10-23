package com.miniprojectteam8.ecommerce.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDAO {
    @Query("SELECT * FROM product")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM product WHERE category = :category")
    LiveData<List<Product>> getProductsByCategory(String category);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Product product);

    @Query("UPDATE product SET isInWishlist = 1 WHERE category = :id")
    void addProductToWishlist(int id);

    @Query("UPDATE product SET isInWishlist = 0 WHERE category = :id")
    void removeProductFromWishlist(int id);
}

