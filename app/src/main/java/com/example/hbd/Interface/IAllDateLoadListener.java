package com.example.hbd.Interface;

import java.util.List;

public interface IAllDateLoadListener {

    void onAllDateLoadSuccess(List<String> dateList);
    void onAllDateLoadFailed(String message);


}
