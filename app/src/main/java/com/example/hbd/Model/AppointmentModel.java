package com.example.hbd.Model;

public class AppointmentModel {

    String appointmentid, usernameappoint, dataappointment, timeappointment, hospappointment, statusappointment,useridappointment;
    String appslotid,apploc,balslot;
    String userblood;


    public AppointmentModel() {
    }

    public AppointmentModel(String appointmentid, String usernameappoint, String dataappointment, String timeappointment, String hospappointment, String statusappointment, String useridappointment
    ,String appsloid, String apploc, String balslot, String userblood ) {
        this.appointmentid = appointmentid;
        this.usernameappoint = usernameappoint;
        this.dataappointment = dataappointment;
        this.timeappointment = timeappointment;
        this.hospappointment = hospappointment;
        this.statusappointment = statusappointment;
        this.useridappointment = useridappointment;
        this.appslotid = appsloid;
        this.apploc = apploc;
        this.balslot = balslot;
        this.userblood = userblood;
    }

    public String getAppointmentid() {
        return appointmentid;
    }

    public void setAppointmentid(String appointmentid) {
        this.appointmentid = appointmentid;
    }

    public String getUsernameappoint() {
        return usernameappoint;
    }

    public void setUsernameappoint(String usernameappoint) {
        this.usernameappoint = usernameappoint;
    }

    public String getDataappointment() {
        return dataappointment;
    }

    public void setDataappointment(String dataappointment) {
        this.dataappointment = dataappointment;
    }

    public String getTimeappointment() {
        return timeappointment;
    }

    public void setTimeappointment(String timeappointment) {
        this.timeappointment = timeappointment;
    }

    public String getHospappointment() {
        return hospappointment;
    }

    public void setHospappointment(String hospappointment) {
        this.hospappointment = hospappointment;
    }

    public String getStatusappointment() {
        return statusappointment;
    }

    public void setStatusappointment(String statusappointment) {
        this.statusappointment = statusappointment;
    }

    public String getUseridappointment() {
        return useridappointment;
    }

    public void setUseridappointment(String useridappointment) {
        this.useridappointment = useridappointment;
    }


    public String getAppslotid() {
        return appslotid;
    }

    public void setAppslotid(String appslotid) {
        this.appslotid = appslotid;
    }

    public String getApploc() {
        return apploc;
    }

    public void setApploc(String apploc) {
        this.apploc = apploc;
    }

    public String getBalslot() {
        return balslot;
    }

    public void setBalslot(String balslot) {
        this.balslot = balslot;
    }

    public String getUserblood() {
        return userblood;
    }

    public void setUserblood(String userblood) {
        this.userblood = userblood;
    }
}
