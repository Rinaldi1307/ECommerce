package com.miniprojectteam8.ecommerce.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDAO {
    @Query("SELECT * FROM product")
    List<Product> getAllProducts();

    @Query("SELECT * FROM product WHERE category = :category")
    List<Product> getProductsByCategory(String category);

    @Query("SELECT * FROM product WHERE lower(title) like '%' || :query || '%'")
    List<Product> getProductsQueryTitle(String query);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Product product);

    @Query("UPDATE product SET isInWishlist = 1 WHERE category = :id")
    void addProductToWishlist(int id);

    @Query("UPDATE product SET isInWishlist = 0 WHERE category = :id")
    void removeProductFromWishlist(int id);
}

