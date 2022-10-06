package com.example.hbd.Interface;

import java.util.List;

public interface IAllHosLoadListener {

    void onAllHosLoadSuccess(List<String> hosList);
    void onAllHosLoadFailed(String message);


}
