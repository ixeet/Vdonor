package com.bluecepheid.admin.vdonor;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class SymptonsActivity extends Activity {
WebView w;
    CircleProgressBar progressBar;
    String mess,SYMPTOMS_URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptons);
        mess= getApplicationContext().getResources().getString(R.string.base_url);
        SYMPTOMS_URL = mess+"/symptoms.html";
        w=(WebView)findViewById(R.id.t);
       if(isNetworkAvailable()) {
            progressBar = (CircleProgressBar) findViewById(R.id.pBar);
            progressBar.setColorSchemeResources(R.color.red1);
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    //  registerInBackground(n,e);
                    // close this activity
                    progressBar.setVisibility(View.INVISIBLE);
                    w.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }
                    });
                    w.loadUrl(SYMPTOMS_URL);

                }
            }, 5000);
       }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Please connect to Internet",
                    Toast.LENGTH_LONG).show();
        }
}


             @Override
             public boolean onCreateOptionsMenu(Menu menu) {
                 // Inflate the menu; this adds items to the action bar if it is present.
                 getMenuInflater().inflate(R.menu.menu_symptons, menu);
                 return true;
             }

             @Override
             public boolean onOptionsItemSelected(MenuItem item) {
                 // Handle action bar item clicks here. The action bar will
                 // automatically handle clicks on the Home/Up button, so long
                 // as you specify a parent activity in AndroidManifest.xml.
                 int id = item.getItemId();

                 //noinspection SimplifiableIfStatement


                 return super.onOptionsItemSelected(item);
             }
    public boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
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
