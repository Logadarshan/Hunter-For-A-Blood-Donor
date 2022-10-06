package com.example.hbd.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class HospitalModel implements Parcelable {

    String name, address,hospitalid, Image,num;


    public HospitalModel() {
    }

    public HospitalModel(String name, String address, String hospitalid,String Image,String num) {
        this.name = name;
        this.address = address;
        this.hospitalid = hospitalid;
        this.Image = Image;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHospitalid() {
        return hospitalid;
    }

    public void setHospitalid(String hospitalid) {
        this.hospitalid = hospitalid;
    }

    protected HospitalModel(Parcel in) {
        name = in.readString();
        address = in.readString();
        hospitalid = in.readString();
    }

    public static final Creator<HospitalModel> CREATOR = new Creator<HospitalModel>() {
        @Override
        public HospitalModel createFromParcel(Parcel in) {
            return new HospitalModel(in);
        }

        @Override
        public HospitalModel[] newArray(int size) {
            return new HospitalModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(hospitalid);
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
