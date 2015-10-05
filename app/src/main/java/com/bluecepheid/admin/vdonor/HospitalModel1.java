package com.bluecepheid.admin.vdonor;

/**
 * Created by PIYANKA GUPTA on 9/20/2015.
 */

import android.graphics.Bitmap;

/**
 * Created by Admin on 01-07-2015.
 */

public class HospitalModel1 {
    private String name;
    private Bitmap photo;
    public boolean isChecked;
    String contactNo1,address1,contactNo2;
    private String OtherData;
    public HospitalModel1(String name) {
        this.name = name;


    }
    public HospitalModel1(String name, String address1,String contactNo1,String contactNo2 ) {
        this.name = name;


        this.address1 = address1;
        this.contactNo1 = contactNo1;
        this.contactNo2 = contactNo2;

    }


    public String getName() {
        return this.name;
    }
    public String getContactNo1() {
        return this.contactNo1;
    }
    public String getAddress1() {
        return this.address1;
    }


    public String getContactNo2() {
        return this.contactNo2;
    }


}