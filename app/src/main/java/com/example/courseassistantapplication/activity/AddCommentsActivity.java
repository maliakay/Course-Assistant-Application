package com.example.courseassistantapplication.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.Comment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCommentsActivity extends AppCompatActivity {
    private EditText commentContent;
    private Button postCommentButton;
    private FirebaseDatabase database;
    private DatabaseReference commentsRef;
    private String announcementId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comments);

        commentContent = findViewById(R.id.comment_content);
        postCommentButton = findViewById(R.id.post_comment_button);

        announcementId = getIntent().getStringExtra("announcementId");
        database = FirebaseDatabase.getInstance();
        commentsRef = database.getReference("announcements").child(announcementId).child("comments");

        postCommentButton.setOnClickListener(v -> {
            String content = commentContent.getText().toString();
            String commentId = commentsRef.push().getKey();
            Comment comment = new Comment(commentId, announcementId, "stdId", content); // Replace "stdId" with the actual student ID

            commentsRef.child(commentId).setValue(comment);
            finish();
        });


    }
}