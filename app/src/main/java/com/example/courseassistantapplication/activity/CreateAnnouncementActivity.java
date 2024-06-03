package com.example.courseassistantapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.Announcement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateAnnouncementActivity extends AppCompatActivity {
    private EditText announcementContent;
    private Switch alertSwitch;
    private Button postButton, btViewAnnouncements;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private Context context;
    private String courseId;

    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);

        courseId = getIntent().getStringExtra("courseId");
        announcementContent = findViewById(R.id.announcement_content);
        alertSwitch = findViewById(R.id.alert_switch);
        postButton = findViewById(R.id.post_button);
        btViewAnnouncements=findViewById(R.id.btViewAnnouncements);
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference("announcements");
        mUser = mAuth.getCurrentUser();

        postButton.setOnClickListener(v -> {
            String content = announcementContent.getText().toString();
            boolean alert = alertSwitch.isChecked();
            String announcementId = mReference.push().getKey();
            Announcement announcement = new Announcement(courseId, mUser.getEmail(), content, announcementId, new ArrayList<>(), alert);

            assert announcementId != null;
            mReference.child(announcementId).setValue(announcement);
            finish();
        });


    }
    public void viewAnnouncements(View view){
        startActivity(new Intent(CreateAnnouncementActivity.this, AnnouncementActivity.class));
    }
}
