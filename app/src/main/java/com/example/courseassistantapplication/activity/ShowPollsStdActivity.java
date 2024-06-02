package com.example.courseassistantapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.example.courseassistantapplication.model.Poll;
import com.example.courseassistantapplication.recyclerview.PollsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowPollsStdActivity extends AppCompatActivity {
    private Spinner spinnerCourses;
    private RecyclerView recyclerViewPolls;
    private PollsAdapter pollsAdapter;
    private List<Poll> pollsList;
    private List<String> courseList;
    private DatabaseReference mReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_polls_std);

        spinnerCourses = findViewById(R.id.spinnerCourses);
        recyclerViewPolls = findViewById(R.id.recyclerViewPolls);

        mReference = FirebaseDatabase.getInstance().getReference();
        pollsList = new ArrayList<>();
        courseList = new ArrayList<>();
        pollsAdapter = new PollsAdapter(pollsList);

        recyclerViewPolls.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPolls.setAdapter(pollsAdapter);

        loadCourses();

        spinnerCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadPollsForCourse(spinnerCourses.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    private void loadCourses(){
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String currentUserEmailKey = currentUserEmail.replace(".", "_").replace("@", "_at_");

        mReference.child("Öğrenciler").child(currentUserEmailKey).child("Kayıtlı Dersler").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseList.clear();
                for(DataSnapshot courseSnapshot : snapshot.getChildren()){
                    String courseId = courseSnapshot.getKey();
                    courseList.add(courseId);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ShowPollsStdActivity.this, android.R.layout.simple_spinner_item, courseList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCourses.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowPollsStdActivity.this, "Lessons could not be loaded: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadPollsForCourse(String course){
        mReference.child("Anketler").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pollsList.clear();
                for(DataSnapshot pollSnapshot: snapshot.getChildren()){
                    if(pollSnapshot.child("course").getValue(String.class).equals(course)){
                        Poll poll = pollSnapshot.getValue(Poll.class);
                        poll.setPollId(pollSnapshot.getKey()); // Poll ID'sini ayarlayın
                        pollsList.add(poll);
                    }
                }
                pollsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowPollsStdActivity.this, "Polls failed to load " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}