package com.example.courseassistantapplication.activity;

import android.os.Bundle;
import android.util.Log;
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

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.Question;
import com.example.courseassistantapplication.model.QuestionWithIndex;
import com.example.courseassistantapplication.recyclerview.StudentExamAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewExamActivity extends AppCompatActivity {

    private static final String TAG = "ViewExamActivity";

    private TextView examTitleTextView;
    private Spinner examSpinner;
    private RecyclerView questionsRecyclerView;
    private Button submitExamButton;
    private StudentExamAdapter studentExamAdapter;
    private List<QuestionWithIndex> allQuestions;
    private List<QuestionWithIndex> selectedQuestions;
    private List<String> examTitles;
    private List<String> examIds;
    private List<String> courseIds;

    private DatabaseReference examRef;
    private DatabaseReference studentRef;
    private String selectedExamId;
    private int questionsToShowCount;
    private boolean questionsLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exam);

        examTitleTextView = findViewById(R.id.examTitleTextView);
        examSpinner = findViewById(R.id.examSpinner);
        questionsRecyclerView = findViewById(R.id.questionsRecyclerView);
        submitExamButton = findViewById(R.id.submitExamButton);

        allQuestions = new ArrayList<>();
        selectedQuestions = new ArrayList<>();
        examTitles = new ArrayList<>();
        examIds = new ArrayList<>();
        courseIds = new ArrayList<>();
        studentExamAdapter = new StudentExamAdapter(selectedQuestions);
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionsRecyclerView.setAdapter(studentExamAdapter);

        loadCourses();

        examSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCourseId = courseIds.get(position);
                loadExams(selectedCourseId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        submitExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitExamResponses();
            }
        });
    }

    private void loadCourses() {
        String studentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (studentEmail == null) {
            Toast.makeText(this, "Student email is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        studentRef = FirebaseDatabase.getInstance().getReference("Öğrenciler");
        String studentKey = studentEmail.replace(".", "_").replace("@", "_at_");
        studentRef.child(studentKey).child("Kayıtlı Dersler").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot courseSnapshot : snapshot.getChildren()) {
                    String courseId = courseSnapshot.getKey();
                    if (courseId != null) {
                        courseIds.add(courseId);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewExamActivity.this, android.R.layout.simple_spinner_item, courseIds);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                examSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewExamActivity.this, "Failed to load courses", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadExams(String courseId) {
        examRef = FirebaseDatabase.getInstance().getReference("Exams");
        examRef.orderByChild("Course").equalTo(courseId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                examTitles.clear();
                examIds.clear();
                for (DataSnapshot examSnapshot : snapshot.getChildren()) {
                    String title = examSnapshot.child("title").getValue(String.class);
                    String examId = examSnapshot.getKey();
                    if (title != null && examId != null) {
                        examTitles.add(title);
                        examIds.add(examId);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewExamActivity.this, android.R.layout.simple_spinner_item, examTitles);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                examSpinner.setAdapter(adapter);

                // İlk sınavı yükleyin
                if (!examIds.isEmpty()) {
                    selectedExamId = examIds.get(0); // Update selectedExamId
                    loadExamData(selectedExamId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewExamActivity.this, "Failed to load exams", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadExamData(String examId) {
        selectedExamId = examId; // Set selectedExamId here
        examRef.child(examId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String examTitle = snapshot.child("title").getValue(String.class);
                examTitleTextView.setText(examTitle);
                questionsToShowCount = snapshot.child("questionsToShowCount").getValue(Integer.class);

                allQuestions.clear(); // Clear the all questions list

                for (DataSnapshot questionSnapshot : snapshot.child("questions").getChildren()) {
                    String questionText = questionSnapshot.child("questionText").getValue(String.class);
                    Map<String, String> optionsMap = (Map<String, String>) questionSnapshot.child("options").getValue();

                    if (questionText != null && optionsMap != null) {
                        Question question = new Question();
                        question.setQuestionText(questionText);
                        question.setOptions(optionsMap);

                        String firebaseKey = questionSnapshot.getKey(); // Get the Firebase key
                        allQuestions.add(new QuestionWithIndex(question, firebaseKey)); // Add the question with its key
                    }
                }

                Log.d(TAG, "Total questions loaded: " + allQuestions.size());

                // Rastgele soruları sadece bir kez seç
                if (!questionsLoaded) {
                    Collections.shuffle(allQuestions);
                    selectedQuestions.clear(); // Clear selected questions list here

                    if (allQuestions.size() > questionsToShowCount) {
                        selectedQuestions.addAll(allQuestions.subList(0, questionsToShowCount)); // Get only the required number of questions
                    } else {
                        selectedQuestions.addAll(allQuestions);
                    }
                    questionsLoaded = true; // Set the flag
                }

                Log.d(TAG, "Total selected questions: " + selectedQuestions.size());

                studentExamAdapter.notifyDataSetChanged();
                questionsRecyclerView.setVisibility(View.VISIBLE);
                submitExamButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewExamActivity.this, "Failed to load exam data", Toast.LENGTH_SHORT).show();
            }
        });
    }





    private void submitExamResponses() {
        if (selectedExamId == null) {
            Toast.makeText(ViewExamActivity.this, "Selected exam ID is null", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "UserID: " + userId);
        Log.d(TAG, "Selected Exam ID: " + selectedExamId);

        DatabaseReference responsesRef = examRef.child(selectedExamId).child("responses").child(userId);
        responsesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(ViewExamActivity.this, "You have already submitted this exam", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> responsesMap = new HashMap<>();

                    for (int i = 0; i < questionsRecyclerView.getChildCount(); i++) {
                        View questionView = questionsRecyclerView.getChildAt(i);
                        RadioGroup answersGroup = questionView.findViewById(R.id.answersGroup);
                        int selectedAnswerId = answersGroup.getCheckedRadioButtonId();
                        if (selectedAnswerId != -1) { // Ensure an answer is selected
                            RadioButton selectedAnswer = questionView.findViewById(selectedAnswerId);
                            String questionKey = selectedQuestions.get(i).getFirebaseKey(); // Use the original Firebase key
                            responsesMap.put(questionKey, selectedAnswer.getText().toString());
                            Log.d(TAG, "Saving response: " + questionKey + " = " + selectedAnswer.getText().toString());
                        } else {
                            Toast.makeText(ViewExamActivity.this, "Please answer all questions", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    responsesRef.setValue(responsesMap)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ViewExamActivity.this, "Responses saved successfully", Toast.LENGTH_SHORT).show();
                                    finish(); // Close the activity after saving
                                } else {
                                    Log.e(TAG, "Error saving responses: ", task.getException());
                                    Toast.makeText(ViewExamActivity.this, "Failed to save responses", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error checking responses: ", databaseError.toException());
                Toast.makeText(ViewExamActivity.this, "Error checking responses", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
