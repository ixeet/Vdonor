package com.bluecepheid.admin.vdonor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HospitalActivity extends Activity implements LocationListener {
EditText sstate;
    SqliteHelper2 sqliteHelper2;
    Button go;
    String idd,state;
ListView hlist1;

    List<String> list1 = new ArrayList<String>();
    List<String> list2 = new ArrayList<String>();
    List<String> list3 = new ArrayList<String>();
    List<String> list4 = new ArrayList<String>();

    CircleProgressBar progressBar;
    HospitalAdapter1 adapter1;
    LocationManager locationManager;
    String mess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if ( !locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER ) ) {

            showSettingsAlert("GPS");

        }
        mess= getApplicationContext().getResources().getString(R.string.base_url);
hlist1=(ListView)findViewById(R.id.hlist1);
        sqliteHelper2 = new SqliteHelper2(this);
        sstate=(EditText)findViewById(R.id.spnr_state);
        go=(Button)findViewById(R.id.go);
        sstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStateData();
            }
        });
        sstate.setText("Delhi");
        String nameid=sstate.getText().toString();
        Cursor cursor = sqliteHelper2.getUser(nameid);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            idd = cursor.getString(cursor.getColumnIndex("name"));
        }
        //Toast.makeText(HospitalActivity.this, "" + idd, Toast.LENGTH_LONG).show();
        if(isNetworkAvailable()) {
            new Loadhoslist1().execute();
        }
        else {
            Toast.makeText(getApplicationContext(),
                    "Please connect to Internet",
                    Toast.LENGTH_LONG).show();
        }
        go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                state = sstate.getText().toString();
                Cursor cursor = sqliteHelper2.getUser(state);
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    idd = cursor.getString(cursor.getColumnIndex("name"));
                }
                hlist1.setVisibility(View.INVISIBLE);
                if(isNetworkAvailable()) {
                    new Loadhoslist1().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Please connect to Internet",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
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

    class Loadhoslist1 extends AsyncTask<String, String, String> {
        String[] name, OtherName,contactNo1,address1,contactNo2;
        ArrayList<HospitalModel1> countrylist1;
       String pp=null;
        String p=null;
        JSONArray data;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = (CircleProgressBar) findViewById(R.id.pBar);
            progressBar.setColorSchemeResources(R.color.red1);
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag

           String LOAD_URL =mess+"/rest/common/getHospitals/search";
                list1.clear();
                list2.clear();
                list3.clear();
                list4.clear();
                /*name = loadArray("Hosparray",getApplicationContext());
                Log.d("hospitaal",""+name.length);
                // Building Parameters
               if(name.length==0) {*/
                PostJsonObject json = new PostJsonObject();
                JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("searchtxt", idd);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                p = json.postJson(jsonObj, LOAD_URL);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("Response of list1", p.toString());
                if (!p.contains("[]")) {
                    JSONObject j = null;
                    try {
                        j = new JSONObject(p);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Getting JSON Array node
                    try {
                        data = j.getJSONArray("hospitalList");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                  //  Log.d("Returned", "" + data.length());
                  //  Log.d("Returned", "" + data);

                    for (int i = 0; i < data.length(); i++) {
                        //  JSONObject c = data.getJSONObject(i);

                        try {
                            list1.add(data.getJSONObject(i).getString("hospitalNm"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            list2.add(data.getJSONObject(i).getString("hospitalAddress1"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            list3.add(data.getJSONObject(i).getString("contactNo1"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            list4.add(data.getJSONObject(i).getString("contactNo2"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    // t.setText("There are "+data.length()+" tests available for you.");
                    name = list1.toArray(new String[list1.size()]);

                    address1 = list2.toArray(new String[list2.size()]);
                    contactNo1 = list3.toArray(new String[list3.size()]);
                    contactNo2 = list4.toArray(new String[list4.size()]);
               /*    Boolean p1 = saveArray(name, "Hosparray", getApplicationContext());
                   Boolean p2 = saveArray(address1, "Hospaddarray", getApplicationContext());
                   Boolean p3 = saveArray(contactNo1, "Hospcont1array", getApplicationContext());
                   Boolean p4 = saveArray(contactNo2, "Hospcont2array", getApplicationContext());
               }
                else
               {    name = loadArray("Hosparray",getApplicationContext());
                   address1 = loadArray("Hospaddarray",getApplicationContext());
                   contactNo1 = loadArray("Hospcont1array",getApplicationContext());
                   contactNo2 = loadArray("Hospcont2array",getApplicationContext());

               }*/
                    countrylist1 = new ArrayList<HospitalModel1>();
                    for (int i = 0; i < name.length; i++) {
                        HospitalModel1 country = new HospitalModel1(name[i], address1[i], contactNo1[i], contactNo2[i]);
                        countrylist1.add(country);
                    }

                    adapter1 = new HospitalAdapter1(getApplicationContext(), countrylist1);
pp="success";


            }
            else
            {
                pp="fail";
            }
/*if(data.length()>0)
{
    return "success";
}
          else
{
    return "fail";
}*/
            return pp;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted

            progressBar.setVisibility(View.INVISIBLE);

            if(file_url.equals("success")) {

                // Toast.makeText(DonorActivity.this, "yes"+location.getLatitude()+location.getLatitude()+group1, Toast.LENGTH_LONG).show();
                hlist1.setVisibility(View.VISIBLE);
                hlist1.setAdapter(adapter1);

            }
            else
            {
                Toast.makeText(HospitalActivity.this, "Sorry! There is no search result matching this criteria...", Toast.LENGTH_LONG).show();
            }
        }

    }
    public boolean saveArray(String[] array, String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.length);
        for(int i=0;i<array.length;i++)
            editor.putString(arrayName + "_" + i, array[i]);
        return editor.commit();
    }

    private void setStateData() {
        //final String[] itemsarray = loadArray("sarray",getApplicationContext());
        final String[] items = loadArray("sarray",getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(HospitalActivity.this);
        builder.setTitle("Select State");
        //builder.setI
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //Toast.makeText(getApplicationContext(), con.get(item).getCountrName(), Toast.LENGTH_SHORT).show();
                sstate.setText(items[item]);
                //System.out.println("Item is: "+items[item]);
                /*CONTRY_ID = con.get(item).getCountryId();
                stateET.requestFocus();*/
            }
        });
        //AlertDialog alert = builder.create();
        builder.show();
    }
    public String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hospital, menu);
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
    public void statusCheck()
    {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            showSettingsAlert("GPS");

        }


    }

    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
               HospitalActivity.this);

        alertDialog.setTitle( "Enable Location Settings");

        alertDialog
                .setMessage("Please enable location to allow users to locate you for platelates donation/searches.");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                       HospitalActivity.this.startActivity(intent);
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
