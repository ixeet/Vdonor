package com.bluecepheid.admin.vdonor;

/**
 * Created by PIYANKA GUPTA on 9/20/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by PRIYANKA GUPTA on 5/6/2015.
 */

/**
 * Created by PRIYANKA GUPTA on 4/29/2015.
 */
public class HospitalAdapter1 extends BaseAdapter {


    ArrayList<HospitalModel1> listArray=null;
    String userid;
    Context context;
    ArrayList<HospitalModel1> countrylist;
    ArrayList<HospitalModel1> mmStringFilterList = new ArrayList<HospitalModel1>();
    ArrayList<HospitalModel1> mStringFilterList;

    int flag;
    // TelephonyManager telephonyManager;


    public HospitalAdapter1(Context context, ArrayList<HospitalModel1> countrylist) {
        this.context = context;
        this.countrylist = countrylist;
        this.mStringFilterList = countrylist;

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
            view = inflater.inflate(R.layout.hosplist_item, parent, false);
            // Toast.makeText(parent.getContext(), "There is no data", Toast.LENGTH_SHORT).show();
        }

        final HospitalModel1 dataModel = countrylist.get(index);

        final TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(dataModel.getName());
        final TextView add1 = (TextView) view.findViewById(R.id.address1);
       add1.setText(dataModel.getAddress1());
        final TextView contact1 = (TextView) view.findViewById(R.id.contact1);
        contact1.setText(dataModel.getContactNo1());
        final TextView contact2 = (TextView) view.findViewById(R.id.contact2);
        contact2.setText(dataModel.getContactNo2());

        // telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
        Button call = (Button) view.findViewById(R.id.call);
        if(dataModel.getContactNo1().equals(""))
        {
           call.setVisibility(View.GONE);
        }
        else
        {
           call.setVisibility(View.VISIBLE);
        }
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact=dataModel.getContactNo1();
                String cont="tel:"+contact;
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(cont));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.startActivity(callIntent);

            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }





}