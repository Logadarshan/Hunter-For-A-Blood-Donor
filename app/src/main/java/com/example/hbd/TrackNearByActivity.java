package com.example.hbd;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.hbd.databinding.ActivityTrackNearByBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Vector;

public class TrackNearByActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityTrackNearByBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final  int Request_code =101;
    private  double lat,lng;
    ImageButton local;

    MarkerOptions markerOptions;
    LatLng centerlocation;
    Vector<MarkerOptions> markerOptionsVector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTrackNearByBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getApplicationContext());

        local = findViewById(R.id.localhos);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        centerlocation = new LatLng(3.0, 101);

        markerOptionsVector = new Vector<>();

        markerOptionsVector.add(new MarkerOptions()
                .position(new LatLng(2.7093,101.9435)).title("Hospital Tuanku Jaafar")
                .snippet("Jalan Rasah, 70300 Seremban, Negeri Sembilan")
                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.localhospitalicon)));
        markerOptionsVector.add(new MarkerOptions()
                .position(new LatLng(3.1731,101.7064)).title("Pusat Darah Negara")
                .snippet("Jalan Tun Razak, 50400 Kuala Lumpur")
                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.localhospitalicon)));
        markerOptionsVector.add(new MarkerOptions()
                .position(new LatLng(1.4585,103.7460)).title("Hospital Sultanah Aminah")
                .snippet("80100 Johor Bharu, Johor")
                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.localhospitalicon)));
        markerOptionsVector.add(new MarkerOptions()
                .position(new LatLng(2.2172,102.2613)).title("Hospital Melaka")
                .snippet("Jalan Mufti Hj.Khalil, 75499 Melaka")
                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.localhospitalicon)));
        markerOptionsVector.add(new MarkerOptions()
                .position(new LatLng(3.0202,101.4399)).title("Hospital Tengku Ampuan Rahimah")
                .snippet("41200 Klang, Selangor ")
                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.localhospitalicon)));
        markerOptionsVector.add(new MarkerOptions()
                .position(new LatLng(4.6039,101.0902)).title("Hospital Raja Permaisuri Bainun")
                .snippet("30450 Ipoh, Perak")
                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.localhospitalicon)));
        markerOptionsVector.add(new MarkerOptions()
                .position(new LatLng(6.1488,100.4064)).title("Hospital Sultanah Bahiyah")
                .snippet("05100 Alor Star, Kedah")
                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.localhospitalicon)));
        markerOptionsVector.add(new MarkerOptions()
                .position(new LatLng(5.4169,100.3113)).title("Hospital Pulau Pinang")
                .snippet("Jalan Residensi, 10450 Pulau Pinang")
                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.localhospitalicon)));
        markerOptionsVector.add(new MarkerOptions()
                .position(new LatLng(6.4409,100.1914)).title("Hospital Tuanku Fauziah")
                .snippet("01000 Kangar, Perlis")
                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.localhospitalicon)));
        markerOptionsVector.add(new MarkerOptions()
                .position(new LatLng(6.1248,102.2457)).title("Hospital Raja Perempuan Zainab II")
                .snippet("Jalan Hospital, 15000 Kota Bharu, Kelantan")
                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.localhospitalicon)));
        markerOptionsVector.add(new MarkerOptions()
                .position(new LatLng(5.3239,103.1507)).title("Hospital Sultanah Nur Zahirah")
                .snippet("20400 Kuala Terengganu, Terengganu")
                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.localhospitalicon)));
        markerOptionsVector.add(new MarkerOptions()
                .position(new LatLng(3.8009,103.3215)).title("Hospital Tengku Ampuan Afzan")
                .snippet("25100 Kuantan, Pahang")
                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.localhospitalicon)));
        markerOptionsVector.add(new MarkerOptions()
                .position(new LatLng(1.5436,110.3396)).title("Hospital Umum Sarawak")
                .snippet("Jalan Tun Ahmad Zaidi Adruse, 93586 Kuching, Sarawak")
                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.localhospitalicon)));
        markerOptionsVector.add(new MarkerOptions()
                .position(new LatLng(5.9568,116.0726)).title("Hospital Queen Elizabeth")
                .snippet("88586 Kota Kinabalu, Sabah")
                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.localhospitalicon)));






    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (MarkerOptions mark : markerOptionsVector){
                    mMap.addMarker(mark);
                }
            }
        });

        // Add a marker in current location
        enableMyLocation();


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerlocation ,6));


    }


    private  void enableMyLocation(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            Location location;
            if (mMap != null){
                mMap.setMyLocationEnabled(true);
                Task<Location> task = fusedLocationProviderClient.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null){

                            lat = location.getLatitude();
                            lng = location.getLongitude();


                            LatLng latLng1 = new LatLng(lat,lng);
                            mMap.addMarker(new MarkerOptions().position(latLng1).title("Current location")
                                    .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.peopleicon)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 13));


                        }



                    }
                });

            }

        }else{

            ActivityCompat.requestPermissions
                    (this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_code);


        }


    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId){

        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }

}