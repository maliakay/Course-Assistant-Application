package com.example.courseassistantapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.model.Question;
import com.example.courseassistantapplication.recyclerview.StudentExamAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.courseassistantapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewExamActivity extends AppCompatActivity {

    private TextView examTitleTextView;
    private RecyclerView questionsRecyclerView;
    private Button submitExamButton;
    private StudentExamAdapter studentExamAdapter;
    private List<Question> questionsList;

    private Spinner spinnerCourses;

    private List<String> courseList;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exam);

        examTitleTextView = findViewById(R.id.examTitle);
        questionsRecyclerView = findViewById(R.id.questionsRecyclerView);
        submitExamButton = findViewById(R.id.submitExamButton);

        mReference = FirebaseDatabase.getInstance().getReference();
        spinnerCourses = findViewById(R.id.spinnerCourses);

        questionsList = new ArrayList<>();
        courseList = new ArrayList<>();
        studentExamAdapter = new StudentExamAdapter(questionsList);
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionsRecyclerView.setAdapter(studentExamAdapter);

        loadCourses();
        spinnerCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadExamData(spinnerCourses.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        submitExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submitExamButton.isEnabled()) {
                    submitExamResponses();
                }
            }
        });
    }

    private void loadCourses() {
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String currentUserEmailKey = currentUserEmail.replace(".", "_").replace("@", "_at_");

        mReference.child("Öğrenciler").child(currentUserEmailKey).child("Kayıtlı Dersler").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseList.clear();
                for (DataSnapshot courseSnapshot : snapshot.getChildren()) {
                    String courseId = courseSnapshot.getKey();
                    courseList.add(courseId);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewExamActivity.this, android.R.layout.simple_spinner_item, courseList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCourses.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewExamActivity.this, "Lessons could not be loaded: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadExamData(String selectedCourse) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mReference.child("Exams").orderByChild("Course").equalTo(selectedCourse).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                questionsList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot examSnapshot : snapshot.getChildren()) {
                        String examTitle = examSnapshot.child("title").getValue(String.class);
                        examTitleTextView.setText(examTitle);

                        // Check if the student has already submitted responses
                        if (examSnapshot.child("responses").hasChild(userId)) {
                            Toast.makeText(ViewExamActivity.this, "You have already taken this exam.", Toast.LENGTH_SHORT).show();
                            submitExamButton.setEnabled(false);
                            return;
                        }

                        for (DataSnapshot questionSnapshot : examSnapshot.child("questions").getChildren()) {
                            Question question = questionSnapshot.getValue(Question.class);
                            questionsList.add(question);
                        }
                    }
                    studentExamAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ViewExamActivity.this, "No exams found for the selected course.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewExamActivity.this, "Exams could not be loaded: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitExamResponses() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> responsesMap = new HashMap<>();

        for (int i = 0; i < questionsRecyclerView.getChildCount(); i++) {
            View questionView = questionsRecyclerView.getChildAt(i);
            RadioGroup answersGroup = questionView.findViewById(R.id.answersGroup);
            int selectedAnswerId = answersGroup.getCheckedRadioButtonId();
            RadioButton selectedAnswer = questionView.findViewById(selectedAnswerId);

            responsesMap.put("question" + (i + 1), selectedAnswer.getText().toString());
        }

        String selectedCourse = spinnerCourses.getSelectedItem().toString();
        mReference.child("Exams").orderByChild("Course").equalTo(selectedCourse).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot examSnapshot : snapshot.getChildren()) {
                    examSnapshot.getRef().child("responses").child(userId).setValue(responsesMap);
                }
                Toast.makeText(ViewExamActivity.this, "Responses submitted successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewExamActivity.this, "Failed to submit responses: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
