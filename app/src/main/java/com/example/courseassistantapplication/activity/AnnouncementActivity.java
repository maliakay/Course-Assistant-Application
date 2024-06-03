package com.example.courseassistantapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.Announcement;
import com.example.courseassistantapplication.recyclerview.AnnouncementAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AnnouncementAdapter announcementAdapter;
    private List<Announcement> announcementList;
    private FirebaseDatabase database;
    private DatabaseReference mReference;
    private Context context;
    String courseId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        courseId = getIntent().getStringExtra("courseId");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        announcementList = new ArrayList<>();
        announcementAdapter = new AnnouncementAdapter(announcementList);
        recyclerView.setAdapter(announcementAdapter);

        database = FirebaseDatabase.getInstance();
        mReference = database.getReference("announcements");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                announcementList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Announcement announcement = snapshot.getValue(Announcement.class);
                    announcementList.add(announcement);
                }
                announcementAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Error On Reaching Data", Toast.LENGTH_LONG).show();
            }
        });
    }

}
