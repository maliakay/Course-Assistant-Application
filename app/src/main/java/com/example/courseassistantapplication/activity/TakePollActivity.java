package com.example.courseassistantapplication.activity;

// TakePollActivity.java
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.recyclerview.AnswerQuestionsAdapter;
import com.example.courseassistantapplication.recyclerview.QuestionsAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakePollActivity extends AppCompatActivity {

    private TextView textViewPollTitle;
    private RecyclerView recyclerViewQuestions;
    private Button buttonSubmitAnswers;
    private AnswerQuestionsAdapter AnswerQuestionsAdapter;
    private List<String> questionsList;
    private List<String> answersList;
    private String pollId;

    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_poll);

        textViewPollTitle = findViewById(R.id.textViewPollTitle);
        recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions);
        buttonSubmitAnswers = findViewById(R.id.buttonSubmitAnswers);

        mReference = FirebaseDatabase.getInstance().getReference();
        questionsList = new ArrayList<>();
        answersList = new ArrayList<>();
        AnswerQuestionsAdapter = new AnswerQuestionsAdapter(questionsList,answersList);

        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewQuestions.setAdapter(AnswerQuestionsAdapter);

        pollId = getIntent().getStringExtra("pollId");
        loadPoll();

        buttonSubmitAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswers();
            }
        });
    }

    private void loadPoll() {
        mReference.child("Anketler").child(pollId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("title").getValue(String.class);
                textViewPollTitle.setText(title);

                for (DataSnapshot questionSnapshot : dataSnapshot.child("questions").getChildren()) {
                    String question = questionSnapshot.getValue(String.class);
                    questionsList.add(question);
                    answersList.add(""); // Initialize with empty answers
                }
                AnswerQuestionsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TakePollActivity.this, "Anket yüklenemedi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitAnswers() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> answers = new HashMap<>();
        for (int i = 0; i < questionsList.size(); i++) {
            answers.put("question" + (i + 1), answersList.get(i));
        }

        mReference.child("Anketler").child(pollId).child("responses").child(userId).setValue(answers).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(TakePollActivity.this, "Cevaplar başarıyla gönderildi", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(TakePollActivity.this, "Cevaplar gönderilemedi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
