package com.bluecepheid.admin.vdonor;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;

import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    EditText verify;
    Button vbutton;
    String area;
    String state,state1,city1,locality1;
    BroadcastReceiver sendBroadcastReceiver;
    BroadcastReceiver deliveryBroadcastReceiver;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    String flag,phoneNo;
    String fbid;
    List<String> list = new ArrayList<String>();
    ArrayList<String> list1 = new ArrayList<String>();
    CircleProgressBar progressBar;
    ImageView v1;
    SqliteHelper1 sqliteHelper1;
    SqliteHelper2 sqliteHelper2;
    SqliteHelper3 sqliteHelper3;
    SqliteHelper4 sqliteHelper4;
    SqliteHelper5 sqliteHelper5;
    String name,lname,fname,str_id,phone,add,locality,city,userid,gen,email,DOB,group,stateName,type,stateId,bloodId,bloodName;
    int mflag,eflag;
    public static final String STORAGE_DATA = "Userdata";
    TextView stat;
    Button logfb;

    CallbackManager callbackManager;
    private LoginManager manager;
    public static final String STORAGE_USERTYPE = "usertype";
    public static final String STORAGE_NAME = "VERIFICATION";
    public static final String STORAGE_FB = "FB";
    String []sname,sid;
    String []bname,bid;
    String mess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        sqliteHelper1 = new SqliteHelper1(this);
        sqliteHelper2 = new SqliteHelper2(this);
        sqliteHelper3 = new SqliteHelper3(this);
        sqliteHelper4 = new SqliteHelper4(this);
        sqliteHelper5 = new SqliteHelper5(this);
        verify=(EditText)findViewById(R.id.verify);
        mess= getApplicationContext().getResources().getString(R.string.base_url);
        stat=(TextView)findViewById(R.id.stat);
        verify.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
        vbutton=(Button)findViewById(R.id.vbutton);
        v1=(ImageView)findViewById(R.id.v);
        logfb=(Button)findViewById(R.id.logfb);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SharedPreferences sharedPreferences = getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        flag = sharedPreferences.getString("flag", "");
        phoneNo = sharedPreferences.getString("phoneNo", "");
        callbackManager = CallbackManager.Factory.create();
        final List<String> permissionNeeds = Arrays.asList("publish_actions");
       //  final List<String> permissionNeeds1 = Arrays.asList("public_profile", "user_friends", "email");


        // getUserInfo();



      logfb.setOnClickListener(new View.OnClickListener() {
                                       public void onClick(View v) {


                                               if (isNetworkAvailable()) {
                                                   SharedPreferences sharedPreferences = getSharedPreferences(STORAGE_FB, Context.MODE_PRIVATE);

                                                   fbid = sharedPreferences.getString("str_id", "");
                                                   if(!fbid.equals(""))
                                                   {
                                                       Intent i=new Intent(MainActivity.this,HomeActivity.class);
                                                       startActivity(i);
                                                       finish();
                                                   }

                                                   manager = LoginManager.getInstance();
                                               // manager.setLoginBehavior(LoginBehavior.SSO_WITH_FALLBACK);
                                                   manager.logInWithPublishPermissions(MainActivity.this, permissionNeeds);
                                                   manager.registerCallback(callbackManager,
                                                       new FacebookCallback<LoginResult>() {
                                                           @Override
                                                           public void onSuccess(LoginResult loginResult) {
                                                               Log.d("access", "" + loginResult.getAccessToken());
                                                               // App code
                                                               // Toast.makeText(getApplicationContext(), "yesss", Toast.LENGTH_SHORT).show();
                                                               System.out.println("Success");

                                                               //  loginbutton.setVisibility(View.INVISIBLE);
                                                               GraphRequest.newMeRequest(
                                                                       loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                                                           @Override
                                                                           public void onCompleted(JSONObject json, GraphResponse response) {
                                                                               if (response.getError() != null) {
                                                                                   // handle error
                                                                                   System.out.println("ERROR");
                                                                               } else {
                                                                                   System.out.println("Success");
                                                                                   try {

                                                                                       String jsonresult = String.valueOf(json);
                                                                                       System.out.println("JSON Result" + jsonresult);

                                                                                       // String str_email = json.getString("email");
                                                                                       str_id = json.getString("id");
                                                                                       fname = json.getString("name");

                                                                                   } catch (JSONException e) {
                                                                                       e.printStackTrace();
                                                                                   }
                                                                                   //  String str_lastname = json.getString("last_name");
                                                                                   //   String link = json.getString("link");
                                                                                   //   String gender = json.getString("gender");
                                                                                   //Toast.makeText(getApplicationContext(), str_email, Toast.LENGTH_SHORT).show();
                                                                                   //textView1.setText(str_email);
                                                                                   //textView2.setText(str_id);
                                                                                   //textView3.setText(str_firstname);
                                                                                   //textView4.setText(link);
                                                                                   //button.setEnabled(true);
                                                                                   //verify.setText(str_email+""+name);
                                                                                   new Send1().execute();

                                                                                   //textView5.setText(fields);

                                                                               }
                                                                           }

                                                                       }).executeAsync();

                                                           }

                                                           //Intent i = new Intent(MainActivity.this, New.class);
                                                           // startActivity(i);
                                                           //        AccessToken accessToken = loginResult.getAccessToken();
                                                           //       Profile profile = Profile.getCurrentProfile();
                                                           //     displayMessage(profile);


                                                           @Override
                                                           public void onCancel() {
                                                               // App code
                                                               Log.d("fb cancel","cancelled");
                                                           }

                                                           @Override
                                                           public void onError(FacebookException exception) {
                                                               // App code
                                                               Log.d("fb error",exception.toString());
                                                           }
                                                       });
                                           } else {
                                               Toast.makeText(getApplicationContext(),
                                                          "Please connect to Internet",
                                                          Toast.LENGTH_LONG).show();
                                           }
                                       }});
                verify.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if (s.length() == 10) {
                            vbutton.setEnabled(true);
                            vbutton.setBackgroundResource(R.mipmap.send_enable);
                        }

                    }

                });
        vbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                phoneNo = verify.getText().toString();
            /*SharedPreferences sharedPreferences1 = getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                // editor.putString("message",messageReceived);

                editor1.putString("phoneNo", phoneNo);
                editor1.apply();
                editor1.commit();
                if(isNetworkAvailable()) {
                    new Send().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                             "please connect to internet",
                              Toast.LENGTH_LONG).show();
                }
*/
           if (v1.getTag() == null) {
                    // Toast.makeText(getApplicationContext(),
                    //  "" + v1.getTag(),
                    //   Toast.LENGTH_LONG).show();
                    String message = "Verification done successfully";
                    // String phoneNo = txtPhoneNo.getText().toString();
                    // String message = txtMessage.getText().toString();
                    SharedPreferences sharedPreferences1 = getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    // editor.putString("message",messageReceived);
                    editor1.putString("flag", "");
                    editor1.putString("phoneNo", phoneNo);
                    editor1.apply();
                    editor1.commit();
                    String regexStr = "[0-9]+";
                    if ((phoneNo.matches(regexStr)) && (phoneNo.length() == 10))
                        //  if(phoneNo.length()==10) {
                        sendSMS(phoneNo,message);
                        //   }

                    else
                        Toast.makeText(getBaseContext(),
                                "Please enter 10 digit valid phone number for verification",
                                Toast.LENGTH_LONG).show();


                } else if ((int) v1.getTag() == R.mipmap.success) {
                    Toast.makeText(getBaseContext(),
                            "Your Number is verified Successfully",
                            Toast.LENGTH_LONG).show();

                            if(isNetworkAvailable())
                            {
                            new Send().execute();
                            }
                             else
                {
                    Toast.makeText(getApplicationContext(),
                             "please connect to internet",
                              Toast.LENGTH_LONG).show();
                }
                } else if ((int) v1.getTag() == R.drawable.no_converted) {
                    Toast.makeText(getBaseContext(),
                            "Your Number is not verified.Please try again",
                            Toast.LENGTH_LONG).show();
                    v1.setTag(null);
                    v1.setVisibility(View.INVISIBLE);
                    stat.setVisibility(View.INVISIBLE);
                }


            }
        });


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

            vbutton.setBackgroundResource(R.mipmap.send_disable);

        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag


            String REGISTER_URL = mess+"/rest/user/getDetail/contactNo/"+phoneNo;

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
                    try {
                        statearray=j.getJSONArray("stateList");

                        for(int i=0;i<statearray.length();i++)
                        {
                            JSONObject stateobj=statearray.getJSONObject(i);
                            list.add(stateobj.getString("stateName"));
                            list1.add(stateobj.getString("stateId"));
                            stateId=stateobj.getString("stateId");
                            stateName=stateobj.getString("stateName");
                            Boolean p=sqliteHelper2.saveUser(stateName,stateId);
                        }
                        sname = list.toArray(new String[list.size()]);
                        sid = list1.toArray(new String[list1.size()]);
                        Boolean p=saveArray(sname,"sarray",getApplicationContext());
                        data = j.getJSONObject("userDetail");

                        name = data.getString("firstName");
                        lname = data.getString("lastName");
                        phone = data.getString("contactNo");
                        add = data.getString("address1");
                        locality = data.getString("area");
                        city = data.getString("district");
                        userid=data.getString("userId");
                        state=data.getString("state");
                        if(data.has("gender")) {
                            gen = data.getString("gender");


                                if (gen.equals("M")) {
                                    gen = "Mr.";
                                } else {
                                    gen = "Ms.";
                                }

                        }
                        else
                        {
                            gen="Mr";
                        }
                        if(data.has("email")) {
                            email = data.getString("email");
                        }

                        if(data.has("dateOfBirth")) {
                            DOB = data.getString("dateOfBirth");
                        }
                        group = data.getString("bloodGroupId");
                        if(data.has("contactNoDispFlag")) {
                            mflag = data.getInt("contactNoDispFlag");
                        }
                        if(data.has("emailDispFlag")) {
                            eflag = data.getInt("emailDispFlag");
                        }
                        type=data.getString("userType");
                        Log.d("server start",DOB+group);

                        list.clear();

                        list1.clear();

                        bloodarray=j.getJSONArray("bloodGroupList");

                        for(int i=0;i<bloodarray.length();i++)
                        {
                            JSONObject bloodobj=bloodarray.getJSONObject(i);
                            list.add(bloodobj.getString("bloodGroupName"));
                            list1.add(bloodobj.getString("bloodGroupId"));
                            bloodId=bloodobj.getString("bloodGroupId");
                            bloodName=bloodobj.getString("bloodGroupName");
                            Boolean p1=sqliteHelper3.saveUser(bloodName,bloodId);
                            Log.d("Blood array",""+p1);
                        }
                        bname = list.toArray(new String[list.size()]);
                        bid = list1.toArray(new String[list1.size()]);
                        Boolean p1=saveArray(bname,"barray",getApplicationContext());
                        //Boolean p1=saveArray(sid,"sarrayid",getApplicationContext());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String REGISTER_URL1 = mess+"/rest/common/getDistricts/stateId/"+state;

                    GetJsonObject json1=new GetJsonObject();
                    String response1 =json1.getWebServceObj(REGISTER_URL1);
                    try {
                        JSONObject jsonResponse = new JSONObject(response1);
                        if(jsonResponse.getString("statusMessage").equals("success")){
                            JSONArray jsonDistArr = jsonResponse.getJSONArray("districtList");
                            int[] districIdArr = new int[jsonDistArr.length()];
                            for(int i=0;i<jsonDistArr.length();i++){
                                JSONObject jsonDisObj = jsonDistArr.getJSONObject(i);
                                String areaList = jsonDisObj.getJSONArray("areaList").toString();
                                String districtName = jsonDisObj.getString("districtName");
                                String districId=jsonDisObj.getString("districtId");
                                Boolean d=sqliteHelper4.saveUser(districtName,districId);
                                Log.d("distric saved",""+d);
                                Log.d("areaa",areaList);
                                // list.add(jsonDisObj.getString("districtName"));
                                Boolean p11=sqliteHelper1.saveUser(districtName,areaList);
                            }
                            //dname = list.toArray(new String[list.size()]);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Cursor cursor1 = sqliteHelper2.getUser1(state);
                    if (cursor1.getCount() != 0) {
                        cursor1.moveToFirst();
                        state = cursor1.getString(cursor1.getColumnIndex("id"));
                    }
                    Cursor cursor2 = sqliteHelper4.getUser1(city);
                    if (cursor2.getCount() != 0) {
                        cursor2.moveToFirst();
                        city = cursor2.getString(cursor2.getColumnIndex("id"));
                    }
                    Cursor cursor = sqliteHelper1.getUser(city);
                    if (cursor.getCount() != 0) {
                        cursor.moveToFirst();
                        area = cursor.getString(cursor.getColumnIndex("area"));
                    }
                    try {
                        JSONArray jsonAreaArr = new JSONArray(area);
                        String []areaArr = new String[jsonAreaArr.length()];
                        for(int jj=0;jj<jsonAreaArr.length();jj++){
                            JSONObject jsonAreaObj = jsonAreaArr.getJSONObject(jj);
                            String areaId = jsonAreaObj.getString("areaId");
                            String areaName = jsonAreaObj.getString("areaName");
                            //  areaArr[jj]=areaName;
                            Boolean d=sqliteHelper5.saveUser(areaName,areaId);
                            Log.d("area saved",""+d);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Cursor cursor3 = sqliteHelper5.getUser1(locality);
                    if (cursor3.getCount() != 0) {
                        cursor3.moveToFirst();
                        locality = cursor3.getString(cursor3.getColumnIndex("id"));
                    }
                    Log.d("server se data",state+city+locality);

                    if(group.equals("1"))
                    {
                        group="A+";
                    }
                    else if(group.equals("2"))
                    {
                        group="A-";
                    }
                    else if(group.equals("3"))
                    {
                        group="B+";
                    }
                    else if(group.equals("4"))
                    {
                        group="B-";
                    }
                    else if(group.equals("5"))
                    {
                        group="AB+";
                    }
                    else if(group.equals("6"))
                    {
                        group="AB-";
                    }
                    else if(group.equals("7"))
                    {
                        group="O+";
                    }
                    else if(group.equals("8"))
                    {
                        group="O-";
                    }
                    SharedPreferences s1= getSharedPreferences(STORAGE_DATA, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = s1.edit();
                    //   editor.clear();

                    editor.putString("name", name);
                    editor.putString("lname", lname);
                    editor.putString("phone", phone);
                    editor.putString("add", add);
                    editor.putString("locality", locality);
                    editor.putString("city", city);
                    editor.putString("gender", gen);
                    editor.putString("userid",userid);
                    editor.putString("state", state);
                    editor.putString("email", email);
                    editor.putString("date", DOB);
                    editor.putString("group", group);
                    editor.putString("group", group);
                    editor.putInt("mflag", mflag);
                    editor.putInt("eflag", eflag);
                    // editor.putString("lastdonated", lastdonated);

                    editor.apply();
                    editor.commit();


                    if(type.equals("D")) {
                        SharedPreferences s11= getSharedPreferences(STORAGE_USERTYPE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = s11.edit();
                        editor1.putString("type", "donor");
                        editor1.apply();
                        editor1.commit();
                    }
                    else
                    {
                        SharedPreferences s11= getSharedPreferences(STORAGE_USERTYPE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = s11.edit();
                        editor1.putString("type", "rcvr");
                        editor1.apply();
                        editor1.commit();

                    }

                    Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
                    // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
                else {
                    statearray=j.getJSONArray("stateList");

                    for(int i=0;i<statearray.length();i++)
                    {  JSONObject stateobj=statearray.getJSONObject(i);
                        list.add(stateobj.getString("stateName"));
                        list1.add(stateobj.getString("stateId"));
                        stateId=stateobj.getString("stateId");
                        stateName=stateobj.getString("stateName");
                        Boolean p=sqliteHelper2.saveUser(stateName,stateId);
                        Log.d("sqlite",stateId+stateName);
                        Log.d("boolean",""+p);
                        //  Toast.makeText(getApplicationContext(),
                        //     ""+p,
                        //     Toast.LENGTH_LONG).show();
                    }
                    sname = list.toArray(new String[list.size()]);
                    sid = list1.toArray(new String[list1.size()]);
                    Boolean p=saveArray(sname, "sarray", getApplicationContext());
                    Boolean p1=saveArray(sid,"sarrayid",getApplicationContext());
                    list.clear();
                    list1.clear();
                    bloodarray=j.getJSONArray("bloodGroupList");

                    for(int i=0;i<bloodarray.length();i++)
                    {
                        JSONObject bloodobj=bloodarray.getJSONObject(i);
                        list.add(bloodobj.getString("bloodGroupName"));
                        list1.add(bloodobj.getString("bloodGroupId"));
                        bloodId=bloodobj.getString("bloodGroupId");
                        bloodName=bloodobj.getString("bloodGroupName");
                        Boolean p11=sqliteHelper3.saveUser(bloodName,bloodId);
                        Log.d("Blood array",""+p1);
                    }
                    bname = list.toArray(new String[list.size()]);
                    bid = list1.toArray(new String[list1.size()]);
                    Boolean p11=saveArray(bname,"barray",getApplicationContext());
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
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

            vbutton.setBackgroundResource(R.mipmap.send_enable);


        }

    }
    class Send1 extends AsyncTask<String, String, String> {

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


              String REGISTER_URL = mess+"/rest/user/getDetail/contactNo/1234";

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
                    try {
                        statearray=j.getJSONArray("stateList");

                        for(int i=0;i<statearray.length();i++)
                        {
                            JSONObject stateobj=statearray.getJSONObject(i);
                            list.add(stateobj.getString("stateName"));
                            list1.add(stateobj.getString("stateId"));
                            stateId=stateobj.getString("stateId");
                            stateName=stateobj.getString("stateName");
                            Boolean p=sqliteHelper2.saveUser(stateName,stateId);
                        }
                        sname = list.toArray(new String[list.size()]);
                        sid = list1.toArray(new String[list1.size()]);
                        Boolean p=saveArray(sname,"sarray",getApplicationContext());
                        data = j.getJSONObject("userDetail");

                        name = data.getString("firstName");
                        lname = data.getString("lastName");
                        phone = data.getString("contactNo");
                        add = data.getString("address1");
                        locality = data.getString("area");
                        city = data.getString("district");
                        state=data.getString("state");
                        if(data.has("gender")) {
                            gen = data.getString("gender");
                            if (gen.equals("M")) {
                                gen = "Mr.";
                            } else {
                                gen = "Ms.";
                            }

                        }
                        else
                        {
                            gen="Mr.";
                        }
                        if(data.has("email")) {
                            email = data.getString("email");
                        }

if(data.has("dateOfBirth")) {
    DOB = data.getString("dateOfBirth");
}
                        group = data.getString("bloodGroupId");
                        if(data.has("contactNoDispFlag")) {
                            mflag = data.getInt("contactNoDispFlag");
                        }
                        if(data.has("emailDispFlag")) {
                            eflag = data.getInt("emailDispFlag");
                        }
                        type=data.getString("userType");
                        Log.d("server start",DOB+group);

                        list.clear();

                        list1.clear();

                        bloodarray=j.getJSONArray("bloodGroupList");

                        for(int i=0;i<bloodarray.length();i++)
                        {
                            JSONObject bloodobj=bloodarray.getJSONObject(i);
                            list.add(bloodobj.getString("bloodGroupName"));
                            list1.add(bloodobj.getString("bloodGroupId"));
                            bloodId=bloodobj.getString("bloodGroupId");
                            bloodName=bloodobj.getString("bloodGroupName");
                            Boolean p1=sqliteHelper3.saveUser(bloodName,bloodId);
                            Log.d("Blood array",""+p1);
                        }
                        bname = list.toArray(new String[list.size()]);
                        bid = list1.toArray(new String[list1.size()]);
                        Boolean p1=saveArray(bname,"barray",getApplicationContext());
                        //Boolean p1=saveArray(sid,"sarrayid",getApplicationContext());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                        String REGISTER_URL1 = mess+"/rest/common/getDistricts/stateId/"+state;

                        GetJsonObject json1=new GetJsonObject();
                        String response1 =json1.getWebServceObj(REGISTER_URL1);
                        try {
                            JSONObject jsonResponse = new JSONObject(response1);
                            if(jsonResponse.getString("statusMessage").equals("success")){
                                JSONArray jsonDistArr = jsonResponse.getJSONArray("districtList");
                               int[] districIdArr = new int[jsonDistArr.length()];
                                for(int i=0;i<jsonDistArr.length();i++){
                                    JSONObject jsonDisObj = jsonDistArr.getJSONObject(i);
                                    String areaList = jsonDisObj.getJSONArray("areaList").toString();
                                    String districtName = jsonDisObj.getString("districtName");
                                    String districId=jsonDisObj.getString("districtId");
                                    Boolean d=sqliteHelper4.saveUser(districtName,districId);
                                    Log.d("distric saved",""+d);
                                    Log.d("areaa",areaList);
                                   // list.add(jsonDisObj.getString("districtName"));
                                    Boolean p11=sqliteHelper1.saveUser(districtName,areaList);
                                }
                                //dname = list.toArray(new String[list.size()]);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Cursor cursor1 = sqliteHelper2.getUser1(state);
                        if (cursor1.getCount() != 0) {
                            cursor1.moveToFirst();
                            state = cursor1.getString(cursor1.getColumnIndex("id"));
                        }
                        Cursor cursor2 = sqliteHelper4.getUser1(city);
                        if (cursor2.getCount() != 0) {
                            cursor2.moveToFirst();
                            city = cursor2.getString(cursor2.getColumnIndex("id"));
                        }
                    Cursor cursor = sqliteHelper1.getUser(city);
                    if (cursor.getCount() != 0) {
                        cursor.moveToFirst();
                        area = cursor.getString(cursor.getColumnIndex("area"));
                    }
                    try {
                        JSONArray jsonAreaArr = new JSONArray(area);
                        String []areaArr = new String[jsonAreaArr.length()];
                        for(int jj=0;jj<jsonAreaArr.length();jj++){
                            JSONObject jsonAreaObj = jsonAreaArr.getJSONObject(jj);
                            String areaId = jsonAreaObj.getString("areaId");
                            String areaName = jsonAreaObj.getString("areaName");
                          //  areaArr[jj]=areaName;
                            Boolean d=sqliteHelper5.saveUser(areaName,areaId);
                            Log.d("area saved",""+d);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Cursor cursor3 = sqliteHelper5.getUser1(locality);
                        if (cursor3.getCount() != 0) {
                            cursor3.moveToFirst();
                            locality = cursor3.getString(cursor3.getColumnIndex("id"));
                        }
                        Log.d("server se data",state+city+locality);


                   /* if(gen.equals("M"))
                    {
                        gen="Mr." ;
                    }
                    else
                    {
                        gen="Ms.";
                    }*/
                    if(group.equals("1"))
                    {
                        group="A+";
                    }
                    else if(group.equals("2"))
                    {
                        group="A-";
                    }
                    else if(group.equals("3"))
                    {
                        group="B+";
                    }
                    else if(group.equals("4"))
                    {
                        group="B-";
                    }
                    else if(group.equals("5"))
                    {
                        group="AB+";
                    }
                    else if(group.equals("6"))
                    {
                        group="AB-";
                    }
                    else if(group.equals("7"))
                    {
                        group="O+";
                    }
                    else if(group.equals("8"))
                    {
                        group="O-";
                    }



                }
                else {
                    statearray=j.getJSONArray("stateList");

                    for(int i=0;i<statearray.length();i++)
                    {  JSONObject stateobj=statearray.getJSONObject(i);
                        list.add(stateobj.getString("stateName"));
                        list1.add(stateobj.getString("stateId"));
                        stateId=stateobj.getString("stateId");
                        stateName=stateobj.getString("stateName");
                        Boolean p=sqliteHelper2.saveUser(stateName,stateId);
                        Log.d("sqlite",stateId+stateName);
                        Log.d("boolean",""+p);
                        //  Toast.makeText(getApplicationContext(),
                        //     ""+p,
                        //     Toast.LENGTH_LONG).show();
                    }
                    sname = list.toArray(new String[list.size()]);
                    sid = list1.toArray(new String[list1.size()]);
                    Boolean p=saveArray(sname,"sarray",getApplicationContext());
                    Boolean p1=saveArray(sid,"sarrayid",getApplicationContext());
                    list.clear();
                    list1.clear();
                    bloodarray=j.getJSONArray("bloodGroupList");

                    for(int i=0;i<bloodarray.length();i++)
                    {
                        JSONObject bloodobj=bloodarray.getJSONObject(i);
                        list.add(bloodobj.getString("bloodGroupName"));
                        list1.add(bloodobj.getString("bloodGroupId"));
                        bloodId=bloodobj.getString("bloodGroupId");
                        bloodName=bloodobj.getString("bloodGroupName");
                        Boolean p11=sqliteHelper3.saveUser(bloodName,bloodId);
                        Log.d("Blood array",""+p1);
                    }
                    bname = list.toArray(new String[list.size()]);
                    bid = list1.toArray(new String[list1.size()]);
                    Boolean p11=saveArray(bname,"barray",getApplicationContext());
                    SharedPreferences sharedPreferences = getSharedPreferences(STORAGE_FB, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    // editor.putString("str_email", str_email);
                    editor.putString("str_id", str_id);
                    editor.putString("fname", fname);

                    // editor.putString("str_lastname", str_lastname);
                    //  editor.putString("link", link);
                    //   editor.putString("gender", gender);
                    // editor.putBoolean("hasLoggedIn", true);
                    editor.apply();
                    editor.commit();
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
    private void sendSMS(String phoneNumber, String message)
    {

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---


        //---when the SMS has been delivered---
        //   registerReceiver(new BroadcastReceiver(){
        //     @Override
        //   public void onReceive(Context arg0, Intent arg1) {
        //     switch (getResultCode())
        //   {
        //     case Activity.RESULT_OK:
        //       Toast.makeText(getBaseContext(), "SMS delivered",
        //             Toast.LENGTH_SHORT).show();
        //    break;
        //case Activity.RESULT_CANCELED:
        //  Toast.makeText(getBaseContext(), "SMS not delivered",
        //        Toast.LENGTH_SHORT).show();
        // break;
        // }
        //  }
        // }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        progressBar = (CircleProgressBar) findViewById(R.id.pBar);
        progressBar.setColorSchemeResources(R.color.red1);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        vbutton.setBackgroundResource(R.mipmap.send_disable);
        verify.setFocusable(false);
        vbutton.setFocusable(false);
        vbutton.setEnabled(false);
        stat.setVisibility(View.VISIBLE);
        stat.setText("Please Wait...\n While your number is being verified");
        stat.setTypeface(Typeface.DEFAULT_BOLD);
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
                vbutton.setEnabled(true);
                vbutton.setFocusableInTouchMode(true);
                vbutton.setBackgroundResource(R.mipmap.send_enable);
                SharedPreferences sharedPreferences = getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
                // RelativeLayout rl=(RelativeLayout)findViewById(R.id.done);
                //  phoneNo = sharedPreferences.getString("phoneNo", "");
                String flag = sharedPreferences.getString("flag", "");
                if(flag.equals("yes")) {
                    v1.setVisibility(View.VISIBLE);
                    v1.setImageResource(R.mipmap.success);
                    v1.setTag(R.mipmap.success);
                    stat.setText("Thank you! \n Your number is verified");
                    stat.setTypeface(Typeface.DEFAULT_BOLD);
                    stat.setTextColor(Color.parseColor("#ff1f7707"));
                }
                else
                {
                    v1.setVisibility(View.VISIBLE);
                    v1.setImageResource(R.drawable.no_converted);
                    v1.setTag(R.drawable.no_converted);
                    stat.setText("Sorry! \n Please try with other number");
                    verify.setFocusableInTouchMode(true);
                    stat.setTextColor(Color.parseColor("#ffd61d23"));
                    stat.setTypeface(Typeface.DEFAULT_BOLD);
                }

            }
        }, 20000);





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


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
