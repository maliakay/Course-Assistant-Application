package com.example.courseassistantapplication.activity;

import static java.lang.Math.abs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.MyLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinAttendanceActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference mReference;
    private Button btnStartAttendance;
    private String courseID;
    private String userMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_attandence);

        int PERMISSION_ID = 44;

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mReference = FirebaseDatabase.getInstance().getReference("yoklama");
        btnStartAttendance = findViewById(R.id.btnJoinAttendance);

        // Retrieve the data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            courseID = intent.getStringExtra("courseId");
            userMail = intent.getStringExtra("userMail");
        }

        btnStartAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReference.child(courseID).child("katılan öğrenciler").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean isAlreadyAttend = false;
                        for (DataSnapshot childSnapshot : snapshot.getChildren()){
                            if ( childSnapshot.getValue().equals(userMail) ){
                                Toast.makeText(JoinAttendanceActivity.this, "Yoklamada kaydınız bulunmakta", Toast.LENGTH_SHORT).show();
                                isAlreadyAttend = true;
                            }
                        }
                        if (!isAlreadyAttend){
                            checkPermissions();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(JoinAttendanceActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            if(isLocationEnabled()){
                getLastLocation();
            }else{
                Toast.makeText(this, "Lokasyon servisinizi açınız", Toast.LENGTH_LONG).show();
            }

        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        requestNewLocationData();
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location location = task.getResult();
                            if (location == null) {
                                requestNewLocationData();
                                joinToAttandence(location);
                            } else {
                                joinToAttandence(location);
                            }
                        }
                        else {
                            Toast.makeText(JoinAttendanceActivity.this, "Failed to get location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private void joinToAttandence(Location location){
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if(childSnapshot.getKey().equals(courseID)){
                        MyLocation instructorLocation = childSnapshot.child("konum").getValue(MyLocation.class);
                        Log.d("lokasyon", String.valueOf(instructorLocation.distanceTo(location)) +" "+ String.valueOf(abs(location.getAltitude()-instructorLocation.getAltitude())));
                        if(instructorLocation.distanceTo(location) < 20 &&
                                abs(location.getAltitude()-instructorLocation.getAltitude()) < 3){
                            mReference.child(courseID).child("katılan öğrenciler").push().setValue(userMail);
                            Toast.makeText(JoinAttendanceActivity.this, "Yoklamaya katıldın", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(JoinAttendanceActivity.this, "Derslikte değilsiniz", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(JoinAttendanceActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
        }
    };
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}