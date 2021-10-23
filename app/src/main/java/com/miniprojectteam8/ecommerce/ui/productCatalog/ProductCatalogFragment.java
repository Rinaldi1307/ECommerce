package com.miniprojectteam8.ecommerce.ui.productCatalog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.miniprojectteam8.ecommerce.R;
import com.miniprojectteam8.ecommerce.room.ProductViewModel;

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

        //Set RecyclerView
        recyclerView.setAdapter(productCatalogAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
}