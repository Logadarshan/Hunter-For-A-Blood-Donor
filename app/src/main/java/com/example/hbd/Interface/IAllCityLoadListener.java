package com.example.hbd.Interface;

import com.example.hbd.Model.HospitalModel;

import java.util.List;

public interface IAllCityLoadListener {

    void onAllCityLoadSuccess(List<String> cityList);
    void onAllCityLoadFailed(String message);

    void onAllHospLoadSuccess(List<HospitalModel> hospitalList);
    void onAllHospLoadFailed(String message);





}
