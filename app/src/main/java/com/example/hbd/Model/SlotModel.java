package com.example.hbd.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class SlotModel implements Parcelable {

    private  String date, time,capacity,slotid,timestamp;

    public SlotModel() {
    }


    public SlotModel(String date, String time, String capacity, String slotid, String timestamp) {
        this.date = date;
        this.time = time;
        this.capacity = capacity;
        this.slotid = slotid;
        this.timestamp = timestamp;
    }

    protected SlotModel(Parcel in) {
        date = in.readString();
        time = in.readString();
        capacity = in.readString();
        slotid = in.readString();
        timestamp = in.readString();
    }

    public static final Creator<SlotModel> CREATOR = new Creator<SlotModel>() {
        @Override
        public SlotModel createFromParcel(Parcel in) {
            return new SlotModel(in);
        }

        @Override
        public SlotModel[] newArray(int size) {
            return new SlotModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(capacity);
        dest.writeString(slotid);
        dest.writeString(timestamp);
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getSlotid() {
        return slotid;
    }

    public void setSlotid(String slotid) {
        this.slotid = slotid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
