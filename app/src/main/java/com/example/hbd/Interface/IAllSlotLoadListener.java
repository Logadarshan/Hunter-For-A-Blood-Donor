package com.example.hbd.Interface;

import com.example.hbd.Model.HospitalModel;
import com.example.hbd.Model.SlotModel;

import java.util.List;

public interface IAllSlotLoadListener {

    void onAllSlotLoadSuccess(List<String> slotlist);
    void onAllSlotLoadFailed(String message);

    void onAllSlotTimeLoadSuccess(List<SlotModel> slotModelList);
    void onAllSlotTimeLoadFailed(String message);

}
