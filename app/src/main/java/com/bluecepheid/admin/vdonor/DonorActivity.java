package com.bluecepheid.admin.vdonor;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DonorActivity extends Activity implements LocationListener {
    RelativeLayout sbutton,sbutton1;
    RelativeLayout rl1,rl2;
    int p1=0;
    int p12=0;
    TextView t;
    String srad,sgroup;
    DonorAdapter1 adapter1;
    EditText sbgroup,scity,sstate,sbgroup1,sradius;
    ListView dlist1,dlist2;
    ProgressDialog pDialog;
    LocationManager locationManager;
    Location location;
    String group1,city,group,statee,state11,city11,locality;
    //LatLng latlong;
    Button search,search1;
    double lat1,long1;
    CircleProgressBar progressBar;

    public static final String STORAGE_NAME = "VERIFICATION";

    List<String> list9 = new ArrayList<String>();
    List<String> list10 = new ArrayList<String>();
    List<String> list11 = new ArrayList<String>();
    List<String> list12 = new ArrayList<String>();
    List<String> list13 = new ArrayList<String>();
    List<String> list14 = new ArrayList<String>();
    List<String> list15 = new ArrayList<String>();
    List<String> list16 = new ArrayList<String>();
    List<String> list17 = new ArrayList<String>();

    List<String> listcc = new ArrayList<String>();
    List<String> list111 = new ArrayList<String>();
    SqliteHelper1 sqliteHelper1;
    SqliteHelper2 sqliteHelper2;
    SqliteHelper3 sqliteHelper3;
    SqliteHelper4 sqliteHelper4;
    SqliteHelper5 sqliteHelper5;
    String[] areaArr,dname;
    int []districIdArr;
    String area,areaName,idd;

    public static final String STORAGE_DATA = "Userdata";
    String mess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mess= getApplicationContext().getResources().getString(R.string.base_url);
        sqliteHelper2 = new SqliteHelper2(this);
        sqliteHelper1 = new SqliteHelper1(this);
        sqliteHelper3 = new SqliteHelper3(this);
        sqliteHelper4 = new SqliteHelper4(this);
        sqliteHelper5 = new SqliteHelper5(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if ( !locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER ) ) {

            showSettingsAlert("GPS");

        }
        Boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isNetworkEnabled) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    1, this);
            Log.d("Network", "Network");
            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    lat1 = location.getLatitude();
                    long1 = location.getLongitude();
