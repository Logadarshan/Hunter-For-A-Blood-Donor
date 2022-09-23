package com.example.hbd.Others;

import android.content.Intent;

import com.example.hbd.Model.HospitalModel;
import com.example.hbd.Model.SlotModel;
import com.example.hbd.Model.UserModel;

import java.util.Random;

public class Common {



    public static final String KEY_ENABLE_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_HOSPITAL_STORE = "HOSPITAL SAVE";
    public static final String KEY_DATE_LOAD_DONE = "DATE_LOAD_DONE";
    public static final String KEY_TIME_LOAD_DONE = "TIME_LOAD_DONE";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_DATE_SELECTD = "DATE_SELECTED";
    public static final String KEY_TIME_SELECTD = "TIME_SELECTED";
    public static final String KEY_CONFIRM_APPOINTMENT = "CONFIRM_APPOINTMENT";


    public static  int step =0;


    public static String city = "";
    public static String appointmenthospital = "";
    public static String appointmentdate = "";
    public static String appointments = "";



    public static SlotModel currentdate;
    public static HospitalModel currentHosp;



    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";

    public static final int TIME_SLOT_TOTAL = 5;


    public static String generateString(int lenght){

        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for(int i=0 ; i< lenght; i++){
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }

        return stringBuilder.toString();

    }


}
