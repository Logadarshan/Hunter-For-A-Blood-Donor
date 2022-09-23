package com.example.hbd.Others;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.content.Context;

public class preferences {

    // authenticate login details

    private static final String DATA_LOGIN = "status_login",
                 DATA_AS = "as";


    private static SharedPreferences getSharedReferences(Context context){

        return PreferenceManager.getDefaultSharedPreferences(context);

    }


    public static void setDataAs(Context context, String data){

        SharedPreferences.Editor editor = getSharedReferences(context).edit();
        editor.putString(DATA_AS, data);
        editor.apply();

    }


    public static String getDataAs(Context context){
        
        return getSharedReferences(context).getString(DATA_AS, "");
    }


    public static void setDataLogin(Context  context, boolean status){
        SharedPreferences.Editor editor = getSharedReferences(context).edit();
        editor.putBoolean(DATA_LOGIN, status);
        editor.apply();
    }

    public static boolean getDataLogin(Context context){

        return getSharedReferences(context).getBoolean(DATA_LOGIN, false);

    }

    public static void clearData(Context context){

        SharedPreferences.Editor editor = getSharedReferences(context).edit();
        editor.remove(DATA_AS);
        editor.remove(DATA_LOGIN);
        editor.apply();

    }

}
