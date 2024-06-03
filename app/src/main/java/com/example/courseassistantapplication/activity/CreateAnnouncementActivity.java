package com.example.courseassistantapplication.activity;

import android.os.Bundle;
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
    private Button postButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;

    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);


        announcementContent = findViewById(R.id.announcement_content);
        alertSwitch = findViewById(R.id.alert_switch);
        postButton = findViewById(R.id.post_button);
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference("announcements");
        mUser = mAuth.getCurrentUser();

        postButton.setOnClickListener(v -> {
            String content = announcementContent.getText().toString();
            boolean alert = alertSwitch.isChecked();
            String announcementId = mReference.push().getKey();
            //TODO courseID birleştirilirken alınacak
            Announcement announcement = new Announcement("courseId", mUser.getEmail(), content, announcementId, new ArrayList<>(), alert);

            mReference.child(announcementId).setValue(announcement);
            finish();
        });
    }
}
