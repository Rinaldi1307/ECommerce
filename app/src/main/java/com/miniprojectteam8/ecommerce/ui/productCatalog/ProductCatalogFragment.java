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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miniprojectteam8.ecommerce.R;
import com.miniprojectteam8.ecommerce.ui.productDetail.ProductDetailFragment;

public class ProductCatalogFragment extends Fragment {

    private ProductViewModel productViewModel;
    private ProductCatalogAdapter productCatalogAdapter;

    private TextView tvNotFound;

    public static ProductCatalogFragment newInstance() {
        return new ProductCatalogFragment();
    }

    private final ProductClickableCallback productClickableCallback = (view, product) -> {
        ProductDetailFragment productDetailFragment = new ProductDetailFragment(product, requireActivity().getApplication());

        getParentFragmentManager().beginTransaction().replace(R.id.container, productDetailFragment)
                .commitNow();
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

        tvNotFound = view.findViewById(R.id.productCatalogTextViewNotFound);

        //Initiate RecyclerView dan productCatalogAdapter
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.productCatalogRecyclerView);
        productCatalogAdapter = new ProductCatalogAdapter(productClickableCallback);

        //Set product pada catalog ke semua product
        productViewModel.setProductsToAllProducts();

        //Set RecyclerView
        recyclerView.setAdapter(productCatalogAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productViewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            tvNotFound.setVisibility(View.INVISIBLE);
            if (products == null || products.size() == 0) tvNotFound.setVisibility(View.VISIBLE);

            productCatalogAdapter.submitList(products);
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (getActivity() != null) {
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
                    if (TextUtils.isEmpty(s)) {
                        productViewModel.setProductsToAllProducts();
                        return true;
                    } else {
                        productViewModel.setProductsQueryTitle(s);
                        return false;
                    }
                }
            });
            sv.setOnCloseListener(() -> {
                productViewModel.setProductsToAllProducts();
                return false;
            });
        }
    }
}