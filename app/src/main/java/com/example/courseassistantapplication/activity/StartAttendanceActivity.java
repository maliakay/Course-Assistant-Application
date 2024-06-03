package com.example.courseassistantapplication.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.recyclerview.AttandenceAdapter;
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
import com.google.firebase.database.collection.BuildConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartAttendanceActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference mReference;
    private RecyclerView recyclerView;
    private AttandenceAdapter attandenceAdapter;
    private List<String> studentList;
    String courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_attandence);

        int PERMISSION_ID = 44;

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mReference = FirebaseDatabase.getInstance().getReference();
        Button btnStartAttendance = findViewById(R.id.btnStartAttendance);
        Button btnStopAttandence = findViewById(R.id.btnStopAttendance);

        recyclerView = findViewById(R.id.recyclerViewAttandence);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentList = new ArrayList<>();
        attandenceAdapter = new AttandenceAdapter(studentList);
        recyclerView.setAdapter(attandenceAdapter);

        // Retrieve the data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            courseID = intent.getStringExtra("courseId");
        }

        mReference.child("yoklama").child(courseID).
                child("katılan öğrenciler").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String email = snapshot.getValue(String.class);
                        studentList.add(email);
                }
                attandenceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        btnStartAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAttendance();
            }
        });

        btnStopAttandence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StopAttandence();
            }
        });

    }

    private void startAttendance() {
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
    private void StopAttandence(){
        exportCsv();
        mReference.child("yoklama").child(courseID).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                Toast.makeText(StartAttendanceActivity.this, "Yoklama başarıyla alındı", Toast.LENGTH_LONG).show();
            }
        });
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
                            if (location != null) {
                                //ders için yoklamayı başlat
                                mReference.child("yoklama").child(courseID).child("konum").setValue(location);
                                Toast.makeText(StartAttendanceActivity.this, "Yoklama başlatıldı", Toast.LENGTH_SHORT).show();
                            }
                        }
                        // Process location data, e.g., store in database
                        else {
                            Toast.makeText(StartAttendanceActivity.this, "Failed to get location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void exportCsv() {
        File exportDir = getExternalFilesDir(null); // Android 10 ve üzeri için doğru yol
        if (exportDir != null && !exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "attendance.csv");
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.append("Email\n");
            for (String student : studentList) {
                writer.append(student).append("\n");
            }
            writer.flush();
            writer.close();
            openFile(file);
//            Toast.makeText(this, "CSV dosyası başarıyla oluşturuldu: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "CSV dosyası oluşturulurken hata oluştu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void openFile(File file) {
        Uri fileUri = FileProvider.getUriForFile(this, "com.course.fileprovider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "text/csv");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "CSV dosyasını açacak uygulama bulunamadı", Toast.LENGTH_SHORT).show();
        }
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
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportCsv();
            } else {
                Toast.makeText(this, "Dosya yazma izni reddedildi", Toast.LENGTH_SHORT).show();
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

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
        }
    };
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    private void goToJoin(){
        startActivity(new Intent(StartAttendanceActivity.this, JoinAttendanceActivity.class));
    }
}