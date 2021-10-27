package com.miniprojectteam8.ecommerce.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.miniprojectteam8.ecommerce.R;
import com.miniprojectteam8.ecommerce.ui.productCatalog.ProductCatalogFragment;

public class TransactionSuccessFragment extends Fragment {

    private NotificationManager notificationManager;
    private final int notificationId = 1;
    private static final String channelId = "channelId";
    private static final String channelName = "channelName";

    private static final String contentTitle = "Transaction on E-Commerce Success";
    private static final String contentText = "Thank you for your patronage. Your order(s) are on the way!";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_success_fragment, container, false);

        Button btnBack = view.findViewById(R.id.transaction_success_button_back);
        btnBack.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.container, ProductCatalogFragment.newInstance())
                .commitNow());

        notificationManager = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannelNotification();
            builder = new NotificationCompat.Builder(requireContext(), channelId);
        } else {
            builder = new NotificationCompat.Builder(requireContext());
        }
        builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setContentTitle(contentTitle)
                .setContentText(contentText);
        notificationManager.notify(notificationId, builder.build());

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannelNotification(){
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
    }
}