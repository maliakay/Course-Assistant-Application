package com.example.courseassistantapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.Report;
import com.example.courseassistantapplication.recyclerview.ReportAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListReportsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewReports;
    private ReportAdapter reportAdapter;
    private List<Report> reportList;
    private DatabaseReference mReference;
    private FirebaseUser mUser;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_reports);

        mReference = FirebaseDatabase.getInstance().getReference("Şikayetler");

        recyclerViewReports = findViewById(R.id.recyclerViewReports);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        reportList = new ArrayList<>();

        ValueEventListener reportEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reportList.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Report report = childSnapshot.getValue(Report.class);
                    if(report.getAlıcı().equals(mUser.getEmail())){
                        reportList.add(childSnapshot.getValue(Report.class));
                    }
                }
                // Report listesini tarihe göre sırala
                Collections.sort(reportList, new Comparator<Report>() {
                    @Override
                    public int compare(Report r1, Report r2) {
                        return r2.getReportDate().compareTo(r1.getReportDate());
                    }
                });
                reportAdapter = new ReportAdapter(reportList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListReportsActivity.this);
                recyclerViewReports.setLayoutManager(layoutManager);
                recyclerViewReports.setAdapter(reportAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("firebase", "veri okunamadı", error.toException());
            }
        };
        //Listener'ı Database'e ekle
        mReference.addValueEventListener(reportEventListener);

        // Report listesini tarihe göre sırala
        Collections.sort(reportList, new Comparator<Report>() {
            @Override
            public int compare(Report r1, Report r2) {
                return r2.getReportDate().compareTo(r1.getReportDate());
            }
        });
        reportAdapter = new ReportAdapter(reportList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewReports.setLayoutManager(layoutManager);
        recyclerViewReports.setAdapter(reportAdapter);


        reportAdapter.notifyDataSetChanged();
    }
}