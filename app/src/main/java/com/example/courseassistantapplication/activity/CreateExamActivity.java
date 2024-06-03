package com.example.courseassistantapplication.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.recyclerview.ExamQuestionAdapter;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.courseassistantapplication.R;

import java.util.HashMap;
import java.util.Map;

public class CreateExamActivity extends AppCompatActivity {

    private EditText examTitleEditText, questionCountEditText;
    private Button generateQuestionsButton, saveExamButton;
    private RecyclerView questionsRecyclerView;
    private ExamQuestionAdapter examQuestionAdapter;
    private int questionCount;

    private String courseId;

    private DatabaseReference mUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exam);

        examTitleEditText = findViewById(R.id.examTitle);
        questionCountEditText = findViewById(R.id.questionCount);
        generateQuestionsButton = findViewById(R.id.generateQuestionsButton);
        saveExamButton = findViewById(R.id.saveExamButton);
        questionsRecyclerView = findViewById(R.id.questionsRecyclerView);
        mUser = FirebaseDatabase.getInstance().getReference();
        courseId = getIntent().getStringExtra("courseId");

        generateQuestionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(questionCountEditText.getText().toString())) {
                    questionCount = Integer.parseInt(questionCountEditText.getText().toString());
                    examQuestionAdapter = new ExamQuestionAdapter(questionCount);
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
        examData.put("questionCount", questionCount);
        examData.put("CreaterMail",currentEmail);
        examData.put("Course",courseId);

        Map<String, Object> questionsMap = new HashMap<>();
        for (int i = 0; i < questionCount; i++) {
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
        examsRef.setValue(examData);
    }
}
