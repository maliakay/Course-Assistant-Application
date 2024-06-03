package com.example.courseassistantapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.Comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCommentsActivity extends AppCompatActivity {
    private EditText commentContent;
    private Button postCommentButton;
    private FirebaseDatabase database;
    private DatabaseReference mReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String announcementId;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comments);
        announcementId = getIntent().getStringExtra("announcementId");

        commentContent = findViewById(R.id.comment_content);
        postCommentButton = findViewById(R.id.post_comment_button);

        announcementId = getIntent().getStringExtra("announcementId");
        database = FirebaseDatabase.getInstance();
        mReference = database.getReference("announcements").child(announcementId).child("comments");
        mUser= mAuth.getCurrentUser();

        postCommentButton.setOnClickListener(v -> {
            String content = commentContent.getText().toString();
            String commentId = mReference.push().getKey();
            Comment comment = new Comment(commentId, announcementId, mUser.getEmail(), content);
            assert commentId != null;
            mReference.child(commentId).setValue(comment);

            Toast.makeText(context, "Your Comment Has Been Shared", Toast.LENGTH_LONG).show();
            finish();
        });


    }
}