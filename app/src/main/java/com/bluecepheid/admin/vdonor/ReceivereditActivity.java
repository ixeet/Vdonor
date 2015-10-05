package com.bluecepheid.admin.vdonor;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.Profile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceivereditActivity extends Activity implements LocationListener {
    EditText eName,eNumber,eLastdonate,eAddress,lName;
    EditText spCity,spLocality,spGen,spState;
    ImageView imgCity,imgLoc;
    Button bSubmit,setdonor;
    Profile profile;
String addr;
    JSONObject location1;
    String name1,lname1,city1,state1,local1,gender1,phone1,group1,add1,bg,userid,location_string;
    ImageView imgProfile;
    Geocoder geocoder;
    List<Address> addresses;
    private final int SELECT_PHOTO = 22;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String STORAGE_DATA = "Userdata";
    Bitmap selectedImage;

    String p,fname,lname,imagePreferance;
    public static final String STORAGE_IMAGE = "Image";
ImageView cancel;
    double lat1,long1;
    private int year, month, day;
    List<String> list = new ArrayList<String>();
    private Calendar calendar;
    CheckBox c1,c2;
    String semail,sname,locality1,email;
    Location location;
    public static final String STORAGE_FB = "FB";
    public static final String STORAGE_USERTYPE = "usertype";
    LocationManager locationManager;
    TextView latt,longg;
    int [] districIdArr;
    SqliteHelper2 sqliteHelper2;
    SqliteHelper1 sqliteHelper1;
    String idd,area,areaName,name,state,gend,phone,add,locality,city,group,gender;
    String [] areaArr,dname;
    CircleProgressBar progressBar;
    SqliteHelper4 sqliteHelper4;
    SqliteHelper5 sqliteHelper5;
    SqliteHelper3 sqliteHelper3;
    String[]splited;
    String mess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiveredit);
      /*  if (android.os.Build.VERSION.SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/
        latt=(TextView)findViewById(R.id.latt);
        longg=(TextView)findViewById(R.id.longg);
        mess= getApplicationContext().getResources().getString(R.string.base_url);
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

                    latt.setText("Lattitude:"+lat1);
                    longg.setText("Longitude:"+long1);
                }
            }
        }
        else
        {
            lat1=28.6100;
            long1=77.2300;
        }
        sqliteHelper2 = new SqliteHelper2(this);
        sqliteHelper1 = new SqliteHelper1(this);
        sqliteHelper4 = new SqliteHelper4(this);
        sqliteHelper5 = new SqliteHelper5(this);
        sqliteHelper3 = new SqliteHelper3(this);
        SharedPreferences sharedPreferences = getSharedPreferences(STORAGE_FB, Context.MODE_PRIVATE);

        semail = sharedPreferences.getString("str_email", "");
        sname = sharedPreferences.getString("fname", "");

        if(isNetworkAvailable()) {
            FacebookSdk.sdkInitialize(getApplicationContext());
            profile = Profile.getCurrentProfile();
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Please connect to Internet",
                    Toast.LENGTH_LONG).show();
        }

        cancel=(ImageView)findViewById(R.id.cancel);
        eName=(EditText)findViewById(R.id.f_name);
        lName=(EditText)findViewById(R.id.l_name);
        eNumber=(EditText)findViewById(R.id.m_number);
        //eEmail=(EditText)findViewById(R.id.email_id);
        //eDob=(EditText)findViewById(R.id.dob);
        eLastdonate=(EditText)findViewById(R.id.l_donate);
        eAddress=(EditText)findViewById(R.id.add);
        spGen=(EditText)findViewById(R.id.spnr_gen);
        //spBgrup=(EditText)findViewById(R.id.spnr_blood);
        spCity=(EditText)findViewById(R.id.spnr_city);
        spState=(EditText)findViewById(R.id.spnr_state);
        spLocality=(EditText)findViewById(R.id.spnr_loc);
        bSubmit=(Button)findViewById(R.id.btnsubmit);
        setdonor=(Button)findViewById(R.id.bedonor);
        //imgGen=(ImageView)findViewById(R.id.img_gen);
        //imgDob=(ImageView)findViewById(R.id.img_dob);
        imgCity=(ImageView)findViewById(R.id.img_city);
        imgLoc=(ImageView)findViewById(R.id.img_loc);
        imgProfile=(ImageView)findViewById(R.id.pro_img);
        //c1=(CheckBox)findViewById(R.id.pub_chk1);
        //c2=(CheckBox)findViewById(R.id.pub_chk2);
        eName.getBackground().setColorFilter(getResources().getColor(R.color.editcolor), PorterDuff.Mode.SRC_ATOP);
        lName.getBackground().setColorFilter(getResources().getColor(R.color.editcolor), PorterDuff.Mode.SRC_ATOP);
        eNumber.getBackground().setColorFilter(getResources().getColor(R.color.editcolor), PorterDuff.Mode.SRC_ATOP);
        //  eEmail.getBackground().setColorFilter(getResources().getColor(R.color.editcolor), PorterDuff.Mode.SRC_ATOP);
        // eDob.getBackground().setColorFilter(getResources().getColor(R.color.editcolor), PorterDuff.Mode.SRC_ATOP);
        eLastdonate.getBackground().setColorFilter(getResources().getColor(R.color.editcolor), PorterDuff.Mode.SRC_ATOP);
        eAddress.getBackground().setColorFilter(getResources().getColor(R.color.editcolor), PorterDuff.Mode.SRC_ATOP);
        spGen.getBackground().setColorFilter(getResources().getColor(R.color.editcolor), PorterDuff.Mode.SRC_ATOP);
        //spBgrup.getBackground().setColorFilter(getResources().getColor(R.color.editcolor), PorterDuff.Mode.SRC_ATOP);
        spCity.getBackground().setColorFilter(getResources().getColor(R.color.editcolor), PorterDuff.Mode.SRC_ATOP);
        spLocality.getBackground().setColorFilter(getResources().getColor(R.color.editcolor), PorterDuff.Mode.SRC_ATOP);

        if(!sname.equals("")) {
            eName.setText(sname);
        }
        SharedPreferences sharedPreferences11 = getSharedPreferences(STORAGE_IMAGE, Context.MODE_PRIVATE);
        imagePreferance = sharedPreferences11.getString("imagePreferance", "");
        if(!imagePreferance.equals("")) {
            Bitmap p = decodeBase64(imagePreferance);
            imgProfile.setImageBitmap(p);
        }
        else {
            if (profile != null) {
                if(isNetworkAvailable()) {
                    new getc().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Please connect to Internet",
                            Toast.LENGTH_LONG).show();
                }

            } else {
                Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.mipmap.no_photo);
                imgProfile.setImageBitmap(icon1);
            }
        }
        SharedPreferences s1= getSharedPreferences(STORAGE_DATA, Context.MODE_PRIVATE);
        name1 = s1.getString("name", "");
        lname1 = s1.getString("lname", "");
        phone1 = s1.getString("phone", "");
        add1 = s1.getString("add", "");
        local1 = s1.getString("locality", "");
        city1 = s1.getString("city", "");
        gender1 = s1.getString("gender", "");
        state1 = s1.getString("state", "");
        group1 = s1.getString("group", "");
        userid=s1.getString("userid","");
       location_string=s1.getString("location_string","");
        if(userid.equals("")) {
            if(isNetworkAvailable()) {
                new Send().execute();
            }
            else
            {
                Toast.makeText(getApplicationContext(),
                        "Please connect to Internet",
                        Toast.LENGTH_LONG).show();
            }
        }
        if(location_string.equals(""))
        {
            if(isNetworkAvailable()) {
                geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(lat1, long1, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    addr = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();

                } catch (Exception e) {

                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),
                        "Please connect to Internet",
                        Toast.LENGTH_LONG).show();
            }

            location_string = addr + "," + city + "," + state;
       /* Toast.makeText(getApplicationContext(),
                location_string,
                Toast.LENGTH_LONG).show();*/
            if(location_string.equals("null,null,null")) {
                if (isNetworkAvailable()) {
                    new address().execute();


                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        eName.setText(name1);
        lName.setText(lname1);
        eNumber.setText(phone1);
        eAddress.setText(add1);
        spLocality.setText(local1);
        spCity.setText(city1);
        spGen.setText(gender1);
        spState.setText(state1);
        eLastdonate.setText(group1);

       /* Toast.makeText(getApplicationContext(),
                location_string,
                Toast.LENGTH_LONG).show();*/
        String nameid=spState.getText().toString();
        Cursor cursor = sqliteHelper2.getUser(nameid);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            idd = cursor.getString(cursor.getColumnIndex("name"));
        }
        if(isNetworkAvailable()) {
            new Getstate().execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Please connect to Internet",
                    Toast.LENGTH_LONG).show();
        }
        String cname=spCity.getText().toString();
        Cursor cursor1 = sqliteHelper1.getUser(cname);
        if (cursor1.getCount() != 0) {
            cursor1.moveToFirst();
            area = cursor1.getString(cursor1.getColumnIndex("area"));
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
       //Toast.makeText(ReceivereditActivity.this,name1+gender1 , Toast.LENGTH_SHORT).show();

   /*     Uri pp=profile.getProfilePictureUri(200,200);
        String p1=pp.toString();
        Toast.makeText(RegisterActivity.this,p1 , Toast.LENGTH_SHORT).show();
        Log.d("image",p1);
       // p = compressImage(p1);
        selectedImage = BitmapFactory.decodeFile(p1);
        imgProfile.setImageBitmap(selectedImage);*/
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFrom();
            }
        });
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        eLastdonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLastdonate();
            }
        });

        spGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGenData();
            }
        });
        spState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStateData();
            }
        });
        spCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCityData();
            }
        });
        spLocality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocalityData();
            }
        });
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences s1= getSharedPreferences(STORAGE_USERTYPE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = s1.edit();
                editor.putString("type","rcvr");
                editor.apply();
                editor.commit();
                name=eName.getText().toString();
                lname=lName.getText().toString();
               // gend=spGen.getText().toString();
                state=spState.getText().toString();
                //  String email=eEmail.getText().toString();
                phone=eNumber.getText().toString();
                //String DOB=eDob.getText().toString();
                add=eAddress.getText().toString();


               group=eLastdonate.getText().toString();
                gender=spGen.getText().toString();

                //  String email=eEmail.getText().toString();
                phone=eNumber.getText().toString();
                //String DOB=eDob.getText().toString();
                add=eAddress.getText().toString();
                locality=spLocality.getText().toString();
                city=spCity.getText().toString();
                state=spState.getText().toString();
                group=eLastdonate.getText().toString();

                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(name);
                boolean b = m.find();
                Matcher m1 = p.matcher(lname);
                boolean b1 = m1.find();
                // int month=(Integer.parseInt(eLastdonate.getText().toString()));
                // Toast.makeText(RegisterActivity.this,"C1"+c1.isChecked()+"C2"+c2.isChecked() , Toast.LENGTH_SHORT).show();
                if((!b)&&(!b1)&&!name.equals("")&& (!lname.equals("")&&(phone.length()==10)&&(!add.equals("")&&(!locality.equals("")&&(!city.equals("")&&(!state.equals("")&&(!group.equals("")))))))) {
                    if(isNetworkAvailable()) {
                        new Register().execute();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "Please connect to Internet",
                                Toast.LENGTH_LONG).show();
                    }
                }
                else if(name.equals(""))
                {
                    Toast.makeText(ReceivereditActivity.this,"Please enter first name" , Toast.LENGTH_SHORT).show();
                }
                else if(lname.equals(""))
                {
                    Toast.makeText(ReceivereditActivity.this,"Please enter last name" , Toast.LENGTH_SHORT).show();
                }

                else if(add.equals(""))
                {
                    Toast.makeText(ReceivereditActivity.this,"Please enter your address" , Toast.LENGTH_SHORT).show();
                }
                else if(locality.equals(""))
                {
                    Toast.makeText(ReceivereditActivity.this,"Please select your locality" , Toast.LENGTH_SHORT).show();
                }
                else if(city.equals(""))
                {
                    Toast.makeText(ReceivereditActivity.this,"Please select your city" , Toast.LENGTH_SHORT).show();
                }
                else if(state.equals(""))
                {
                    Toast.makeText(ReceivereditActivity.this,"Please select your state" , Toast.LENGTH_SHORT).show();
                }
                else if(group.equals(""))
                {
                    Toast.makeText(ReceivereditActivity.this,"Please select your blood group" , Toast.LENGTH_SHORT).show();
                }
                else if(phone.equals(""))
                {
                    Toast.makeText(ReceivereditActivity.this,"Please enter your phone number" , Toast.LENGTH_SHORT).show();
                }

                else if(phone.length()!=10)
                {
                    Toast.makeText(ReceivereditActivity.this,"Please enter 10 digit valid phone number" , Toast.LENGTH_SHORT).show();
                }

                else
                if(b)
                {
                    Toast.makeText(ReceivereditActivity.this,"Please enter valid first name" , Toast.LENGTH_SHORT).show();
                }
                else
                if(b1)
                {
                    Toast.makeText(ReceivereditActivity.this,"Please enter valid last name" , Toast.LENGTH_SHORT).show();
                }




            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ReceivereditActivity.this,WelcomeActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });
        spState.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s)
            {
                String nameid=spState.getText().toString();
                Cursor cursor = sqliteHelper2.getUser(nameid);
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    idd = cursor.getString(cursor.getColumnIndex("name"));
                }
                if(isNetworkAvailable()) {
                    new Getstate().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Please connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
                // Toast.makeText(RegisterActivity.this,idd , Toast.LENGTH_SHORT).show();


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
              /*  if (s.length() == 10) {
              /*  if (s.length() == 10) {
                    vbutton.setEnabled(true);
                    vbutton.setBackgroundResource(R.mipmap.send_enable);
                }*/

            }
        });
        spCity.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s)
            {
                String name=spCity.getText().toString();
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
        setdonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=eName.getText().toString();
                lname=lName.getText().toString();
                gend=spGen.getText().toString();
                state=spState.getText().toString();
                //  String email=eEmail.getText().toString();
                phone=eNumber.getText().toString();
                //String DOB=eDob.getText().toString();
                add=eAddress.getText().toString();
                locality=spLocality.getText().toString();
                city=spCity.getText().toString();

                group=eLastdonate.getText().toString();
                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(name);
                boolean b = m.find();
                Matcher m1 = p.matcher(lname);
                boolean b1 = m1.find();
                // int month=(Integer.parseInt(eLastdonate.getText().toString()));
                // Toast.makeText(RegisterActivity.this,"C1"+c1.isChecked()+"C2"+c2.isChecked() , Toast.LENGTH_SHORT).show();
                if((!b)&&(!b1)&&!name.equals("")&& (!lname.equals("")&&(phone.length()==10)&&(!add.equals("")&&(!locality.equals("")&&(!city.equals("")&&(!state.equals("")&&(!group.equals("")))))))) {
                    if(isNetworkAvailable()) {
                        new SetDonor().execute();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "Please connect to Internet",
                                Toast.LENGTH_LONG).show();
                    }
                }
                else if(name.equals(""))
                {
                    Toast.makeText(ReceivereditActivity.this,"Please enter first name" , Toast.LENGTH_SHORT).show();
                }
                else if(lname.equals(""))
                {
                    Toast.makeText(ReceivereditActivity.this,"Please enter last name" , Toast.LENGTH_SHORT).show();
                }

                else if(add.equals(""))
                {
                    Toast.makeText(ReceivereditActivity.this,"Please enter your address" , Toast.LENGTH_SHORT).show();
                }
                else if(locality.equals(""))
                {
                    Toast.makeText(ReceivereditActivity.this,"Please select your locality" , Toast.LENGTH_SHORT).show();
                }
                else if(city.equals(""))
                {
                    Toast.makeText(ReceivereditActivity.this,"Please select your city" , Toast.LENGTH_SHORT).show();
                }
                else if(state.equals(""))
                {
                    Toast.makeText(ReceivereditActivity.this,"Please select your state" , Toast.LENGTH_SHORT).show();
                }
                else if(group.equals(""))
                {
                    Toast.makeText(ReceivereditActivity.this,"Please select your blood group" , Toast.LENGTH_SHORT).show();
                }
                else if(phone.equals(""))
                {
                    Toast.makeText(ReceivereditActivity.this,"Please enter your phone number" , Toast.LENGTH_SHORT).show();
                }

                else if(phone.length()!=10)
                {
                    Toast.makeText(ReceivereditActivity.this,"Please enter 10 digit valid phone number" , Toast.LENGTH_SHORT).show();
                }

                else
                if(b)
                {
                    Toast.makeText(ReceivereditActivity.this,"Please enter valid first name" , Toast.LENGTH_SHORT).show();
                }
                else
                if(b1)
                {
                    Toast.makeText(ReceivereditActivity.this,"Please enter valid last name" , Toast.LENGTH_SHORT).show();
                }

            }
            });
    }
    class address extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */


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




            HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?latlng="+lat1+","+long1+"&sensor=true");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            StringBuilder stringBuilder = new StringBuilder();

            try {
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                int b;
                while ((b = stream.read()) != -1) {
                    stringBuilder.append((char) b);
                }
            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = new JSONObject(stringBuilder.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try{
                location1 = jsonObject.getJSONArray("results").getJSONObject(0);
                location_string = location1.getString("formatted_address");
            } catch (JSONException e1) {
                e1.printStackTrace();

            }
            return location_string;






        }


        protected void onPostExecute(String photo) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), photo, Toast.LENGTH_SHORT)
                    .show();


        }
    }
    class Send extends AsyncTask<String, String, String> {

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
            progressBar = (CircleProgressBar) findViewById(R.id.pBar);
            progressBar.setColorSchemeResources(R.color.red1);
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);


        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag


            String REGISTER_URL = mess+"/rest/user/getDetail/contactNo/"+phone1;

            GetJsonObject json=new GetJsonObject();
            String response =json.getWebServceObj(REGISTER_URL);
            JSONObject data;
            JSONArray statearray,bloodarray;
            JSONObject j = null;
            try {
                j = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.d("Server data",s);
            try {
                if(j.getString("statusMessage").equals("success"))
                {



                    // Getting JSON Array node


                    data = j.getJSONObject("userDetail");


                    userid=data.getString("userId");


                    Log.d("Got Id",userid);

                    SharedPreferences s1= getSharedPreferences(STORAGE_DATA, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = s1.edit();
                    //   editor.clear();


                    editor.putString("userid",userid);

                    // editor.putString("lastdonated", lastdonated);

                    editor.apply();
                    editor.commit();


                }
                else {

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

            //  pDialog.dismiss();
            progressBar.setVisibility(View.INVISIBLE);

         //   Toast.makeText(ReceivereditActivity.this, userid ,Toast.LENGTH_LONG).show();


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

    class Register extends AsyncTask<String, String, String> {


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


                Cursor cursor = sqliteHelper3.getUser(group);
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    bg = cursor.getString(cursor.getColumnIndex("name"));
                }
                if(spGen.equals("Mr."))
                {
                    gender="M";

                }
                else
                {
                    gender="F";
                }
                Cursor cursor1 = sqliteHelper2.getUser(state);
                if (cursor1.getCount() != 0) {
                    cursor1.moveToFirst();
                    state1 = cursor1.getString(cursor1.getColumnIndex("name"));
                }
                Cursor cursor2 = sqliteHelper4.getUser(city);
                if (cursor2.getCount() != 0) {
                    cursor2.moveToFirst();
                    city1 = cursor2.getString(cursor2.getColumnIndex("name"));
                }
                Cursor cursor3 = sqliteHelper5.getUser(locality);
                if (cursor3.getCount() != 0) {
                    cursor3.moveToFirst();
                    locality1 = cursor3.getString(cursor3.getColumnIndex("name"));
                }
                String REGISTER2_URL = mess+"/rest/user/registeration/receiver";
                PostJsonObject json=new PostJsonObject();
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("firstName",name);
                jsonObj.put("lastName",lname);
                jsonObj.put("contactNo",phone);
                jsonObj.put("address1",add);
                jsonObj.put("area",locality1);
                jsonObj.put("district",city1);
                jsonObj.put("gender",gender);
                jsonObj.put("userId",userid);
                jsonObj.put("state",state1);
                jsonObj.put("lat",""+lat1);
                jsonObj.put("lon",""+long1);
                jsonObj.put("currentLoc",location_string);

                jsonObj.put("bloodGroupId",bg);

                String p=json.postJson(jsonObj,REGISTER2_URL);
                Log.d("Response",p.toString());
                String res=p.trim();
                JSONObject jobj= null;
                try {
                    jobj = new JSONObject(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    c = jobj.getString("statusMessage");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
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


            if (file_url.equals("success")) {

                SharedPreferences s11 = getSharedPreferences(STORAGE_DATA, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = s11.edit();
                //   editor.clear();

                editor1.putString("name", name);
                editor1.putString("lname", lname);
                editor1.putString("phone", phone);
                editor1.putString("add", add);
                editor1.putString("locality", locality);
                editor1.putString("city", city);
                editor1.putString("gender", gender);
                editor1.putString("state", state);
                editor1.putString("location_string",location_string);
                editor1.putString("group", group);


                editor1.apply();
                editor1.commit();
                Toast.makeText(ReceivereditActivity.this, "Profile Updated.", Toast.LENGTH_LONG).show();
                Intent i = new Intent(ReceivereditActivity.this, WelcomeActivity.class);
                startActivity(i);
                finish();


            } else {
                Toast.makeText(ReceivereditActivity.this, file_url, Toast.LENGTH_LONG).show();
            }
        }

        }
    class SetDonor extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;
        String gender;
        int mflag,eflag;
        int success;
        String bg;
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


                Cursor cursor = sqliteHelper3.getUser(group);
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    bg = cursor.getString(cursor.getColumnIndex("name"));
                }
                if(gend.equals("Mr."))
                {
                    gender="M";

                }
                else
                {
                    gender="F";
                }

               /* if(c2.isChecked())
                {
                   eflag=1;
                }
                else
                {
                    eflag=0;
                }*/
                Cursor cursor1 = sqliteHelper2.getUser(state);
                if (cursor1.getCount() != 0) {
                    cursor1.moveToFirst();
                    state1 = cursor1.getString(cursor1.getColumnIndex("name"));
                }
                Cursor cursor2 = sqliteHelper4.getUser(city);
                if (cursor2.getCount() != 0) {
                    cursor2.moveToFirst();
                    city1 = cursor2.getString(cursor2.getColumnIndex("name"));
                }
                Cursor cursor3 = sqliteHelper5.getUser(locality);
                if (cursor3.getCount() != 0) {
                    cursor3.moveToFirst();
                    locality1 = cursor3.getString(cursor2.getColumnIndex("name"));
                }
                String SETDONOR_URL = mess+"/rest/user/registeration/donar";

                PostJsonObject json=new PostJsonObject();
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("firstName",name);
                jsonObj.put("lastName",lname);
                jsonObj.put("contactNo",phone);
                jsonObj.put("address1",add);
                jsonObj.put("area",locality1);
                jsonObj.put("district",city1);
                jsonObj.put("gender",gender);
                jsonObj.put("state",state1);
                jsonObj.put("lat",lat1);
                jsonObj.put("lon",long1);
                jsonObj.put("currentLoc",location_string);
                //jsonObj.put("location",locality);

               // jsonObj.put("dateOfBirth",date);
                jsonObj.put("bloodGroupId",bg);

                jsonObj.put("contactNoDispFlag",0);
                jsonObj.put("emailDispFlag",0);
                jsonObj.put("donateTypeId",""+1);
                jsonObj.put("isDonated",""+0);
                jsonObj.put("dateOfBirth","1990-09-15");
             //   Log.d("got Dataa",""+name+" "+lname+phone+add+state1+gend+city1+locality1+bg+location.getLatitude()+" "+location.getLongitude());
                String p=json.postJson(jsonObj,SETDONOR_URL);
                Log.d("Response got",p.toString());
                String res=p.trim();
                JSONObject jobj= null;
                try {
                    jobj = new JSONObject(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    c = jobj.getString("statusMessage");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               /* // check your log for json response
                //Log.d("Login attempt", json.toString());

                // json success tag
              *//**//*  success = json.getInt("status");
                if (success==1001) {
                    Log.d("Login Successful!", json.toString());

                    *//**//**//**//*p1=json.getString("user_name").toString();
                    p2=json.getString("user_id").toString();
                    p3=json.getString("password").toString();
                    p4=json.getString("email_id").toString();
                    p5=json.getString("address").toString();
                    p6=json.getString("phone_no").toString();
                    SharedPreferences sharedPreferences = getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_id",p2);
                    editor.putString("name",p1);
                    editor.putString("password",p3);
                    editor.putString("email_id",p4);
                    editor.putString("address",p5);
                    editor.putString("phone_no", p6);
                    editor.putBoolean("hasLoggedIn", true);
                    editor.apply();*//**//**//**//*
                    Intent i = new Intent(RegisterActivity.this, Shareskip.class);
                    startActivity(i);
                    //   i.putExtra("name1", p1);
                    // i.putExtra("user_id", p2);


                    startActivity(i);
                    finish();

                }
                else{
                    Log.d("Login Failure!", json.getString("statusMessage"));
                    return json.getString("statusMessage");

                }
*/
            } catch (Exception e) {
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

            if(file_url.equals("success")){

                // Toast.makeText(RegisterActivity.this, file_url, Toast.LENGTH_LONG).show();
                SharedPreferences s1= getSharedPreferences(STORAGE_DATA, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = s1.edit();
                //   editor.clear();

                editor.putString("name", name);
                editor.putString("lname", lname);
                editor.putString("phone", phone);
                editor.putString("add", add);
                editor.putString("locality", locality);
                editor.putString("city", city);
                editor.putString("gender",gend);
                editor.putString("lat",""+lat1);
                editor.putString("long",""+long1);
                editor.putString("state", state);
               // editor.putString("email", email);
               // editor.putString("date", DOB);
                editor.putString("group", group);
                editor.putString("location_string",location_string);
                editor.putInt("mflag", 0);

                editor.putInt("eflag", 0);
              //  editor.putString("lastdonated", lastdonated);

                editor.apply();
                editor.commit();
                SharedPreferences s11= getSharedPreferences(STORAGE_USERTYPE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = s11.edit();
                editor1.putString("type","donor");
                editor1.apply();
                editor1.commit();
                Intent i = new Intent(ReceivereditActivity.this, Shareskip.class);
                startActivity(i);
                finish();
                Log.d("gendra",gend);
                Toast.makeText(ReceivereditActivity.this, "You are registered as a donor" ,Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(ReceivereditActivity.this, file_url, Toast.LENGTH_LONG).show();
            }


        }

    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
       /* BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;*/
        Bitmap bmp = BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
        return bmp;
    }
    class getc extends AsyncTask<String, String, Bitmap> {

        /**
         * Before starting background thread Show Progress Dialog
         */

        Profile profile = Profile.getCurrentProfile();
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag



            try {

                url = new URL("https://graph.facebook.com/"+profile.getId()+"/picture?type=large");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpURLConnection.setFollowRedirects(true);
            conn.setInstanceFollowRedirects(true);
            Bitmap fbpicture = null;
            try {
                fbpicture = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fbpicture;






        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(Bitmap photo) {

            imgProfile.setImageBitmap(photo);
            SharedPreferences s2= getSharedPreferences(STORAGE_IMAGE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = s2.edit();
            editor.putString("imagePreferance", encodeTobase64(photo));
            editor.apply();
            editor.commit();
        }
    }






    private void setLocalityData() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ReceivereditActivity.this);
        builder.setTitle("Select Locality");
        //builder.setI
        builder.setItems(areaArr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //Toast.makeText(getApplicationContext(), con.get(item).getCountrName(), Toast.LENGTH_SHORT).show();
                spLocality.setText(areaArr[item]);
                //System.out.println("Item is: "+items[item]);
                /*CONTRY_ID = con.get(item).getCountryId();
                stateET.requestFocus();*/
            }
        });
        //AlertDialog alert = builder.create();
        builder.show();
    }
    class Getstate extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;
        String gender;
        int mflag, eflag;
        int success;
        int bg;
        String c = null;

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
                        dname[i]=districtName;
                        //   list.add(jsonDisObj.getString("districtName"));
                        Boolean p=sqliteHelper1.saveUser(districtName,areaList);
                    }
                    //  dname = list.toArray(new String[list.size()]);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return c;

        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted

            progressBar.setVisibility(View.INVISIBLE);
             spCity.setText(dname[0]);

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
                    Log.d("area saved",""+d);
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
            spLocality.setText(areaArr[0]);


        }

    }
    private void setLastdonate() {
        final String[] items = loadArray("barray",getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(ReceivereditActivity.this);
        builder.setTitle("Select Blood Group");
        //builder.setI
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //Toast.makeText(getApplicationContext(), con.get(item).getCountrName(), Toast.LENGTH_SHORT).show();
                eLastdonate.setText(items[item]);
                //System.out.println("Item is: "+items[item]);
                /*CONTRY_ID = con.get(item).getCountryId();
                stateET.requestFocus();*/
            }
        });
        //AlertDialog alert = builder.create();
        builder.show();
    }

    private void setCityData() {
        //   final CharSequence[] items = {"Noida","Greater Noida","Bareilly","Badaun","Patna","Gaziyabad","Ujhani"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ReceivereditActivity.this);
        builder.setTitle("Select City");
        //builder.setI
        builder.setItems(dname, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //Toast.makeText(getApplicationContext(), con.get(item).getCountrName(), Toast.LENGTH_SHORT).show();
                spCity.setText(dname[item]);
                //System.out.println("Item is: "+items[item]);
                /*CONTRY_ID = con.get(item).getCountryId();
                stateET.requestFocus();*/
            }
        });
        //AlertDialog alert = builder.create();
        builder.show();
    }
    private void setStateData() {
        //final String[] itemsarray = loadArray("sarray",getApplicationContext());
        final String[] items = loadArray("sarray",getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(ReceivereditActivity.this);
        builder.setTitle("Select State");
        //builder.setI
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //Toast.makeText(getApplicationContext(), con.get(item).getCountrName(), Toast.LENGTH_SHORT).show();
                spState.setText(items[item]);
                //System.out.println("Item is: "+items[item]);
                /*CONTRY_ID = con.get(item).getCountryId();
                stateET.requestFocus();*/
            }
        });
        //AlertDialog alert = builder.create();
        builder.show();
    }
    private void setGenData() {
        final CharSequence[] items = {"Mr.","Ms."};
        AlertDialog.Builder builder = new AlertDialog.Builder(ReceivereditActivity.this);
        builder.setTitle("Select Gender");
        //builder.setI
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //Toast.makeText(getApplicationContext(), con.get(item).getCountrName(), Toast.LENGTH_SHORT).show();
                spGen.setText(items[item]);
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

    private void getImageFrom() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ReceivereditActivity.this);
        builder.setTitle("Select Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                } else if (items[item].equals("Choose from Library")) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //Toast.makeText(ReceivereditActivity.this,"yess", Toast.LENGTH_LONG).show();
                        // final Uri imageUri = imageReturnedIntent.getData();
                       /* final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                       // options.inSampleSize = 2;
                        options.inPreferredConfig = Bitmap.Config.RGB_565;

                        selectedImage = BitmapFactory.decodeStream(imageStream);

                        img_main.setImageBitmap(selectedImage);*/
                        String p1=imageReturnedIntent.getData().toString();
                        p=compressImage(p1);
                        /*SharedPreferences s1= getSharedPreferences(STORAGE_IMAGE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = s1.edit();
                        editor.putString("imagepath",p);
                        editor.apply();*/

                        // filee=p;
                        // Uri uri = Uri.parse(p);
                        selectedImage = BitmapFactory.decodeFile(p);
                        imgProfile.setImageBitmap(selectedImage);
                        SharedPreferences s2= getSharedPreferences(STORAGE_IMAGE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = s2.edit();
                        editor.putString("imagePreferance", encodeTobase64(selectedImage));
                        editor.apply();
                        editor.commit();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                   // Toast.makeText(ReceivereditActivity.this,"yess", Toast.LENGTH_LONG).show();
                    Bundle extras = imageReturnedIntent.getExtras();
                    selectedImage = (Bitmap) extras.get("data");
                    Uri pp=getImageUri(getApplicationContext(),selectedImage);
                    String p1=pp.toString();
                    p = compressImage(p1);

                    selectedImage = BitmapFactory.decodeFile(p);
                    imgProfile.setImageBitmap(selectedImage);
                    SharedPreferences s2= getSharedPreferences(STORAGE_IMAGE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = s2.edit();
                    editor.putString("imagePreferance", encodeTobase64(selectedImage));
                    editor.apply();
                    editor.commit();


                }
        }

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {               imgRatio = maxHeight / actualHeight;                actualWidth = (int) (imgRatio * actualWidth);               actualHeight = (int) maxHeight;             } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }
    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }       final float totalPixels = width * height;       final float totalReqPixelsCap = reqWidth * reqHeight * 2;       while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, WelcomeActivity.class);
        startActivity(i);
        finish();
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
                ReceivereditActivity.this);

        alertDialog.setTitle("Enable Location Settings");

        alertDialog
                .setMessage("Please enable location to allow users to locate you for platelates donation/searches.");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                      ReceivereditActivity.this.startActivity(intent);
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