Log.d("yesss got latlong",""+lat1+long1);
                    // latt.setText("Lattitude:"+lat1);
                    // longg.setText("Longitude:"+longg);
                }
            }
        }
        else
        {
            lat1=28.6100;
            long1=77.2300;
        }
        sbutton=(RelativeLayout)findViewById(R.id.sbutton);
        sbutton1=(RelativeLayout)findViewById(R.id.sbutton1);
        sbgroup=(EditText)findViewById(R.id.spnr_blood);
        sbgroup1=(EditText)findViewById(R.id.spnr_blood1);
        scity=(EditText)findViewById(R.id.spnr_city);
        sstate=(EditText)findViewById(R.id.spnr_state);
       // sloc=(EditText)findViewById(R.id.spnr_local);
        sradius=(EditText)findViewById(R.id.spnr_radius1);

        t=(TextView)findViewById(R.id.t);
        dlist1=(ListView)findViewById(R.id.dlist1);
        dlist2=(ListView)findViewById(R.id.dlist2);
        //slocal=(EditText)findViewById(R.id.spnr_local);
        search=(Button)findViewById(R.id.search);
        search1=(Button)findViewById(R.id.search1);
        sbgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setbData();
            }
        });
        sbgroup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setbData1();
            }
        });
        scity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCityData();
            }
        });
     /*   sloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocalData();
            }
        });*/
        sstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStateData();
            }
        });
        sradius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRadiusData();
            }
        });
        /*slocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocalityData();
            }
        });*/



        SharedPreferences s1= getSharedPreferences(STORAGE_DATA, Context.MODE_PRIVATE);
        group1 = s1.getString("group", "");
        rl1=(RelativeLayout)findViewById(R.id.rlayout);
        rl2=(RelativeLayout)findViewById(R.id.rlayout1);
        dlist1.setVisibility(View.VISIBLE);
        if(isNetworkAvailable()) {
            new Loadlist1().execute();
        }
        else
        {
            Toast.makeText(DonorActivity.this,"Please connect to internet", Toast.LENGTH_LONG).show();
        }
        sbutton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {

                if(p1==0)
                {
                    rl1.setVisibility(View.VISIBLE);
                    dlist1.setVisibility(View.GONE);
                    dlist2.setVisibility(View.GONE);
                    rl2.setVisibility(View.GONE);
                    p1=1;
                }
                else
                {
                    rl1.setVisibility(View.GONE);
                    dlist1.setVisibility(View.VISIBLE);
                    dlist2.setVisibility(View.GONE);
                    rl2.setVisibility(View.GONE);
                    // t.setVisibility(View.VISIBLE);
                    p1=0;
                }
            }
        });

        sbutton1.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {

                if(p12==0)
                {
                    rl2.setVisibility(View.VISIBLE);
                    rl1.setVisibility(View.GONE);
                    dlist1.setVisibility(View.GONE);
                    dlist2.setVisibility(View.GONE);
                    p12=1;
                }
                else
                {
                    rl1.setVisibility(View.GONE);
                    dlist2.setVisibility(View.GONE);
                    dlist1.setVisibility(View.VISIBLE);
                    rl2.setVisibility(View.GONE);

                    // t.setVisibility(View.VISIBLE);

                    p12=0;
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                statee=sstate.getText().toString();
                city=scity.getText().toString();
               // locality=sloc.getText().toString();
                //local=slocal.getText().toString();
                group=sbgroup.getText().toString();
                if(!group.equals("")&&!city.equals("") &&(!statee.equals("")))
                {
                    if(isNetworkAvailable()) {
                        new LoadResult().execute();
                    }
                    else
                    {
                        Toast.makeText(DonorActivity.this,"Please connect to internet", Toast.LENGTH_LONG).show();
                    }
                }
                else if(statee.equals(""))
                {
                    Toast.makeText(DonorActivity.this,"Please select state to search", Toast.LENGTH_SHORT).show();
                }

                else if(city.equals(""))
                {
                    Toast.makeText(DonorActivity.this,"Please select city to search", Toast.LENGTH_SHORT).show();
                }
                else if(group.equals(""))
                {
                    Toast.makeText(DonorActivity.this,"Please select blood group to search", Toast.LENGTH_SHORT).show();
                }


            }
        });
        search1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                sgroup=sbgroup1.getText().toString();
                srad=sradius.getText().toString();
                if(!sgroup.equals("")&&(!srad.equals("")))
                {
                    if(isNetworkAvailable()) {
                        new LoadResult1().execute();
                    }
                    else
                    {
                        Toast.makeText(DonorActivity.this,"Please connect to internet", Toast.LENGTH_LONG).show();
                    }
                }


                else if(srad.equals(""))
                {
                    Toast.makeText(DonorActivity.this,"Please select radius to search", Toast.LENGTH_SHORT).show();
                }
                else if(sgroup.equals(""))
                {
                    Toast.makeText(DonorActivity.this,"Please select blood group to search", Toast.LENGTH_SHORT).show();
                }


            }
        });
        sstate.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s)
            {
                String nameid=sstate.getText().toString();
                Cursor cursor = sqliteHelper2.getUser(nameid);
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    idd = cursor.getString(cursor.getColumnIndex("name"));
                }

                //  Toast.makeText(DonorActivity.this,idd , Toast.LENGTH_SHORT).show();
                if(isNetworkAvailable()) {
                    new Getstate().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Please connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
              /*  if (s.length() == 10) {
                    vbutton.setEnabled(true);
                    vbutton.setBackgroundResource(R.mipmap.send_enable);
                }*/

            }
        });
        scity.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s)
            {
                String name=scity.getText().toString();
                Cursor cursor = sqliteHelper1.getUser(name);
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    area = cursor.getString(cursor.getColumnIndex("area"));
                }
                if(isNetworkAvailable()) {
                    new GetCity().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Please connect to Internet",
                            Toast.LENGTH_LONG).show();
                }

               /* String REGISTER_URL = "http://191.239.57.54:8080/iDonyte/rest/common/getDistricts/stateId/"+idd;

                GetJsonObject json=new GetJsonObject();
                String response =json.getWebServceObj(REGISTER_URL);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if(jsonResponse.getString("statusMessage").equals("success")){
                        JSONArray jsonDistArr = jsonResponse.getJSONArray("districtList");
                        districIdArr = new int[jsonDistArr.length()];
                        for(int i=0;i<jsonDistArr.length();i++){
                            JSONObject jsonDisObj = jsonDistArr.getJSONObject(i);
                            String districtId = jsonDisObj.getString("districtId");
                            String districtName = jsonDisObj.getString("districtName");
                            list.add(jsonDisObj.getString("districtName"));
                        }
                        dname = list.toArray(new String[list.size()]);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
              /*  if (s.length() == 10) {
                    vbutton.setEnabled(true);
                    vbutton.setBackgroundResource(R.mipmap.send_enable);
                }*/

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

    class Getstate extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;
        String gender;
        int mflag,eflag;
        int success;
        int bg;
        String c = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar =(CircleProgressBar)findViewById(R.id.pBar);
            progressBar.setColorSchemeResources(R.color.red1);
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag

            String REGISTER_URL = mess+"/rest/common/getDistricts/stateId/"+idd;

            GetJsonObject json=new GetJsonObject();
            String response =json.getWebServceObj(REGISTER_URL);
            try {
                JSONObject jsonResponse = new JSONObject(response);
                if(jsonResponse.getString("statusMessage").equals("success")){
                    JSONArray jsonDistArr = jsonResponse.getJSONArray("districtList");
                    districIdArr = new int[jsonDistArr.length()];
                    dname = new String[jsonDistArr.length()];
                    for(int i=0;i<jsonDistArr.length();i++){
                        JSONObject jsonDisObj = jsonDistArr.getJSONObject(i);
                        String areaList = jsonDisObj.getJSONArray("areaList").toString();
                        String districtName = jsonDisObj.getString("districtName");
                        String districId=jsonDisObj.getString("districtId");
                        Boolean d=sqliteHelper4.saveUser(districtName,districId);
                        Log.d("areaa",areaList);
                        // areaName = jsonDisObj.getString("districtName");
                        dname[i]=districtName;
                        //list111.add(jsonDisObj.getString("districtName"));
                        Boolean p=sqliteHelper1.saveUser(districtName,areaList);
                    }
                    //  dname = list111.toArray(new String[list111.size()]);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return c;

        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted

            progressBar.setVisibility(View.INVISIBLE);
            scity.setText(dname[0]);


        }

    }
    class GetCity extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;
        String gender;
        int mflag,eflag;
        int success;
        int bg;
        String c = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar =(CircleProgressBar)findViewById(R.id.pBar);
            progressBar.setColorSchemeResources(R.color.red1);
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag

            try {
                JSONArray jsonAreaArr = new JSONArray(area);
                areaArr = new String[jsonAreaArr.length()];
                for(int j=0;j<jsonAreaArr.length();j++){
                    JSONObject jsonAreaObj = jsonAreaArr.getJSONObject(j);
                    String areaId = jsonAreaObj.getString("areaId");
                    areaName = jsonAreaObj.getString("areaName");

                    areaArr[j]=areaName;
                    Boolean d=sqliteHelper5.saveUser(areaName,areaId);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return c;

        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted

            progressBar.setVisibility(View.INVISIBLE);
          //  sloc.setText(areaArr[0]);


        }

    }
    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this, Locale.getDefault());
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }
    class LoadResult extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

        ArrayList<DonorModel1> countrylist;
        String pp=null;
        String[] name, OtherName,contactNo,address1,area,district,gender,state,groupId,mflag,flag;
        JSONArray data;
        List<String> list = new ArrayList<String>();
        List<String> listc = new ArrayList<String>();
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        List<String> list3 = new ArrayList<String>();
        List<String> list4 = new ArrayList<String>();
        List<String> list5 = new ArrayList<String>();
        List<String> list6 = new ArrayList<String>();
        List<String> list7 = new ArrayList<String>();
        List<String> list8 = new ArrayList<String>();
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

             try {
            String LOAD_URL =mess+"/rest/user/searchByAddress";
            // latlong=getLocationFromAddress(city);
            // Building Parameters

            Cursor cursor1 = sqliteHelper2.getUser(statee);
            if (cursor1.getCount() != 0) {
                cursor1.moveToFirst();
                state11= cursor1.getString(cursor1.getColumnIndex("name"));
            }
            Cursor cursor2 = sqliteHelper4.getUser(city);
            if (cursor2.getCount() != 0) {
                cursor2.moveToFirst();
                city11 = cursor2.getString(cursor2.getColumnIndex("name"));
            }

           /* Cursor cursor3 = sqliteHelper5.getUser(locality);
            if (cursor3.getCount() != 0) {
                cursor3.moveToFirst();
                locality11 = cursor3.getString(cursor3.getColumnIndex("name"));
            }*/
           // Log.d("Ids Gott!!!",statee+state11+city+city11+locality+locality11);
                PostJsonObject json=new PostJsonObject();
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("state",state11);
                jsonObj.put("district",city11);
                 jsonObj.put("area","");
                jsonObj.put("offset",0);
                jsonObj.put("searchText",group);
                jsonObj.put("noOfRecords",1000);

                pp=json.postJson(jsonObj,LOAD_URL);

              //  Log.d("latlongg searched k",""+latlong.latitude+latlong.longitude);
                Log.d("Response of search", pp.toString());


if(pp.contains("errorMessage"))
{
    pp="fail";
}
                else {

    JSONObject j = new JSONObject(pp);

    // Getting JSON Array node
    data = j.getJSONArray("usersList");
    Log.d("Search Returned", "" + data.length());
    Log.d("Search k Returned", "" + data);
    for (int i = 0; i < data.length(); i++) {
        //  JSONObject c = data.getJSONObject(i);

        list.add(data.getJSONObject(i).getString("firstName"));
        list1.add(data.getJSONObject(i).getString("lastName"));
        list2.add(data.getJSONObject(i).getString("contactNo"));
        list3.add(data.getJSONObject(i).getString("address1"));
        list4.add(data.getJSONObject(i).getString("areaNm"));
        list5.add(data.getJSONObject(i).getString("districtNm"));
        list6.add(data.getJSONObject(i).getString("stateNm"));
        list7.add(data.getJSONObject(i).getString("gender"));
        list8.add(data.getJSONObject(i).getString("bloodGroupId"));
        listc.add(data.getJSONObject(i).getString("contactNoDispFlag"));
    }
    // t.setText("There are "+data.length()+" tests available for you.");
    name = list.toArray(new String[list.size()]);

    OtherName = list1.toArray(new String[list1.size()]);
    contactNo = list2.toArray(new String[list2.size()]);
    address1 = list3.toArray(new String[list3.size()]);
    area = list4.toArray(new String[list4.size()]);
    district = list5.toArray(new String[list5.size()]);
    state = list6.toArray(new String[list6.size()]);
    gender = list7.toArray(new String[list7.size()]);
    groupId = list8.toArray(new String[list8.size()]);
    mflag = listc.toArray(new String[listc.size()]);
    flag=new String [name.length];
    Log.d("length", "" + name.length);
    countrylist = new ArrayList<DonorModel1>();
    for (int i = 0; i < name.length; i++) {


        DonorModel1 country = new DonorModel1(name[i], OtherName[i], contactNo[i], address1[i], area[i], district[i], state[i], gender[i], groupId[i],mflag[i]);
        countrylist.add(country);
    }

    adapter1 = new DonorAdapter1(getApplicationContext(), countrylist);
    pp="success";
}

            } catch (Exception e) {
                e.printStackTrace();
            }

            return pp;

        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted

            progressBar.setVisibility(View.INVISIBLE);

            if(file_url=="success") {

                // Toast.makeText(DonorActivity.this, "yes"+location.getLatitude()+location.getLatitude()+group1, Toast.LENGTH_LONG).show();
             //   Toast.makeText(DonorActivity.this, "yess", Toast.LENGTH_LONG).show();
                dlist2.setAdapter(adapter1);
                rl1.setVisibility(View.GONE);
                dlist1.setVisibility(View.INVISIBLE);
                dlist2.setVisibility(View.VISIBLE);
            }
            else
            {
               // Toast.makeText(DonorActivity.this, "noo", Toast.LENGTH_LONG).show();
                dlist2.setAdapter(null);
                rl1.setVisibility(View.GONE);
                dlist1.setVisibility(View.INVISIBLE);
                dlist2.setVisibility(View.VISIBLE);
                Toast.makeText(DonorActivity.this, "Sorry! There is no donor matching this criteria...", Toast.LENGTH_LONG).show();
            }
        }

    }
    class LoadResult1 extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

        ArrayList<DonorModel1> countrylist;
        String pp=null;
        String[] name, OtherName,contactNo,address1,area,district,gender,state,groupId,mflag,flag;
        JSONArray data;
        List<String> list = new ArrayList<String>();
        List<String> listc = new ArrayList<String>();
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        List<String> list3 = new ArrayList<String>();
        List<String> list4 = new ArrayList<String>();
        List<String> list5 = new ArrayList<String>();
        List<String> list6 = new ArrayList<String>();
        List<String> list7 = new ArrayList<String>();
        List<String> list8 = new ArrayList<String>();
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

            try {
                String LOAD_URL =mess+"/rest/user/searchWithRadius";
                // latlong=getLocationFromAddress(city);
                // Building Parameters



                PostJsonObject json=new PostJsonObject();
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("lat",lat1);
                jsonObj.put("lon",long1);
                jsonObj.put("searchRadius",srad);
                jsonObj.put("offset",0);
                jsonObj.put("searchText",sgroup);
                jsonObj.put("noOfRecords",1000);

                pp=json.postJson(jsonObj,LOAD_URL);

                //  Log.d("latlongg searched k",""+latlong.latitude+latlong.longitude);
                Log.d("Response of search2", pp.toString());


                if(pp.contains("errorMessage"))
                {
                    pp="fail";
                }
                else {

                    JSONObject j = new JSONObject(pp);

                    // Getting JSON Array node
                    data = j.getJSONArray("usersList");
                    Log.d("Search Returned", "" + data.length());
                    Log.d("Search k Returned", "" + data);
                    for (int i = 0; i < data.length(); i++) {
                        //  JSONObject c = data.getJSONObject(i);

                        list.add(data.getJSONObject(i).getString("firstName"));
                        list1.add(data.getJSONObject(i).getString("lastName"));
                        list2.add(data.getJSONObject(i).getString("contactNo"));
                        list3.add(data.getJSONObject(i).getString("address1"));
                        list4.add(data.getJSONObject(i).getString("areaNm"));
                        list5.add(data.getJSONObject(i).getString("districtNm"));
                        list6.add(data.getJSONObject(i).getString("stateNm"));
                        list7.add(data.getJSONObject(i).getString("gender"));
                        list8.add(data.getJSONObject(i).getString("bloodGroupId"));
                        listc.add(data.getJSONObject(i).getString("contactNoDispFlag"));
                    }
                    // t.setText("There are "+data.length()+" tests available for you.");
                    name = list.toArray(new String[list.size()]);

                    OtherName = list1.toArray(new String[list1.size()]);
                    contactNo = list2.toArray(new String[list2.size()]);
                    address1 = list3.toArray(new String[list3.size()]);
                    area = list4.toArray(new String[list4.size()]);
                    district = list5.toArray(new String[list5.size()]);
                    state = list6.toArray(new String[list6.size()]);
                    gender = list7.toArray(new String[list7.size()]);
                    groupId = list8.toArray(new String[list8.size()]);
                    mflag = listc.toArray(new String[listc.size()]);
                    flag=new String [name.length];
                    Log.d("length", "" + name.length);
                    countrylist = new ArrayList<DonorModel1>();
                    for (int i = 0; i < name.length; i++) {


                        DonorModel1 country = new DonorModel1(name[i], OtherName[i], contactNo[i], address1[i], area[i], district[i], state[i], gender[i], groupId[i],mflag[i]);
                        countrylist.add(country);
                    }

                    adapter1 = new DonorAdapter1(getApplicationContext(), countrylist);
                    pp="success";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return pp;

        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted

            progressBar.setVisibility(View.INVISIBLE);

            if(file_url=="success") {

                // Toast.makeText(DonorActivity.this, "yes"+location.getLatitude()+location.getLatitude()+group1, Toast.LENGTH_LONG).show();
                //   Toast.makeText(DonorActivity.this, "yess", Toast.LENGTH_LONG).show();
                dlist2.setAdapter(adapter1);
                rl1.setVisibility(View.GONE);
                rl2.setVisibility(View.GONE);
                dlist1.setVisibility(View.INVISIBLE);
                dlist2.setVisibility(View.VISIBLE);
            }
            else
            {
                // Toast.makeText(DonorActivity.this, "noo", Toast.LENGTH_LONG).show();
                dlist2.setAdapter(null);
                rl1.setVisibility(View.GONE);
                rl2.setVisibility(View.GONE);
                dlist1.setVisibility(View.INVISIBLE);
                dlist2.setVisibility(View.VISIBLE);
                Toast.makeText(DonorActivity.this, "Sorry! There is no donor matching this criteria...", Toast.LENGTH_LONG).show();
            }
        }

    }


    class Loadlist1 extends AsyncTask<String, String, String> {
        String[] name, OtherName,contactNo,address1,area,district,gender,state,groupId,mflag;
        ArrayList<DonorModel1> countrylist1;
        /**
         * Before starting background thread Show Progress Dialog
         * */
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

            try {

                String LOAD_URL =mess+"/rest/user/searchWithRadius";
                // latlong=getLocationFromAddress(city);
                // Building Parameters



                PostJsonObject json=new PostJsonObject();
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("lat",lat1);
                jsonObj.put("lon",long1);
                jsonObj.put("searchRadius","100");
                jsonObj.put("offset",0);
                jsonObj.put("searchText","AB+");
                jsonObj.put("noOfRecords",100);

               // pp=json.postJson(jsonObj,LOAD_URL);

                p=json.postJson(jsonObj,LOAD_URL);
                Log.d("Response of default", p.toString());
                JSONObject j = new JSONObject(p);
//Log.d("latt",location.getLatitude()+" "+location.getLongitude());
                // Getting JSON Array node
                data = j.getJSONArray("usersList");
                Log.d("Returned",""+data.length());
                Log.d("Returned",""+data);
                for (int i = 0; i < data.length(); i++) {
                    //  JSONObject c = data.getJSONObject(i);

                    list9.add(data.getJSONObject(i).getString("firstName"));
                    list10.add(data.getJSONObject(i).getString("lastName"));
                    list11.add(data.getJSONObject(i).getString("contactNo"));
                    list12.add(data.getJSONObject(i).getString("address1"));
                    list13.add(data.getJSONObject(i).getString("areaNm"));
                    list14.add(data.getJSONObject(i).getString("districtNm"));
                    list15.add(data.getJSONObject(i).getString("stateNm"));
                    list16.add(data.getJSONObject(i).getString("gender"));
                    list17.add(data.getJSONObject(i).getString("bloodGroupId"));
                    listcc.add(data.getJSONObject(i).getString("contactNoDispFlag"));

                }
                // t.setText("There are "+data.length()+" tests available for you.");
                name = list9.toArray(new String[list9.size()]);

                OtherName = list10.toArray(new String[list10.size()]);
                contactNo = list11.toArray(new String[list11.size()]);
                address1 = list12.toArray(new String[list12.size()]);
                area = list13.toArray(new String[list13.size()]);
                district = list14.toArray(new String[list14.size()]);
                state = list15.toArray(new String[list15.size()]);
                gender = list16.toArray(new String[list16.size()]);
                groupId = list17.toArray(new String[list17.size()]);
                mflag = listcc.toArray(new String[listcc.size()]);
                countrylist1 = new ArrayList<DonorModel1>();
                for (int i = 0; i < name.length; i++) {

                    DonorModel1 country = new DonorModel1(name[i],OtherName[i],contactNo[i],address1[i],area[i],district[i],state[i],gender[i],groupId[i],mflag[i]);
                    countrylist1.add(country);
                }

                adapter1 = new DonorAdapter1(getApplicationContext(), countrylist1);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return p;

        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted

            progressBar.setVisibility(View.INVISIBLE);

            if(file_url.contains("errorMessage")) {

                // Toast.makeText(DonorActivity.this, "yes"+location.getLatitude()+location.getLatitude()+group1, Toast.LENGTH_LONG).show();



            }
            else
            {
                dlist1.setAdapter(adapter1);
            }
        }

    }

    /* private void setLocalityData() {

         AlertDialog.Builder builder = new AlertDialog.Builder(DonorActivity.this);
         builder.setTitle("Select Locality");
         //builder.setI
         builder.setItems(areaArr, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int item) {
                 //Toast.makeText(getApplicationContext(), con.get(item).getCountrName(), Toast.LENGTH_SHORT).show();
                 slocal.setText(areaArr[item]);
                 //System.out.println("Item is: "+items[item]);
                 *//*CONTRY_ID = con.get(item).getCountryId();
                stateET.requestFocus();*//*
            }
        });
        //AlertDialog alert = builder.create();
        builder.show();
    }*/
    private void setCityData() {
        //   final CharSequence[] items = {"Noida","Greater Noida","Bareilly","Badaun","Patna","Gaziyabad","Ujhani"};
        AlertDialog.Builder builder = new AlertDialog.Builder(DonorActivity.this);
        builder.setTitle("Select City");
        //builder.setI
        builder.setItems(dname, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //Toast.makeText(getApplicationContext(), con.get(item).getCountrName(), Toast.LENGTH_SHORT).show();
                scity.setText(dname[item]);
                //System.out.println("Item is: "+items[item]);
                /*CONTRY_ID = con.get(item).getCountryId();
                stateET.requestFocus();*/
            }
        });
        //AlertDialog alert = builder.create();
        builder.show();
    }
