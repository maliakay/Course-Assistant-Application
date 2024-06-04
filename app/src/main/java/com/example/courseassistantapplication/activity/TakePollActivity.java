package com.example.courseassistantapplication.activity;

import android.content.Intent;
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
import com.example.courseassistantapplication.model.Poll;
import com.example.courseassistantapplication.recyclerview.AnswerQuestionsAdapter;
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

    private RecyclerView recyclerViewQuestions;
    private AnswerQuestionsAdapter QuestionPollAdapter;
    private List<String> questionsList;
    private List<String> answersList;
    private DatabaseReference mReference;
    private String pollId,pollName;
    private String userId;
    private Button buttonSubmit;
    private TextView textViewPollTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_poll);

        recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        textViewPollTitle = findViewById(R.id.textViewPollTitle);

        mReference = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        questionsList = new ArrayList<>();
        answersList = new ArrayList<>();
        QuestionPollAdapter = new AnswerQuestionsAdapter(questionsList, answersList);

        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewQuestions.setAdapter(QuestionPollAdapter);

        pollId = getIntent().getStringExtra("pollId");
        pollName = getIntent().getStringExtra("pollName");

        textViewPollTitle.setText(pollName);
        if (pollId == null) {
            Toast.makeText(this, "Anket ID'si bulunamadı", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        checkIfPollAlreadySolved();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPoll();
            }
        });
    }

    private void checkIfPollAlreadySolved() {
        mReference.child("Anketler").child(pollId).child("responses").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Kullanıcı daha önce anketi çözmüş, geri döndür ve mesaj göster
                    Toast.makeText(TakePollActivity.this, "Bu anketi daha önce çözdünüz.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TakePollActivity.this, MyCoursesTeacherActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Kullanıcı anketi daha önce çözmemiş, anketi yükle
                    loadPoll();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TakePollActivity.this, "Anket durumu kontrol edilemedi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPoll() {
        mReference.child("Anketler").child(pollId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Poll poll = dataSnapshot.getValue(Poll.class);
                    if (poll != null) {
                        questionsList.clear();
                        for (DataSnapshot questionSnapshot : dataSnapshot.child("questions").getChildren()) {
                            questionsList.add(questionSnapshot.getValue(String.class));
                        }
                        answersList.clear();
                        for (int i = 0; i < questionsList.size(); i++) {
                            answersList.add("");
                        }
                        QuestionPollAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(TakePollActivity.this, "Anket bulunamadı", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TakePollActivity.this, "Anket yüklenemedi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitPoll() {
        Map<String, String> answersMap = new HashMap<>();
        for (int i = 0; i < questionsList.size(); i++) {
            answersMap.put("question" + (i + 1), answersList.get(i));
        }

        mReference.child("Anketler").child(pollId).child("responses").child(userId).setValue(answersMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(TakePollActivity.this, "Anket başarıyla gönderildi.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TakePollActivity.this, MyCoursesTeacherActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(TakePollActivity.this, "Anket gönderilemedi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
