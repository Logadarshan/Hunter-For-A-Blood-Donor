package com.example.hbd;


import static com.google.android.gms.common.util.MapUtils.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.hbd.utils.MapUtils;
import com.huawei.hms.maps.utils.MapsAdvUtil;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapFragment;
import com.huawei.hms.maps.MapsInitializer;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.LatLng;

public class MapFragmentDemoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapFragmentDemoActivity";

    private HuaweiMap hMap;

    private MapFragment mMapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_map_fragment_demo);
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mMapFragment.getMapAsync(this);



        // this line is needed before using huawei map kit, since version 'com.huawei.hms:maps:6.5.0.301'
        MapsInitializer.initialize(this);

        // please replace "Your API key" with api_key field value in
        // agconnect-services.json if the field is null.
        MapsInitializer.setApiKey(MapUtils.API_KEY);


    }

    @Override
    public void onMapReady(HuaweiMap map) {
        Log.d(TAG, "onMapReady: ");
        hMap = map;
        hMap.getUiSettings().setMyLocationButtonEnabled(false);
        hMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.893478, 2.334595), 10));
    }
}