package com.bluecepheid.admin.vdonor;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class WelcomeActivity extends TabActivity implements TabHost.OnTabChangeListener,LocationListener {
    /** Called when the activity is first created. */
    TabHost tabHost;
    Toolbar tool;
    ImageView picon;
    String type;
    LocationManager locationManager;
    Location location;
    public static final String STORAGE_USERTYPE = "usertype";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
      /*  locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if ( !locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER ) ) {

            showSettingsAlert("GPS");

        }*/
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            // request location update!!
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    1, this);
        }
        tool = (Toolbar) findViewById(R.id.tool_bar);
        picon=(ImageView)findViewById(R.id.picon);
        picon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(STORAGE_USERTYPE, Context.MODE_PRIVATE);

                type = sharedPreferences.getString("type", "");
                if(type.equals("donor")) {
                    Intent i = new Intent(WelcomeActivity.this, DonoreditActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                }
                else
                {
                    Intent i = new Intent(WelcomeActivity.this, ReceivereditActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                }
            }
            });
        tabHost = getTabHost();
        TabHost.TabSpec spec;
        tabHost.setOnTabChangedListener(this);
        Intent intent;

        intent = new Intent().setClass(this, DonorActivity.class);
        spec = tabHost.newTabSpec("First").setIndicator("Donors")
                .setContent(intent);

        tabHost.addTab(spec);

        intent = new Intent().setClass(this, GuidelineActivity.class);
        spec = tabHost.newTabSpec("Guide").setIndicator("Guidelines")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this,SymptonsActivity.class);
        spec = tabHost.newTabSpec("Second").setIndicator("Symptoms")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, PrescriptionActivity.class);
        spec = tabHost.newTabSpec("Third").setIndicator("Preventions")
                .setContent(intent);
        tabHost.addTab(spec);



        intent = new Intent().setClass(this, HospitalActivity.class);
        spec = tabHost.newTabSpec("Fourth").setIndicator("Hospitals")
                .setContent(intent);
        tabHost.addTab(spec);
        // tabHost.setOnTabChangedListener(this);

        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++) {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffffff"));
            tv.setSingleLine();

            if(i==0) {
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.mipmap.tab_select1);
                tv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.donors, 0, 0);
                tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 100;
                tabHost.getTabWidget().getChildAt(i).setPadding(0, 0, 0, 0);
            }
            else if(i==1) {
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.mipmap.tab_unslelect1);
                tv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.guidelines1, 0, 0);
                tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 100;
                tabHost.getTabWidget().getChildAt(i).setPadding(0, 0, 0, 0);
            }
            else if(i==2) {
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.mipmap.tab_unslelect1);
                tv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.sympt, 0, 0);
                tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 100;
                tabHost.getTabWidget().getChildAt(i).setPadding(0, 0, 0, 0);
            }
            else if(i==3) {
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.mipmap.tab_unslelect1);
                tv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.precaut, 0, 0);
                tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 100;
                tabHost.getTabWidget().getChildAt(i).setPadding(0, 0, 0, 0);
            }
            else if(i==4) {
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.mipmap.tab_unslelect1);
                tv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.hospt, 0, 0);
                tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 100;
                tabHost.getTabWidget().getChildAt(i).setPadding(0, 0, 0, 0);
            }

        }
    }

    @Override
    public void onTabChanged(String tabId) {

        /************ Called when tab changed *************/

        //********* Check current selected tab and change according images *******/

        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
           /* if(i==0)
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab2);
            else if(i==1)
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab2);
            else if(i==2)
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab2);*/
            View v = tabHost.getTabWidget().getChildAt(i);
            v.setBackgroundResource(R.mipmap.tab_unslelect1);

            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffffff"));
            tv.setSingleLine();
            //tv.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.inbox_gray,0,0);
            if(i==0) {
                //tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab2_over);
                tv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.donors, 0, 0);
            }
            else if(i==1) {
                //tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab2);
                tv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.guidelines1, 0, 0);
            }
            else if(i==2) {
               // tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab2);
                tv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.sympt, 0, 0);
            }
            else if(i==3) {
               // tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab2);
                tv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.precaut, 0, 0);
            }

            else if(i==4) {
                //tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab2);
                tv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.hospt, 0, 0);
            }
        }


        Log.i("tabs", "CurrentTab: " + tabHost.getCurrentTab());

        if(tabHost.getCurrentTab()==0) {
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.mipmap.tab_select1);
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).getLayoutParams().height = 100;
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setPadding(0, 0, 0, 0);


        }
        else if(tabHost.getCurrentTab()==1) {
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.mipmap.tab_select1);
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).getLayoutParams().height =100;
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setPadding(0, 0, 0, 0);
        }
        else if(tabHost.getCurrentTab()==2) {
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.mipmap.tab_select1);
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).getLayoutParams().height = 100;
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setPadding(0, 0, 0, 0);
        }
        else if(tabHost.getCurrentTab()==3) {
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.mipmap.tab_select1);
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).getLayoutParams().height = 100;
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setPadding(0, 0, 0, 0);
        }
        else if(tabHost.getCurrentTab()==4) {
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.mipmap.tab_select1);
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).getLayoutParams().height = 100;
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setPadding(0, 0, 0, 0);
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

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
 /*   @Override
    public void onBackPressed() {
        Intent i = new Intent(Next1.this, buyer.class);
        startActivity(i);
        finish();
    }*/


    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
               WelcomeActivity.this);

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage(provider + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        WelcomeActivity.this.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

}