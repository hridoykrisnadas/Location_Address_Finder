package com.hridoykrisna.addressfinderbylatlang;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_COARSE = 1;
    private static final int REQUEST_FINE_COARSE = 1;
    EditText latEt, longEt, altET;
    Button currentLocationBtn, submitBtn;
    RecyclerView recyclerView;
    LinearLayout altLLT;

    Double lat, lon;
    List<Address> addressList;
    AddressAdapter addressAdapter;

    //Location
    Criteria criteria;
    String bestProvider;

    LocationManager locationManagerTHis;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latEt = findViewById(R.id.lateETId);
        longEt = findViewById(R.id.longETId);
        currentLocationBtn = findViewById(R.id.currentLocationButtonId);
        submitBtn = findViewById(R.id.submitButtonId);
        recyclerView = findViewById(R.id.recyclerviewId);
        altET = findViewById(R.id.altETId);
        altLLT = findViewById(R.id.altLLTId);

        currentLocationBtn.setOnClickListener(v -> {


            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_COARSE);
            }
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_COARSE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                locationManagerTHis = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                criteria = new Criteria();
                bestProvider = String.valueOf(locationManagerTHis.getBestProvider(criteria, true));

                boolean isGPSEnabled = locationManagerTHis.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = locationManagerTHis.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


                Location locationData = null;
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                if (!isNetworkEnabled) {
                    Toast.makeText(getApplicationContext(), "Network Disable", Toast.LENGTH_SHORT).show();
                } else {
                    locationData = locationManagerTHis.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    altLLT.setVisibility(View.VISIBLE);
                    altET.setText(locationData.getAltitude() + " Meter");
                    Toast.makeText(getApplicationContext(), "Network Data", Toast.LENGTH_SHORT).show();

                }
                if (!isGPSEnabled) {
                    Toast.makeText(getApplicationContext(), "GPS Disable", Toast.LENGTH_SHORT).show();
                } else {
                    if (locationData == null) {
                        locationData = locationManagerTHis.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        altLLT.setVisibility(View.VISIBLE);
                        altET.setText(locationData.getAltitude() + " Meter");
                        Toast.makeText(getApplicationContext(), "GPS Data", Toast.LENGTH_SHORT).show();
                    }
                }

                if (locationData == null) {
                    locationManagerTHis = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                    locationData = locationManagerTHis.getLastKnownLocation(bestProvider);
                    Toast.makeText(getApplicationContext(), "Provider Data", Toast.LENGTH_SHORT).show();
                }

                if (locationData != null) {
                    getAddress(locationData.getLatitude(), locationData.getLongitude());
                    longEt.setText(locationData.getLongitude() + "");
                    latEt.setText(locationData.getLatitude() + "");
                } else {
                    Toast.makeText(getApplicationContext(), "Location Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        submitBtn.setOnClickListener(v ->{
            altLLT.setVisibility(View.GONE);
            lat = Double.parseDouble(latEt.getText().toString());
            lon = Double.parseDouble(longEt.getText().toString());

            getAddress(lat, lon);
        });


    }

    private void getAddress(Double lat, Double lon) {
        Geocoder geocoder = new Geocoder(MainActivity.this);
        try {
            addressList = geocoder.getFromLocation(lat, lon, 10);
            addressAdapter = new AddressAdapter(MainActivity.this, addressList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(addressAdapter);
            addressAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}