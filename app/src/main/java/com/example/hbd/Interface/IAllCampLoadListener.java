package com.example.hbd.Interface;

import java.util.List;

public interface IAllCampLoadListener {

    void onAllCampLoadSuccess(List<String> campList);
    void onAllCampLoadFailed(String message);



}
