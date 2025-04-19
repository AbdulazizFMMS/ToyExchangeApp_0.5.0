//package com.yigit.toyexchanger.activities;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.karumi.dexter.Dexter;
//import com.karumi.dexter.PermissionToken;
//import com.karumi.dexter.listener.PermissionDeniedResponse;
//import com.karumi.dexter.listener.PermissionGrantedResponse;
//import com.karumi.dexter.listener.PermissionRequest;
//import com.karumi.dexter.listener.single.PermissionListener;
//import com.yigit.toyexchanger.R;
//import com.yigit.toyexchanger.utils.LocationAndDateHelper;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//public class GetLocatoinActivity extends AppCompatActivity  {
////ImageView image_back_map;
////Button continueButton;
//////    private GoogleMap mMap;
//////    private FusedLocationProviderClient mFusedLocationClient;
//////    private Geocoder geocoder;
//////    SupportMapFragment supportMapFragment;
////
////    EditText searchEditText;
//
//    String locationLink,locationDate;
//    Task<Location> locationTask;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_get_locatoin);
//
//
////        searchEditText = findViewById(R.id.searchEditText);
////        image_back_map = findViewById(R.id.image_back_map);
////        continueButton= findViewById(R.id.continueButton);
//        // Initialize Geocoder
////        geocoder = new Geocoder(this, Locale.getDefault());
//
////        supportMapFragment = SupportMapFragment.newInstance();
////        getSupportFragmentManager().beginTransaction()
////                .replace(R.id.frame_layout, supportMapFragment)
////                .commit();
//
////        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
////                .findFragmentById(R.id.frame_layout);
////        mapFragment.getMapAsync(this);
//
//
////        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//
////        getpermittionandfindcurrentGps();
//
//
//
//        // Button to save the selected location
//        continueButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(GetLocatoinActivity.this, "İşleminiz Tamamlandı", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    performSearch(v.getText().toString());
//                    return true;
//                }
//                return false;
//            }
//        });
//
//
//
//        image_back_map.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(GetLocatoinActivity.this,ActivityTeslimatAdresi.class);
//                startActivity(i);
//                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);
//            }
//        });
//
//
//
//
//
//    } // OnCreate
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    private void performSearch(String query) {
//        // Your search logic here
//    }
//
//
//
//
//
//
//    private void pushLocationandDateToFirebse() {
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference reference = database.getReference("MyLocations");
//        String gpslink = locationLink;
//        String date = locationDate;
//
//        LocationAndDateHelper locationAndDateHelper = new LocationAndDateHelper(gpslink,date );
//        reference.child(date).setValue(locationAndDateHelper);
//    }
//
//
//
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);
//
//
//    }
//
//
//
//}