/*    private void setLocalData() {
        //   final CharSequence[] items = {"Noida","Greater Noida","Bareilly","Badaun","Patna","Gaziyabad","Ujhani"};
        AlertDialog.Builder builder = new AlertDialog.Builder(DonorActivity.this);
        builder.setTitle("Select Locality");
        //builder.setI
        builder.setItems(areaArr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //Toast.makeText(getApplicationContext(), con.get(item).getCountrName(), Toast.LENGTH_SHORT).show();
                sloc.setText(areaArr[item]);
                //System.out.println("Item is: "+items[item]);
                *//*CONTRY_ID = con.get(item).getCountryId();
                stateET.requestFocus();*//*
            }
        });
        //AlertDialog alert = builder.create();
        builder.show();
    }*/
    private void setStateData() {
        //final String[] itemsarray = loadArray("sarray",getApplicationContext());
        final String[] items = loadArray("sarray",getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(DonorActivity.this);
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
    private void setRadiusData() {
        //final String[] itemsarray = loadArray("sarray",getApplicationContext());
        final String[] items = {"5","10","15","20","25","30","35","40","45","50","100"};
        AlertDialog.Builder builder = new AlertDialog.Builder(DonorActivity.this);
        builder.setTitle("Select Radius");
        //builder.setI
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //Toast.makeText(getApplicationContext(), con.get(item).getCountrName(), Toast.LENGTH_SHORT).show();
                sradius.setText(items[item]);
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
    private void setbData1() {
        final String[] items = loadArray("barray",getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(DonorActivity.this);
        builder.setTitle("Select Blood Group");
        //builder.setI
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //Toast.makeText(getApplicationContext(), con.get(item).getCountrName(), Toast.LENGTH_SHORT).show();
                sbgroup1.setText(items[item]);
                //System.out.println("Item is: "+items[item]);
                /*CONTRY_ID = con.get(item).getCountryId();
                stateET.requestFocus();*/
            }
        });
        //AlertDialog alert = builder.create();
        builder.show();
    }
    private void setbData() {
        final String[] items = loadArray("barray",getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(DonorActivity.this);
        builder.setTitle("Select Blood Group");
        //builder.setI
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //Toast.makeText(getApplicationContext(), con.get(item).getCountrName(), Toast.LENGTH_SHORT).show();
                sbgroup.setText(items[item]);
                //System.out.println("Item is: "+items[item]);
                /*CONTRY_ID = con.get(item).getCountryId();
                stateET.requestFocus();*/
            }
        });
        //AlertDialog alert = builder.create();
        builder.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_donor, menu);
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

    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                DonorActivity.this);

        alertDialog.setTitle( "Enable Location Settings");

        alertDialog
                .setMessage("Please enable location to allow users to locate you for platelates donation/searches.");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        DonorActivity.this.startActivity(intent);
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
