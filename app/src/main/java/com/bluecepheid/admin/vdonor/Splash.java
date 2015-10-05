package com.bluecepheid.admin.vdonor;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class Splash extends Activity {
    Context mContext = Splash.this;
    AccountManager mAccountManager;
    String token;
    int serverCode;
    String textName;
    Boolean verified;
    String name,imagePreferance;

    public static final String VERIFY = "verify";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences s1= getSharedPreferences(VERIFY, Context.MODE_PRIVATE);
        verified = s1.getBoolean("verified", false);
        //  syncGoogleAccount();
        new Handler().postDelayed(new Runnable()
        {
            public void run()
            {
                if(verified==true) {
                    Intent i = new Intent(Splash.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();
                }

                else
                {
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
             /*   Intent i = new Intent(Splash.this, VerificationActivity.class);
                startActivity(i);
                finish();*/
            }
        },1000);

        // textEmail = intent.getStringExtra("email_id");



    }


    public boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.e("Network Testing", "***Available***");
            return true;
        }
        Log.e("Network Testing", "***Not Available***");
        return false;
    }




}