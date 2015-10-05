package com.bluecepheid.admin.vdonor;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class Shareskip extends Activity implements LocationListener {
    Button fbshare;
    TextView skip;
    public static final String STORAGE_NAME = "VERIFICATION";
    ShareDialog shareDialog;
    String imagePreferance,area1,lat,longg;
    String name1,group1,phoneNo;
    public static final String VERIFY = "verify";
    public static final String STORAGE_IMAGE = "Image";
    public static final String STORAGE_DATA = "Userdata";
    ListView dlist;
    int flag;
ImageView image;
    DonorAdapter1 adapter1;
    SqliteHelper1 sqliteHelper1;
    SqliteHelper2 sqliteHelper2;
    SqliteHelper3 sqliteHelper3;
    SqliteHelper4 sqliteHelper4;
    SqliteHelper5 sqliteHelper5;
    CircleProgressBar progressBar;
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
    LocationManager locationManager;
    Location location;
    String mess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareskip);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if ( !locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER ) ) {

            showSettingsAlert("GPS");

        }
        mess= getApplicationContext().getResources().getString(R.string.base_url);
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
                  //  lat1 = location.getLatitude();
                  //  long1 = location.getLongitude();

                    // latt.setText("Lattitude:"+lat1);
                    // longg.setText("Longitude:"+longg);
                }
            }
        }
        else
        {
           // lat1=28.6100;
           // long1=77.2300;
        }
        SharedPreferences sharedPreferences0= getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);

        phoneNo = sharedPreferences0.getString("phoneNo", "");
        sqliteHelper2 = new SqliteHelper2(this);
        sqliteHelper1 = new SqliteHelper1(this);
        sqliteHelper3 = new SqliteHelper3(this);
        sqliteHelper4 = new SqliteHelper4(this);
        sqliteHelper5 = new SqliteHelper5(this);
        fbshare=(Button)findViewById(R.id.fbshare);
        shareDialog = new ShareDialog(this);
        skip=(TextView)findViewById(R.id.skip);
        dlist=(ListView)findViewById(R.id.dlist);
        image=(ImageView)findViewById(R.id.image);
        SharedPreferences s1= getSharedPreferences(STORAGE_DATA, Context.MODE_PRIVATE);
        lat = s1.getString("lat", "");
        longg = s1.getString("long", "");
        group1 = s1.getString("group", "");
        name1 = s1.getString("name", "");
        if (isNetworkAvailable()) {
            //publishImage();

            SharedPreferences sharedPreferences = getSharedPreferences(STORAGE_IMAGE, Context.MODE_PRIVATE);
            imagePreferance = sharedPreferences.getString("imagePreferance", "");
            // Toast.makeText(Shareskip.this,"image"+ imagePreferance, Toast.LENGTH_SHORT).show();
            if (!imagePreferance.equals("")) {
                Bitmap p = decodeBase64(imagePreferance);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                p.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();
                RelativeLayout viewToBeShared = (RelativeLayout) LayoutInflater.from(Shareskip.this).inflate(R.layout.layout_image, null);

                TextView tvName = (TextView) viewToBeShared.findViewById(R.id.tvName);
                tvName.setText(name1.toUpperCase());

                TextView tvBloodGroup = (TextView) viewToBeShared.findViewById(R.id.tvBloodGroupValue);
                tvBloodGroup.setText(group1);

                ImageView imgUser = (ImageView) viewToBeShared.findViewById(R.id.imgUser);
                ByteArrayInputStream imageStream = new ByteArrayInputStream(bitmapdata);
                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                imgUser.setImageBitmap(theImage);

                // construct Final IMAGE to be shared.
                viewToBeShared.setDrawingCacheEnabled(true);
                // this is the important code :)
// Without it the view will have a dimension of 0,0 and the bitmap will be null
                viewToBeShared.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                viewToBeShared.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                viewToBeShared.layout(0, 0, viewToBeShared.getMeasuredWidth(), viewToBeShared.getMeasuredHeight());
                viewToBeShared.buildDrawingCache();
                Bitmap bm = viewToBeShared.getDrawingCache();
                // Bitmap image = Bitmap.createBitmap(bm);
                Bitmap bitmap = Bitmap.createBitmap(bm);
                image.setImageBitmap(bitmap);


            } else {


                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.no_photo);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();
                RelativeLayout viewToBeShared = (RelativeLayout) LayoutInflater.from(Shareskip.this).inflate(R.layout.layout_image, null);

                TextView tvName = (TextView) viewToBeShared.findViewById(R.id.tvName);
                tvName.setText(name1.toUpperCase());

                TextView tvBloodGroup = (TextView) viewToBeShared.findViewById(R.id.tvBloodGroupValue);
                tvBloodGroup.setText(group1);

                ImageView imgUser = (ImageView) viewToBeShared.findViewById(R.id.imgUser);
                ByteArrayInputStream imageStream = new ByteArrayInputStream(bitmapdata);
                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                imgUser.setImageBitmap(theImage);

                // construct Final IMAGE to be shared.
                viewToBeShared.setDrawingCacheEnabled(true);
                // this is the important code :)
// Without it the view will have a dimension of 0,0 and the bitmap will be null
                viewToBeShared.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                viewToBeShared.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                viewToBeShared.layout(0, 0, viewToBeShared.getMeasuredWidth(), viewToBeShared.getMeasuredHeight());
                viewToBeShared.buildDrawingCache();
                Bitmap bm = viewToBeShared.getDrawingCache();
                // Bitmap image = Bitmap.createBitmap(bm);
                Bitmap bitmap1 = Bitmap.createBitmap(bm);
                image.setImageBitmap(bitmap1);

                //Toast.makeText(Shareskip.this, "No image found.", Toast.LENGTH_SHORT).show();
            }
        }


        else

        {
            Toast.makeText(getApplicationContext(),
                    "Please connect to Internet",
                    Toast.LENGTH_LONG).show();
        }
        if(isNetworkAvailable()) {
            new Loadlist1().execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Please connect to Internet",
                    Toast.LENGTH_LONG).show();
        }

        skip.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences s1= getSharedPreferences(VERIFY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = s1.edit();
                //   editor.putString("message", messageReceived);
                editor.putBoolean("verified",true);
                editor.commit();
                //g.setFlag(2);
                Intent i = new Intent(Shareskip.this,WelcomeActivity.class);
                startActivity(i);
                finish();
            //Toast.makeText(Shareskip.this,""+g.getFlag(), Toast.LENGTH_SHORT).show();
            }
        });

        fbshare.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    //publishImage();
                    SharedPreferences s1 = getSharedPreferences(STORAGE_DATA, Context.MODE_PRIVATE);
                    name1 = s1.getString("name", "");

                    group1 = s1.getString("group", "");
                    SharedPreferences sharedPreferences = getSharedPreferences(STORAGE_IMAGE, Context.MODE_PRIVATE);
                    imagePreferance = sharedPreferences.getString("imagePreferance", "");
                    // Toast.makeText(Shareskip.this,"image"+ imagePreferance, Toast.LENGTH_SHORT).show();
                    if (!imagePreferance.equals("")) {
                        Bitmap p = decodeBase64(imagePreferance);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        p.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] bitmapdata = stream.toByteArray();
                        shareImageOnFacebook(name1, group1, bitmapdata);

                        //Toast.makeText(Shareskip.this, "Yesss image found.", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences s11 = getSharedPreferences(STORAGE_DATA, Context.MODE_PRIVATE);
                        name1 = s11.getString("name", "");

                        group1 = s11.getString("group", "");

                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.no_photo);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] bitmapdata = stream.toByteArray();


                        shareImageOnFacebook(name1, group1, bitmapdata);
                        //Toast.makeText(Shareskip.this, "No image found.", Toast.LENGTH_SHORT).show();
                    }
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
    public void shareImageOnFacebook(String name, String bloodGroup, byte[] imageInBytes) {
        RelativeLayout viewToBeShared = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.layout_image, null);

        TextView tvName = (TextView) viewToBeShared.findViewById(R.id.tvName);
        tvName.setText(name.toUpperCase());

        TextView tvBloodGroup = (TextView) viewToBeShared.findViewById(R.id.tvBloodGroupValue);
        tvBloodGroup.setText(bloodGroup);

        ImageView imgUser = (ImageView) viewToBeShared.findViewById(R.id.imgUser);
        ByteArrayInputStream imageStream = new ByteArrayInputStream(imageInBytes);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        imgUser.setImageBitmap(theImage);

        // construct Final IMAGE to be shared.
        viewToBeShared.setDrawingCacheEnabled(true);
        // this is the important code :)
