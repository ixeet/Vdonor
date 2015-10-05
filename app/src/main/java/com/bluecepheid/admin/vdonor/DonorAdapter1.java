package com.bluecepheid.admin.vdonor;

/**
 * Created by PIYANKA GUPTA on 9/20/2015.
 */

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PRIYANKA GUPTA on 5/6/2015.
 */

/**
 * Created by PRIYANKA GUPTA on 4/29/2015.
 */
public class DonorAdapter1 extends BaseAdapter {


    ArrayList<DonorModel1> listArray=null;
    String userid,name1;
    Context context;
    String bg;
    ArrayList<DonorModel1> countrylist;
    ArrayList<DonorModel1> mmStringFilterList = new ArrayList<DonorModel1>();
    ArrayList<DonorModel1> mStringFilterList;
    String area,district,state,flag,add,cno;
    String phoneNo,imagePreferance;
    public static final String STORAGE_NAME = "VERIFICATION";
    public static final String STORAGE_IMAGE = "Image";


   // TelephonyManager telephonyManager;


    public DonorAdapter1(Context context, ArrayList<DonorModel1> countrylist) {
        this.context = context;
        this.countrylist = countrylist;
        this.mStringFilterList = countrylist;
        SharedPreferences sharedPreferences0= context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);

        phoneNo = sharedPreferences0.getString("phoneNo", "");
        SharedPreferences sharedPreferences = context.getSharedPreferences(STORAGE_IMAGE, Context.MODE_PRIVATE);
        imagePreferance = sharedPreferences.getString("imagePreferance", "");
      // telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);


    }


    @Override
    public int getCount() {
        return countrylist.size();    // total number of elements in the list
    }

    @Override
    public Object getItem(int i) {
        return countrylist.get(i);    // single item in the list
    }

    @Override
    public long getItemId(int i) {
        return i;                   // index number
    }

    @Override
    public View getView(int index, View view, final ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.donor_list1, parent, false);
            // Toast.makeText(parent.getContext(), "There is no data", Toast.LENGTH_SHORT).show();
        }

        final DonorModel1 dataModel = countrylist.get(index);

        final TextView name = (TextView) view.findViewById(R.id.name);
        final TextView number = (TextView) view.findViewById(R.id.number);
        name1=dataModel.getName();
        name.setText(dataModel.getName()+" " +dataModel.getOtherData());
        final TextView contact = (TextView) view.findViewById(R.id.contact);
        Button share = (Button) view.findViewById(R.id.sharebutton);
        Button sms = (Button) view.findViewById(R.id.sms);
        Button call = (Button) view.findViewById(R.id.call);
        area=dataModel.getArea();
        add=dataModel.getAddress1();
        final TextView city = (TextView) view.findViewById(R.id.city);
        district=dataModel.getDistrict();

        final TextView local = (TextView) view.findViewById(R.id.local);
        state=dataModel.getState();
        flag=dataModel.getMflag();

        cno=dataModel.getContactNo();
        if(flag.equals("1"))
        {
            number.setVisibility(View.VISIBLE);

        }
        else
        {
           number.setVisibility(View.GONE);
        }
        if(cno.equals(phoneNo))
        {
            share.setVisibility(View.VISIBLE);
            call.setVisibility(View.GONE);
            sms.setVisibility(View.GONE);

        }
        else
        {
            share.setVisibility(View.GONE);
            call.setVisibility(View.VISIBLE);
            sms.setVisibility(View.VISIBLE);
        }

        contact.setText(add+",");
        if(!area.equals("")) {
            city.setText(area);
        }

        if(!district.equals("")&&!state.equals("")) {
            local.setText(district + "," + state);
        }

        number.setText(""+dataModel.getContactNo());

      /*  final TextView add = (TextView) view.findViewById(R.id.add);
        add.setText(dataModel.getAddress1());*/
        String group=dataModel.getGroupId();
        final TextView bgroup = (TextView) view.findViewById(R.id.bgroup);
        if(group.equals("1"))
        {
            bg="A+";
        }
        else if(group.equals("2"))
        {
            bg="A-";
        }
        else if(group.equals("3"))
        {
            bg="B+";
        }
        else if(group.equals("4"))
        {
            bg="B-";
        }
        else if(group.equals("5"))
        {
            bg="AB+";
        }
        else if(group.equals("6"))
        {
            bg="AB-";
        }
        else if(group.equals("7"))
        {
            bg="O+";
        }
        else if(group.equals("8"))
        {
            bg="O-";
        }

       bgroup.setText(""+bg);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    //publishImage();
                     String blood=dataModel.getGroupId();
                    if(blood.equals("1"))
                    {
                        bg="A+";
                    }
                    else if(blood.equals("2"))
                    {
                        bg="A-";
                    }
                    else if(blood.equals("3"))
                    {
                        bg="B+";
                    }
                    else if(blood.equals("4"))
                    {
                        bg="B-";
                    }
                    else if(blood.equals("5"))
                    {
                        bg="AB+";
                    }
                    else if(blood.equals("6"))
                    {
                        bg="AB-";
                    }
                    else if(blood.equals("7"))
                    {
                        bg="O+";
                    }
                    else if(blood.equals("8"))
                    {
                        bg="O-";
                    }
                     String name1=dataModel.getName();

                    // Toast.makeText(Shareskip.this,"image"+ imagePreferance, Toast.LENGTH_SHORT).show();
                    if (!imagePreferance.equals("")) {
                        Bitmap p = decodeBase64(imagePreferance);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        p.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] bitmapdata = stream.toByteArray();

                        shareImageOnFacebook(name1, bg, bitmapdata);
                        //Toast.makeText(Shareskip.this, "Yesss image found.", Toast.LENGTH_SHORT).show();
                    } else {


                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.no_photo);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] bitmapdata = stream.toByteArray();

                        shareImageOnFacebook(name1, bg, bitmapdata);
                        //Toast.makeText(Shareskip.this, "No image found.", Toast.LENGTH_SHORT).show();
                    }
                } else

                {
                    Toast.makeText(context,
                            "Please connect to Internet",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
       // telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact=dataModel.getContactNo();
                String cont="tel:"+contact;
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(cont));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.startActivity(callIntent);

            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact=dataModel.getContactNo();
                String name=dataModel.getName();
                String cont="tel:"+contact;
                Intent sms = new Intent(Intent.ACTION_VIEW);
                // prompts only sms-mms clients
                sms.setType("vnd.android-dir/mms-sms");
                // extra fields for number and message respectively
                sms.putExtra("address", contact);
                sms.putExtra("sms_body","Hi ,"+name + "\n Urgently required platelets for someone close to me. I found your donation request at VDONOR.\n" +
                        "Please revert or call me back if you are available for donation.");
                sms.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.startActivity(sms);

            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    public boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.e("Network Testing", "***Available***");
            return true;
        }
        Log.e("Network Testing", "***Not Available***");
        return false;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
       /* BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;*/
        Bitmap bmp = BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
        return bmp;
    }
    public void shareImageOnFacebook(String name, String bloodGroup, byte[] imageInBytes) {
        RelativeLayout viewToBeShared = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.layout_image, null);

        TextView tvName = (TextView) viewToBeShared.findViewById(R.id.tvName);
        tvName.setText(name);

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
            PackageManager pm = context.getPackageManager();
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
          openInChooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            context.startActivity(openInChooser);
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
}