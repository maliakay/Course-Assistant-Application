package com.example.courseassistantapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.recyclerview.OptionCountAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewExamResponsesActivity extends AppCompatActivity {

    private Spinner examSpinner;
    private TextView examTitleTextView;
    private RecyclerView responsesRecyclerView;
    private OptionCountAdapter optionCountAdapter;
    private List<String> options;
    private Map<String, Integer> optionCounts;

    private List<String> examIds;
    private List<String> examTitles;

    private DatabaseReference examRef;
    private static final String TAG = "ViewExamResponsesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exam_responses);

        examSpinner = findViewById(R.id.examSpinner);
        examTitleTextView = findViewById(R.id.examTitle);
        responsesRecyclerView = findViewById(R.id.responsesRecyclerView);

        examRef = FirebaseDatabase.getInstance().getReference("Exams");

        options = new ArrayList<>();
        optionCounts = new HashMap<>();
        examIds = new ArrayList<>();
        examTitles = new ArrayList<>();

        optionCountAdapter = new OptionCountAdapter(options, optionCounts);
        responsesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        responsesRecyclerView.setAdapter(optionCountAdapter);

        loadExams();

        examSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedExamId = examIds.get(position);
                loadExamData(selectedExamId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void loadExams() {
        examRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                examIds.clear();
                examTitles.clear();
                for (DataSnapshot examSnapshot : snapshot.getChildren()) {
                    String examId = examSnapshot.getKey();
                    String examTitle = examSnapshot.child("title").getValue(String.class);
                    examIds.add(examId);
                    examTitles.add(examTitle);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewExamResponsesActivity.this, android.R.layout.simple_spinner_item, examTitles);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                examSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewExamResponsesActivity.this, "Failed to load exams: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadExamData(String examId) {
        examRef.child(examId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String examTitle = snapshot.child("title").getValue(String.class);
                    examTitleTextView.setText(examTitle);

                    optionCounts.clear();
                    options.clear();

                    for (DataSnapshot responseSnapshot : snapshot.child("responses").getChildren()) {
                        for (DataSnapshot answerSnapshot : responseSnapshot.getChildren()) {
                            String questionKey = answerSnapshot.getKey();
                            String answer = answerSnapshot.getValue(String.class);
                            String optionKey = questionKey + " - " + answer;
                            if (!optionCounts.containsKey(optionKey)) {
                                optionCounts.put(optionKey, 0);
                                options.add(optionKey);
                            }
                            optionCounts.put(optionKey, optionCounts.get(optionKey) + 1);
                        }
                    }

                    optionCountAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ViewExamResponsesActivity.this, "Exam data does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewExamResponsesActivity.this, "Exams could not be loaded: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
