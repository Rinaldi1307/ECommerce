package com.miniprojectteam8.ecommerce.ui.productCatalog;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.miniprojectteam8.ecommerce.R;
import com.miniprojectteam8.ecommerce.room.Product;
import com.miniprojectteam8.ecommerce.room.ProductViewModel;

import java.util.List;

public class ProductCatalogFragment extends Fragment {

    private ProductViewModel productViewModel;
    private ProductCatalogAdapter productCatalogAdapter;

    public static ProductCatalogFragment newInstance() {
        return new ProductCatalogFragment();
    }

    private final ProductClickableCallback productClickableCallback = (view, product) -> {

        Toast.makeText(requireActivity(), product.getTitle(), Toast.LENGTH_SHORT).show();
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //Initiate ProductViewModel
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_catalog_fragment, container, false);

        //Initiate RecyclerView dan productCatalogAdapter
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.productCatalogRecyclerView);
        productCatalogAdapter = new ProductCatalogAdapter(productClickableCallback);

        //Set product pada catalog ke semua product
        productViewModel.setProductsToAllProducts();

        SystemClock.sleep(2000);

        //Set RecyclerView
        recyclerView.setAdapter(productCatalogAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productViewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            if (products != null) {
                productCatalogAdapter.submitList(products);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        SearchManager sm = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView sv = (SearchView) menu.findItem(R.id.search).getActionView();
        sv.setSearchableInfo(sm.getSearchableInfo(getActivity().getComponentName()));
        sv.setIconifiedByDefault(true);
        sv.setMaxWidth(Integer.MAX_VALUE);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                productViewModel.setProductsQueryTitle(s);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)){
                    return true;
                }
                return false;
            }
        });
    }
}