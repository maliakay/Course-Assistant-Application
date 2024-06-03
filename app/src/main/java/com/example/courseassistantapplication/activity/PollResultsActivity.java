package com.example.courseassistantapplication.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.Poll;
import com.example.courseassistantapplication.model.QuestionPollResult;
import com.example.courseassistantapplication.recyclerview.PollResultsAdapter;
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

public class PollResultsActivity extends AppCompatActivity {

    private static final String TAG = "PollResultsActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;

    private RecyclerView recyclerViewResults;
    private PollResultsAdapter resultsAdapter;
    private List<QuestionPollResult> resultsList;
    private DatabaseReference mReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_results);

        recyclerViewResults = findViewById(R.id.recyclerViewResults);

        mReference = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        resultsList = new ArrayList<>();
        resultsAdapter = new PollResultsAdapter(resultsList, this);

        recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewResults.setAdapter(resultsAdapter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            loadPollResults();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadPollResults();
            } else {
                Toast.makeText(this, "İzin verilmedi. CSV dosyasını oluşturmak için izin gereklidir.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadPollResults() {
        mReference.child("Anketler").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resultsList.clear();
                for (DataSnapshot pollSnapshot : dataSnapshot.getChildren()) {
                    Poll poll = pollSnapshot.getValue(Poll.class);
                    if (poll != null) {
                        String pollTitle = poll.getTitle();
                        List<String> questionsList = new ArrayList<>();
                        for (DataSnapshot questionSnapshot : pollSnapshot.child("questions").getChildren()) {
                            questionsList.add(questionSnapshot.getValue(String.class));
                        }

                        Map<String, Map<String, Integer>> QuestionPollResults = new HashMap<>();

                        for (DataSnapshot responseSnapshot : pollSnapshot.child("responses").getChildren()) {
                            for (DataSnapshot answerSnapshot : responseSnapshot.getChildren()) {
                                String questionKey = answerSnapshot.getKey();
                                int questionIndex = Integer.parseInt(questionKey.replace("question", "")) - 1;
                                String question = questionsList.get(questionIndex);
                                String answer = answerSnapshot.getValue(String.class);

                                if (!QuestionPollResults.containsKey(question)) {
                                    QuestionPollResults.put(question, new HashMap<>());
                                }

                                Map<String, Integer> answerCounts = QuestionPollResults.get(question);
                                if (!answerCounts.containsKey(answer)) {
                                    answerCounts.put(answer, 0);
                                }
                                answerCounts.put(answer, answerCounts.get(answer) + 1);
                            }
                        }

                        List<Map<String, Integer>> answersList = new ArrayList<>();
                        for (String question : questionsList) {
                            answersList.add(QuestionPollResults.getOrDefault(question, new HashMap<>()));
                        }

                        resultsList.add(new QuestionPollResult(pollTitle, questionsList, answersList));
                    }
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

