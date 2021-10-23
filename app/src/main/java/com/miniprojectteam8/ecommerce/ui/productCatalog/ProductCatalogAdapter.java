package com.miniprojectteam8.ecommerce.ui.productCatalog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.miniprojectteam8.ecommerce.R;
import com.miniprojectteam8.ecommerce.room.Product;

public class ProductCatalogAdapter extends ListAdapter<Product, ProductCatalogAdapter.ViewHolder> {

    private final ProductClickableCallback productClickableCallback;

    protected ProductCatalogAdapter(@NonNull ProductClickableCallback productClickableCallback) {
        super(new AsyncDifferConfig.Builder<>(new DiffUtil.ItemCallback<Product>() {
            @Override
            public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
                return oldItem.id == newItem.id;
            }
        }).build());
        this.productClickableCallback = productClickableCallback;
    }

    @NonNull
    @Override
    public ProductCatalogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.product_catalog_recycler_view_item, parent, false);

        return new ProductCatalogAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        TextView tvPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.catalog_item_text_view_title);
            tvPrice = itemView.findViewById(R.id.catalog_item_text_view_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAbsoluteAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Product product = getItem(position);
                productClickableCallback.onClick(v, product);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCatalogAdapter.ViewHolder holder, int position) {
        //Set tampilan product pada catalog
        holder.tvTitle.setText(getItem(position).getTitle());
        holder.tvPrice.setText(getItem(position).getPrice());
    }
}
