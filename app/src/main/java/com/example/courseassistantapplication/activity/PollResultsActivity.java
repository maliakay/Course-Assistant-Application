package com.example.courseassistantapplication.activity;

// PollResultsActivity.java
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.QuestionResult;
import com.example.courseassistantapplication.recyclerview.PollResultsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollResultsActivity extends AppCompatActivity {

    private TextView textViewPollTitle;
    private RecyclerView recyclerViewResults;
    private PollResultsAdapter resultsAdapter;
    private List<QuestionResult> resultsList;
    private String pollId;

    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_results);

        textViewPollTitle = findViewById(R.id.textViewPollTitle);
        recyclerViewResults = findViewById(R.id.recyclerViewResults);

        mReference = FirebaseDatabase.getInstance().getReference();
        resultsList = new ArrayList<>();
        resultsAdapter = new PollResultsAdapter(resultsList);

        recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewResults.setAdapter(resultsAdapter);

       // pollId = getIntent().getStringExtra("pollId");
        pollId = "-NzNvtqAq8EFOXKdINqw";
        if (pollId == null) {
            Toast.makeText(this, "Anket ID'si bulunamadı", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadPollResults();
    }

    private void loadPollResults() {
        mReference.child("Anketler").child(pollId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String title = dataSnapshot.child("title").getValue(String.class);
                    textViewPollTitle.setText(title);

                    DataSnapshot responsesSnapshot = dataSnapshot.child("responses");
                    Map<String, Map<String, Integer>> questionResults = new HashMap<>();

                    for (DataSnapshot responseSnapshot : responsesSnapshot.getChildren()) {
                        for (DataSnapshot questionSnapshot : responseSnapshot.getChildren()) {
                            String question = questionSnapshot.getKey();
                            String answer = questionSnapshot.getValue(String.class);

                            if (!questionResults.containsKey(question)) {
                                questionResults.put(question, new HashMap<>());
                            }

                            Map<String, Integer> answerCounts = questionResults.get(question);
                            if (!answerCounts.containsKey(answer)) {
                                answerCounts.put(answer, 0);
                            }
                            answerCounts.put(answer, answerCounts.get(answer) + 1);
                        }
                    }

                    resultsList.clear();
                    for (Map.Entry<String, Map<String, Integer>> entry : questionResults.entrySet()) {
                        String question = entry.getKey();
                        Map<String, Integer> answerCounts = entry.getValue();
                        resultsList.add(new QuestionResult(question, answerCounts));
                    }

                    resultsAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PollResultsActivity.this, "Anket bulunamadı", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PollResultsActivity.this, "Anket sonuçları yüklenemedi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
