package com.example.hbd.Interface;

import java.util.List;

public interface IAllStateLoadListener {

    void onAllStateLoadSuccess(List<String> stateList);
    void onAllStateLoadFailed(String message);
}
