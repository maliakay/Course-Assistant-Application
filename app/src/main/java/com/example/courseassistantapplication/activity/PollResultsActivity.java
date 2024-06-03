package com.example.courseassistantapplication.activity;

import android.os.Bundle;
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

    private RecyclerView recyclerViewResults;
    private PollResultsAdapter resultsAdapter;
    private List<QuestionResult> resultsList;
    private DatabaseReference mReference;
    private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_results);

        recyclerViewResults = findViewById(R.id.recyclerViewResults);

        mReference = FirebaseDatabase.getInstance().getReference();
        resultsList = new ArrayList<>();
        resultsAdapter = new PollResultsAdapter(resultsList);

        recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewResults.setAdapter(resultsAdapter);

        courseId = getIntent().getStringExtra("courseId");
        if (courseId == null) {
            Toast.makeText(this, "Ders ID'si bulunamadı", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadPollResults();
    }

    private void loadPollResults() {
        mReference.child("Anketler").orderByChild("course").equalTo(courseId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resultsList.clear();
                for (DataSnapshot pollSnapshot : dataSnapshot.getChildren()) {
                    String pollTitle = pollSnapshot.child("title").getValue(String.class);
                    List<String> questionsList = new ArrayList<>();
                    for (DataSnapshot questionSnapshot : pollSnapshot.child("questions").getChildren()) {
                        questionsList.add(questionSnapshot.getValue(String.class));
                    }

                    Map<String, Map<String, Integer>> questionResults = new HashMap<>();
                    List<Map<String, Integer>> answersList = new ArrayList<>();

                    for (DataSnapshot responseSnapshot : pollSnapshot.child("responses").getChildren()) {
                        for (DataSnapshot answerSnapshot : responseSnapshot.getChildren()) {
                            String questionKey = answerSnapshot.getKey();
                            int questionIndex = Integer.parseInt(questionKey.replace("question", "")) - 1;
                            String question = questionsList.get(questionIndex);
                            String answer = answerSnapshot.getValue(String.class);

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

                    for (Map.Entry<String, Map<String, Integer>> entry : questionResults.entrySet()) {
                        answersList.add(entry.getValue());
                    }

                    resultsList.add(new QuestionResult(pollTitle, questionsList, answersList));
                }
                resultsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PollResultsActivity.this, "Anket sonuçları yüklenemedi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
