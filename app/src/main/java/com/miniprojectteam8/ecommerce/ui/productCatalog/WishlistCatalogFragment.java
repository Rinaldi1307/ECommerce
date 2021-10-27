package com.miniprojectteam8.ecommerce.ui.productCatalog;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miniprojectteam8.ecommerce.R;
import com.miniprojectteam8.ecommerce.ui.productDetail.ProductDetailFragment;

public class WishlistCatalogFragment extends Fragment {
    private ProductViewModel wishlistViewModel;
    private ProductCatalogAdapter wishlistCatalogAdapter;

    public static WishlistCatalogFragment newInstance() {
        return new WishlistCatalogFragment();
    }

    private final ProductClickableCallback wishlistClickableCallback = (view, product) -> {
        ProductDetailFragment productDetailFragment = new ProductDetailFragment(product, requireActivity().getApplication());

        getParentFragmentManager().beginTransaction().replace(R.id.container, productDetailFragment)
                .commitNow();
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //Initiate ProductViewModel
        wishlistViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View wishlistView = inflater.inflate(R.layout.wishlist_catalog_fragment, container, false);

        //Initiate RecyclerView dan productCatalogAdapter
        RecyclerView wishlistRecyclerView = (RecyclerView) wishlistView.findViewById(R.id.wishlistCatalogRecyclerView);
        wishlistCatalogAdapter = new ProductCatalogAdapter(wishlistClickableCallback);

        //Set product pada catalog ke semua product
        wishlistViewModel.setProductsInWishlist();

        //Set RecyclerView
        wishlistRecyclerView.setAdapter(wishlistCatalogAdapter);
        wishlistRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return wishlistView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wishlistViewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            if (products != null) {
                wishlistCatalogAdapter.submitList(products);
            }
        });
    }
}
