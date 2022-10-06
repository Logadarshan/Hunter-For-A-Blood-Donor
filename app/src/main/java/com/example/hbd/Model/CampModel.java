package com.example.hbd.Model;

public class CampModel {

    String hosname, campname, campdate, campdonorname, campdonorblood, campdonorage, campdonornum;
    String campid, campstate;

    public CampModel() {
    }

    public CampModel(String hosname, String campname, String campdate, String campdonorname, String campdonorblood, String campdonorage, String campdonornum,
                     String campid, String campstate) {
        this.hosname = hosname;
        this.campname = campname;
        this.campdate = campdate;
        this.campdonorname = campdonorname;
        this.campdonorblood = campdonorblood;
        this.campdonorage = campdonorage;
        this.campdonornum = campdonornum;
        this.campid = campid;
        this.campstate = campstate;
    }


    public String getHosname() {
        return hosname;
    }

    public void setHosname(String hosname) {
        this.hosname = hosname;
    }

    public String getCampname() {
        return campname;
    }

    public void setCampname(String campname) {
        this.campname = campname;
    }

    public String getCampdate() {
        return campdate;
    }

    public void setCampdate(String campdate) {
        this.campdate = campdate;
    }

    public String getCampdonorname() {
        return campdonorname;
    }

    public void setCampdonorname(String campdonorname) {
        this.campdonorname = campdonorname;
    }

    public String getCampdonorblood() {
        return campdonorblood;
    }

    public void setCampdonorblood(String campdonorblood) {
        this.campdonorblood = campdonorblood;
    }

    public String getCampdonorage() {
        return campdonorage;
    }

    public void setCampdonorage(String campdonorage) {
        this.campdonorage = campdonorage;
    }

    public String getCampdonornum() {
        return campdonornum;
    }

    public void setCampdonornum(String campdonornum) {
        this.campdonornum = campdonornum;
    }

    public String getCampid() {
        return campid;
    }

    public void setCampid(String campid) {
        this.campid = campid;
    }

    public String getCampstate() {
        return campstate;
    }

    public void setCampstate(String campstate) {
        this.campstate = campstate;
    }
}
