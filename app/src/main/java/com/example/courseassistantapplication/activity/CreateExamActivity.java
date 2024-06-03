package com.example.courseassistantapplication.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.recyclerview.ExamQuestionAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CreateExamActivity extends AppCompatActivity {

    private EditText examTitleEditText, questionPoolCountEditText, questionsToShowCountEditText;
    private Button generateQuestionsButton, saveExamButton;
    private RecyclerView questionsRecyclerView;
    private ExamQuestionAdapter examQuestionAdapter;
    private int questionPoolCount, questionsToShowCount;

    private String courseId;

    private DatabaseReference mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exam);

        examTitleEditText = findViewById(R.id.examTitle);
        questionPoolCountEditText = findViewById(R.id.questionPoolCount);
        questionsToShowCountEditText = findViewById(R.id.questionsToShowCount);
        generateQuestionsButton = findViewById(R.id.generateQuestionsButton);
        saveExamButton = findViewById(R.id.saveExamButton);
        questionsRecyclerView = findViewById(R.id.questionsRecyclerView);
        mUser = FirebaseDatabase.getInstance().getReference();
        courseId = getIntent().getStringExtra("courseId");

        generateQuestionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(questionPoolCountEditText.getText().toString()) && !TextUtils.isEmpty(questionsToShowCountEditText.getText().toString())) {
                    questionPoolCount = Integer.parseInt(questionPoolCountEditText.getText().toString());
                    questionsToShowCount = Integer.parseInt(questionsToShowCountEditText.getText().toString());

                    if (questionsToShowCount > questionPoolCount) {
                        Toast.makeText(CreateExamActivity.this, "Questions to show cannot be more than the question pool count", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    examQuestionAdapter = new ExamQuestionAdapter(questionPoolCount);
                    questionsRecyclerView.setLayoutManager(new LinearLayoutManager(CreateExamActivity.this));
                    questionsRecyclerView.setAdapter(examQuestionAdapter);
                    questionsRecyclerView.setVisibility(View.VISIBLE);
                    saveExamButton.setVisibility(View.VISIBLE);
                }
            }
        });

        saveExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExamToFirebase();
            }
        });
    }

    private void saveExamToFirebase() {
        String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String examTitle = examTitleEditText.getText().toString();
        DatabaseReference examsRef = FirebaseDatabase.getInstance().getReference("Exams").push();

        Map<String, Object> examData = new HashMap<>();
        examData.put("title", examTitle);
        examData.put("questionPoolCount", questionPoolCount);
        examData.put("questionsToShowCount", questionsToShowCount);
        examData.put("CreaterMail", currentEmail);
        examData.put("Course", courseId);

        Map<String, Object> questionsMap = new HashMap<>();
        for (int i = 0; i < questionPoolCount; i++) {
            View questionView = questionsRecyclerView.getChildAt(i);
            EditText questionText = questionView.findViewById(R.id.questionText);
            EditText[] answerOptions = new EditText[5];
            answerOptions[0] = questionView.findViewById(R.id.answerOptionA);
            answerOptions[1] = questionView.findViewById(R.id.answerOptionB);
            answerOptions[2] = questionView.findViewById(R.id.answerOptionC);
            answerOptions[3] = questionView.findViewById(R.id.answerOptionD);
            answerOptions[4] = questionView.findViewById(R.id.answerOptionE);

            Map<String, String> optionsMap = new HashMap<>();
            optionsMap.put("A", answerOptions[0].getText().toString());
            optionsMap.put("B", answerOptions[1].getText().toString());
            optionsMap.put("C", answerOptions[2].getText().toString());
            optionsMap.put("D", answerOptions[3].getText().toString());
            optionsMap.put("E", answerOptions[4].getText().toString());

            Map<String, Object> questionMap = new HashMap<>();
            questionMap.put("questionText", questionText.getText().toString());
            questionMap.put("options", optionsMap);

            questionsMap.put("question" + (i + 1), questionMap);
        }

        examData.put("questions", questionsMap);
        examsRef.setValue(examData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CreateExamActivity.this, "Exam saved successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity after saving
                    } else {
                        Toast.makeText(CreateExamActivity.this, "Failed to save exam", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
