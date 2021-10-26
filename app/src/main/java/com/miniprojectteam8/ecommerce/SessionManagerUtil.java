package com.miniprojectteam8.ecommerce;

import android.content.Context;
import android.content.SharedPreferences;

import com.miniprojectteam8.ecommerce.api.loginRetrofit.Data;

import java.util.Calendar;
import java.util.Date;

public class SessionManagerUtil {
    public static final String SESSION_PREFERENCE = "com.miniprojectteam8.ecommerce.SessionManagerUtil.SESSION_PREFERENCE";
    public static final String SESSION_TOKEN = "com.miniprojectteam8.ecommerce.SessionManagerUtil.SESSION_TOKEN";
    public static final String SESSION_EXPIRY_TIME = "com.miniprojectteam8.ecommerce.SessionManagerUtil.SESSION_EXPIRY_TIME";

    private static SessionManagerUtil INSTANCE;
    public static SessionManagerUtil getInstance(){
        if (INSTANCE == null){
            INSTANCE = new SessionManagerUtil();
        }
        return INSTANCE;
    }

    public void startUserSession(Context context, int expiredIn){
        Calendar calendar = Calendar.getInstance();
        Date userLoggedTime = calendar.getTime();
        calendar.setTime(userLoggedTime);
        calendar.add(Calendar.SECOND, expiredIn);
        Date expiryTime = calendar.getTime();
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(SESSION_EXPIRY_TIME, expiryTime.getTime());
        editor.apply();
    }

    public boolean isSessionActive(Context context, Date currentTime){
        Date sessionExpiresAt = new Date(getExpiryDateFromPreference(context));
        return !currentTime.after(sessionExpiresAt);
    }

    private long getExpiryDateFromPreference(Context context){
        return context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE)
                .getLong(SESSION_EXPIRY_TIME, 0);
    }

    public void storeUserToken(Context context, Data data, String token){
        SharedPreferences.Editor editor =
                context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE).edit();
        editor.putString("id", data.getId());
        editor.putString("full_name", data.getFullName());
        editor.putString("email", data.getEmail());
        editor.putString(SESSION_TOKEN, token);
        editor.apply();
    }

    public Data getData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE);
        return new Data(
                sharedPreferences.getString("id", null),
                sharedPreferences.getString("full_name", null),
                sharedPreferences.getString("email", null)
        );
    }

    public String getUserToken(Context context){
        return context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE)
                .getString(SESSION_TOKEN, "");
    }

    public void endUserSession(Context context){
        clearStoredData(context);
    }

    private void clearStoredData(Context context){
        SharedPreferences.Editor editor =
                context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
}
