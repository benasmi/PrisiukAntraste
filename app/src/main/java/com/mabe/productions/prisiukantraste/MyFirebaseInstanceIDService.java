package com.mabe.productions.prisiukantraste;

/**
 * Created by Benas on 24/06/2017.
 */

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TEST", "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
        super.onTokenRefresh();
    }

    private void sendRegistrationToServer(String token) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("user_data", MODE_PRIVATE);

        String old_token = sharedPreferences.getString("device_token",null);
        String type = "android";
        new ServerManager(this, ServerManager.SERVER_ADDRESS_INSERT_DEVICE_TOKEN,false,null).execute(old_token, token, type);

        sharedPreferences.edit().putString("device_token", token).commit();

    }
}
