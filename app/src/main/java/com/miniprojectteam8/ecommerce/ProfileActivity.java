package com.miniprojectteam8.ecommerce;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.miniprojectteam8.ecommerce.api.loginRetrofit.Data;

public class ProfileActivity extends BaseActivity{
    private TextView profileText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileText = findViewById(R.id.profileText);
        Data data = SessionManagerUtil.getInstance().getData(this);
        profileText.setText("Hello " + data.getFullName() + " " + data.getEmail());

    }
}
