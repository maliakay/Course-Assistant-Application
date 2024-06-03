package com.example.courseassistantapplication.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.Course;
import com.example.courseassistantapplication.model.Poll;
import com.example.courseassistantapplication.recyclerview.QuestionPollAdapter;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddPollsActivity extends AppCompatActivity {

    private EditText editTextTitle,editTextNumQuestions;
    private TextView coursePollId;
    private RecyclerView recyclerViewQuestions;
    private Button buttonGenerateQuestions,buttonSubmitPoll;
    private QuestionPollAdapter QuestionPollAdapter;
    private List<String> questionsList;
    private List<String> courseList;
    private DatabaseReference mReference;
    String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_polls);

        coursePollId = findViewById(R.id.coursePollId);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextNumQuestions = findViewById(R.id.editTextNumQuestions);
        recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions);
        buttonGenerateQuestions = findViewById(R.id.buttonGenerateQuestions);
        buttonSubmitPoll = findViewById(R.id.buttonSubmitPoll);
        courseId = getIntent().getStringExtra("courseId");

        mReference = FirebaseDatabase.getInstance().getReference();
        questionsList = new ArrayList<>();
        courseList = new ArrayList<>();
        QuestionPollAdapter = new QuestionPollAdapter(questionsList);

        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewQuestions.setAdapter(QuestionPollAdapter);

        coursePollId.setText(courseId);

        buttonGenerateQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQuestionsFields();
            }
        });

        buttonSubmitPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPoll();
            }
        });

    }

    /*private void loadCourses(){
        //Dersleri getir Öğretmenin kayıtlı olduğu
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String currentUserEmailKey = currentUserEmail.replace(".", "_").replace("@", "_at_");
        mReference.child("Öğretmenler").child(currentUserEmailKey).child("Dersler").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseList.clear();
                for(DataSnapshot courseSnapshot:snapshot.getChildren()){
                    String courseId = courseSnapshot.getKey();
                    courseList.add(courseId);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddPollsActivity.this, android.R.layout.simple_spinner_item,courseList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCourses.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddPollsActivity.this, "Lessons could not be loaded: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private void generateQuestionsFields(){
        String numQuestionStr = editTextNumQuestions.getText().toString().trim();
        if(!TextUtils.isEmpty(numQuestionStr)){
            int numQuestions = Integer.parseInt(numQuestionStr);
            questionsList.clear();
            for(int i =0;i<numQuestions;i++){
                questionsList.add("");
            }
            QuestionPollAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(this, "Please Enter the number of questions ", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitPoll(){
        String course = courseId;
        String title = editTextTitle.getText().toString().trim();

        if (TextUtils.isEmpty(course) || TextUtils.isEmpty(title) || questionsList.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            return;
        }
        String creatorMail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String pollId = mReference.child("Anketler").push().getKey();
        Poll poll = new Poll(pollId,creatorMail,course,title,questionsList);
        poll.setPollId(pollId);
        poll.setCreatorMail(creatorMail);
        poll.setCourse(course);
        poll.setTitle(title);
        poll.setQuestions(questionsList);

        if(pollId != null){
            mReference.child("Anketler").child(pollId).setValue(poll.toMap()).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(AddPollsActivity.this,"Poll created successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(AddPollsActivity.this,"The Poll was not created successfully" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}