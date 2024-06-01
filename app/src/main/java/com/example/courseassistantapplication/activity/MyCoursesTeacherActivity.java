package com.example.courseassistantapplication.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.recyclerview.CourseGroupAdapter;
import com.example.courseassistantapplication.model.Course;
import com.example.courseassistantapplication.model.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyCoursesTeacherActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private FirebaseUser mUser;
    private RecyclerView recyclerView;
    private CourseGroupAdapter adapter;
    private List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses_teacher);

        // Firebase auth and database reference initialization
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseList = new ArrayList<>();
        adapter = new CourseGroupAdapter(courseList);
        recyclerView.setAdapter(adapter);

        // Load teacher's courses from Firebase
        loadTeacherCourses();
    }

    private void loadTeacherCourses() {
        String currentUserEmail = "eray@yildiz.edu.tr";

        mReference.child("Dersler").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseList.clear();
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    Course course = courseSnapshot.getValue(Course.class);
                    if (course != null && course.getCourseGroups() != null) {
                        List<Group> filteredGroups = new ArrayList<>();
                        for (DataSnapshot groupSnapshot : courseSnapshot.child("courseGroups").getChildren()) {
                            Group group = groupSnapshot.getValue(Group.class);
                            if (group != null && group.getInstructorEmail().equals(currentUserEmail)) {
                                filteredGroups.add(group);
                            }
                        }
                        if (!filteredGroups.isEmpty()) {
                            course.setCourseGroups(filteredGroups);
                            courseList.add(course);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyCoursesTeacherActivity.this, "Failed to load courses.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}