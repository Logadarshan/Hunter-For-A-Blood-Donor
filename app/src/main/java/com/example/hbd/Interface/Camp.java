package com.example.hbd.Interface;

import com.example.hbd.Model.CampModel;


import java.util.List;

public interface Camp {


    void onAllCampaignLoadSuccess(List<CampModel> campModelList);
    void onAllCampaignLoadFailed(String message);

}
