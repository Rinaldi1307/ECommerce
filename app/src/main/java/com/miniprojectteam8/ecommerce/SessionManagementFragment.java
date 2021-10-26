package com.miniprojectteam8.ecommerce;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.miniprojectteam8.ecommerce.api.loginRetrofit.Data;
import com.miniprojectteam8.ecommerce.api.loginRetrofit.LoginResponse;
import com.miniprojectteam8.ecommerce.api.loginRetrofit.LoginRetrofitInstance;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionManagementFragment extends Fragment {
    private View rootView;
    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutPassword;
    private Button btnLogin;
    private Button btnLogout;
    private ProgressBar pb;

    private String username;
    private String password;

    private Executor backgroundThread = Executors.newSingleThreadExecutor();
    private Executor mainThread = new Executor() {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    };

    public static SessionManagementFragment newInstance(){return new SessionManagementFragment();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_login, container, false);

        pb = rootView.findViewById(R.id.progressBar);
        textInputLayoutUsername = rootView.findViewById(R.id.user_name);
        textInputLayoutPassword = rootView.findViewById(R.id.user_password);

        btnLogin = rootView.findViewById(R.id.login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        return rootView;
    }

    private void validate(){

        username = textInputLayoutUsername.getEditText().getText().toString();
        password = textInputLayoutPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Username dan password tidak boleh kosong!!", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (username.equalsIgnoreCase("user") && password.equalsIgnoreCase("pass")) {
//            login();
//        } else {
//            Toast.makeText(getActivity(), "Username dan password tidak cocok!!", Toast.LENGTH_SHORT).show();
//            return;
//        }
        LoginRetrofitInstance loginRetrofitInstance = new LoginRetrofitInstance();
        loginRetrofitInstance.getAPI().getLoginResponse(username, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.body() != null){
                    LoginResponse loginResponse = response.body();
                    if(loginResponse.getStatus()){
                        login(loginResponse.getData(), loginResponse.getToken());
                    }
                }else{
                    Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });

    }

    private void login(Data data, String token){
        pb.setVisibility(View.VISIBLE);
        backgroundThread.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                SystemClock.sleep(3000);
                mainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.INVISIBLE);
                        startAndStoreSession(data, token);
                        startMainActivity();
                    }
                });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startAndStoreSession(Data data, String token){
        SessionManagerUtil.getInstance().storeUserToken(requireActivity(), data, token);
        SessionManagerUtil.getInstance().startUserSession(requireActivity(), 30);
    }

    private void startMainActivity(){
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        requireActivity().startActivity(intent);
    }
}
