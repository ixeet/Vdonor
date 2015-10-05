package com.bluecepheid.admin.vdonor;

/**
 * Created by PIYANKA GUPTA on 9/20/2015.
 */

import android.graphics.Bitmap;

/**
 * Created by Admin on 01-07-2015.
 */

public class DonorModel1 {
    private String name;
    private Bitmap photo;
    public boolean isChecked;
    String contactNo,address1,area,district,state,gender,groupId,mflag;
    private String OtherData;
    public DonorModel1(String name) {
        this.name = name;


    }
    public DonorModel1(String name, String otherData,String contactNo,String address1,String area,String district,String state,String gender,String groupId,String mflag ) {
        this.name = name;

        this.OtherData = otherData;
        this.contactNo = contactNo;
        this.address1 = address1;
        this.area = area;
        this.district = district;
        this.state = state;
        this.gender = gender;
        this.groupId = groupId;
        this.mflag=mflag;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return this.contactNo;
    }




    public String getArea() {
        return this.area;
    }


    public void setArea(String name) {
        this.area = area;
    }
    public String getAddress1() {
        return this.address1;
    }


    public void setAddress1(String name) {
        this.address1 = name;
    }
    public String getDistrict() {
        return this.district;
    }


    public void setDistrict(String name) {
        this.district = name;
    }
    public String getState() {
        return this.state;
    }


    public void setState(String name) {
        this.state = name;
    }
    public String getGender() {
        return this.gender;
    }


    public void setGender(String name) {
        this.gender = name;
    }

    public String getOtherData() {
        return this.OtherData;
    }


    public void setOtherData(String otherData) {
        OtherData = otherData;
    }
    public String getGroupId() {
        return this.groupId;
    }

    public String getFlag() {
        return this.mflag;
    }

    public String getMflag() {
        return this.mflag;
    }
    public void setGroupId(String otherData) {
        this.groupId = otherData;
    }
}