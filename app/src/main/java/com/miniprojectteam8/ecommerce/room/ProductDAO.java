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

    @Query("SELECT * FROM product WHERE isInWishlist = :isInWishlist")
    List<Product> getProductsInWishlist(boolean isInWishlist);

    @Query("SELECT * FROM product WHERE category = :category")
    List<Product> getProductsByCategory(String category);

    @Query("SELECT * FROM product WHERE lower(title) like '%' || :query || '%'")
    List<Product> getProductsQueryTitle(String query);

    @Query("SELECT * FROM product WHERE id = :id LIMIT 1")
    Product getProductById(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Product product);

    @Query("UPDATE product SET isInWishlist = NOT isInWishlist WHERE id = :productId")
    void toggleIsInWishlist(int productId);
}