// Without it the view will have a dimension of 0,0 and the bitmap will be null
        viewToBeShared.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        viewToBeShared.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        viewToBeShared.layout(0, 0, viewToBeShared.getMeasuredWidth(), viewToBeShared.getMeasuredHeight());
        viewToBeShared.buildDrawingCache();
        Bitmap bm = viewToBeShared.getDrawingCache();
        // Bitmap image = Bitmap.createBitmap(bm);
        Bitmap bitmap = Bitmap.createBitmap(bm);
        try {

            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(path, "" + System.currentTimeMillis() + ".png");
            FileOutputStream fileOutPutStream = new FileOutputStream(imageFile);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutPutStream);
            Uri imageUri = Uri.parse("file://" + imageFile.getAbsolutePath());
            Intent emailIntent = new Intent();
            emailIntent.setAction(Intent.ACTION_SEND);
            // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
            emailIntent.setType("image/png");
           emailIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            PackageManager pm = getPackageManager();
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("image/png");
            sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

            Intent openInChooser = Intent.createChooser(emailIntent, "Share via");

            List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
            List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
            for (int i = 0; i < resInfo.size(); i++) {
                // Extract the label, append it, and repackage it in a LabeledIntent
                ResolveInfo ri = resInfo.get(i);
                String packageName = ri.activityInfo.packageName;
                if(packageName.contains("android.email")) {
                    emailIntent.setPackage(packageName);
                } else if(packageName.contains("twitter") || packageName.contains("facebook")  || packageName.contains("whatsapp")) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("image/png");
                    if(packageName.contains("twitter")) {
                        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    } else if(packageName.contains("facebook")) {
                        // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
                        // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
                        // will show the <meta content ="..."> text from that page with our link in Facebook.
                        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    }  else if(packageName.contains("whatsapp"))
                    { // If Gmail shows up twice, try removing this else-if clause and the reference to "android.gm" above
                        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    }

                    intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
                }
            }

            // convert intentList to array
            LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

            openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
            startActivity(openInChooser);
            // share image on fb

            // shareOnFacebook(imageUri);
          /*  if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("Hello Facebook")
                        .setContentDescription(
                                "The 'Hello Facebook' sample  showcases simple Facebook integration")
                        .setContentUrl(imageUri)
                        .build();

                shareDialog.show(linkContent);
            }*/

        } catch (FileNotFoundException e) {
            Log.e("MainActivity", e.getMessage());
        }

      /*  if (ShareDialog.canShow(SharePhotoContent.class)) {
            Bitmap image = Bitmap.createBitmap(bm);
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image)
                    .setCaption("shareeee")
                    .build();

            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();

            ShareApi.share(content, null);

            shareDialog.show(content);
        }*/
      /*  Bitmap image = Bitmap.createBitmap(bm);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption("shareeee")
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);*/


    }
    private void shareOnFacebook(Uri uri) {

        boolean isFacebookAppFound = false;

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("image/png");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).contains("facebook")) {
                isFacebookAppFound = true;
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                startActivity(shareIntent);
                break;
            }
        }

        if (!isFacebookAppFound) {
            Toast.makeText(Shareskip.this,"Please Install the Facebook application.", Toast.LENGTH_SHORT).show();
            publishImage1();
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

    class Loadlist1 extends AsyncTask<String, String, String> {
        String[] name, OtherName,contactNo,address1,area,district,gender,state,groupId,mflag,flag1;
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

                // Building Parameters
               String LOAD_URL =mess+"/rest/user/searchWithRadius";
                PostJsonObject json=new PostJsonObject();
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("lat",lat);
                jsonObj.put("lon",longg);
                jsonObj.put("offset",0);
                jsonObj.put("searchText",group1);
                jsonObj.put("searchRadius","100");
                jsonObj.put("noOfRecords",100);
Log.d("location ha",lat+longg+group1);
                p=json.postJson(jsonObj,LOAD_URL);
                Log.d("Response of list1", p.toString());
                JSONObject j = new JSONObject(p);

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
                flag1=new String[list9.size()];
                countrylist1 = new ArrayList<DonorModel1>();
                for (int i = 0; i < name.length; i++) {
                    /*String REGISTER_URL1 = "http://191.239.57.54:8080/iDonyte/rest/common/getDistricts/stateId/"+state[i];

                    GetJsonObject json1=new GetJsonObject();
                    String response1 =json1.getWebServceObj(REGISTER_URL1);
                    try {
                        JSONObject jsonResponse = new JSONObject(response1);
                        if(jsonResponse.getString("statusMessage").equals("success")){
                            JSONArray jsonDistArr = jsonResponse.getJSONArray("districtList");
                            int[] districIdArr = new int[jsonDistArr.length()];
                            for(int k=0;k<jsonDistArr.length();k++){
                                JSONObject jsonDisObj = jsonDistArr.getJSONObject(k);
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


                    Cursor cursor1 = sqliteHelper2.getUser1(state[i]);
                    if (cursor1.getCount() != 0) {
                        cursor1.moveToFirst();
                        state[i] = cursor1.getString(cursor1.getColumnIndex("id"));
                    }
                    Cursor cursor2 = sqliteHelper4.getUser1(district[i]);
                    if (cursor2.getCount() != 0) {
                        cursor2.moveToFirst();
                        district[i] = cursor2.getString(cursor2.getColumnIndex("id"));
                    }
                    Cursor cursor = sqliteHelper1.getUser(district[i]);
                    if (cursor.getCount() != 0) {
                        cursor.moveToFirst();
                        area1 = cursor.getString(cursor.getColumnIndex("area"));
                    }
                    try {
                        JSONArray jsonAreaArr = new JSONArray(area1);
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

                    Cursor cursor3 = sqliteHelper5.getUser1(area[i]);
                    if (cursor3.getCount() != 0) {
                        cursor3.moveToFirst();
                        area[i] = cursor3.getString(cursor3.getColumnIndex("id"));
                    }*/

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

            if(file_url!=null) {

                // Toast.makeText(DonorActivity.this, "yes"+location.getLatitude()+location.getLatitude()+group1, Toast.LENGTH_LONG).show();

                dlist.setAdapter(adapter1);
                Helper.getListViewSize(dlist);

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
    /*  private void publishImage(uri){
          if (ShareDialog.canShow(ShareLinkContent.class)) {
              ShareLinkContent linkContent = new ShareLinkContent.Builder()
                      .setContentTitle("Hi need help!!")
                      .setContentDescription(
                              "As Dengue fever is spreading its virus in Delhi NCR, and there is platelet shortage; we are compiling the list of platelet donors so that it can reach to the needy person on time.\n" +
                                      "Please send your info in following format if you want to donate platelet\n"+"name,city and bloodgroup")
                      .setContentUrl(Uri.parse(uri))
                      .build();

              shareDialog.show(linkContent);
          }
      }*/
    private void publishImage1(){
        if (ShareDialog.canShow(ShareLinkContent.class)) {

            ShareLinkContent linkContent = new ShareLinkContent.Builder()

                    .setContentTitle("I will do something amazing")
                    .setContentDescription("I will donate platelets of my Blood Group :"+group1+ "\n You can also register and donate through VDONOR App")
                    .setContentUrl(Uri.parse("www.ixeet.com"))
                    .build();

            shareDialog.show(linkContent);
            //   MessageDialog.show(this, linkContent);
            //  }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shareskip, menu);
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
               Shareskip.this);

        alertDialog.setTitle("Enable Location Settings");

        alertDialog
                .setMessage("Please enable location to allow users to locate you for platelates donation/searches.");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        Shareskip.this.startActivity(intent);
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