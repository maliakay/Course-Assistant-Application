package com.example.courseassistantapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.Course;
import com.example.courseassistantapplication.model.Group;
import com.example.courseassistantapplication.recyclerview.ClassNameAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewClassesActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;

    private FirebaseUser mUser;
    private RecyclerView recyclerView;
    private ClassNameAdapter adapter;
    private List<Course> courseList;

    private List<Group> groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_classes);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mUser = mAuth.getCurrentUser();

        recyclerView = findViewById(R.id.rwClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseList = new ArrayList<>();
        groupList= new ArrayList<>();
        adapter = new ClassNameAdapter(courseList, this, mUser);
        recyclerView.setAdapter(adapter);


        if (mUser != null) {
            String currentUserEmail = mUser.getEmail();
            if (currentUserEmail != null && currentUserEmail.endsWith("@std.yildiz.edu.tr")) {
                loadStudentCourses(currentUserEmail);
            } else {
                loadTeacherCourses();
            }
        } else {
            // User is not logged in, redirect to login activity
            Toast.makeText(this, "User not logged in. Redirecting to login...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ViewClassesActivity.this, LoginActivity.class));
        }
    }

    public void loadStudentCourses(String currentUserEmail) {
        mReference.child("Dersler").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseList.clear();
                for (DataSnapshot courseSnapshot : snapshot.getChildren()) {
                    Course course = courseSnapshot.getValue(Course.class);
                    for (DataSnapshot groupSnapshot : courseSnapshot.child("courseGroups").getChildren()) {
                        for (DataSnapshot studentSnapshot : groupSnapshot.child("Kayıtlı Öğrenciler").getChildren()) {
                            String registeredStudentEmail = studentSnapshot.getValue(String.class);
                            if (registeredStudentEmail.equals(currentUserEmail)) {
                                courseList.add(course);
                                break;
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewClassesActivity.this, "Failed to load student courses.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTeacherCourses() {
        if (mUser == null) {
            Toast.makeText(ViewClassesActivity.this, "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUserEmail = mUser.getEmail();
        if (currentUserEmail == null) {
            Toast.makeText(ViewClassesActivity.this, "Email not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        mReference.child("Dersler").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseList.clear();
                groupList.clear();
                for (DataSnapshot courseSnapshot : snapshot.getChildren()) {
                    Course course = courseSnapshot.getValue(Course.class);
                    if (course != null) {
                        if (course.getEmailOfInstructor().equals(currentUserEmail)) {
                            courseList.add(course);
                        } else {
                            for (DataSnapshot groupSnapshot : courseSnapshot.child("courseGroups").getChildren()) {
                                Group group = groupSnapshot.getValue(Group.class);
                                if (group != null && group.getInstructorEmail().equals(currentUserEmail)) {
                                    groupList.add(group);
                                }
                            }
                            if (!groupList.isEmpty()) {
                                course.setCourseGroups(groupList);
                                courseList.add(course);
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewClassesActivity.this, "Failed to load courses.", Toast.LENGTH_SHORT).show();
            }
        });

    }}

