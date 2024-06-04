package com.example.courseassistantapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
//Öğrenci de Öğretmen de tek aktiviteden dersleri görüyor. Dosya ismi güncellencek
//Eğitmenler dersi düzenleyip hali hazırda olan kaydı güncellemeli.
//Öğrenciler derse katılma durumu değerlendirilmeli
//kurgusal olarak değişiklik yapılabilr. eycii
public class MyCoursesTeacherActivity extends AppCompatActivity {
    // EĞİTMENLERİN KURSLARINI GÖRÜNTÜLEDİĞİ YER
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private Button addCourseBtn,showPollsBtn, showExamBtn;

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
        mUser = mAuth.getCurrentUser();

        // View bindings
        addCourseBtn = findViewById(R.id.add_course);
        showPollsBtn = findViewById(R.id.show_polls);
        recyclerView = findViewById(R.id.recycler_view);
        showExamBtn = findViewById(R.id.show_exam);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseList = new ArrayList<>();
        adapter = new CourseGroupAdapter(courseList, this, mUser);
        recyclerView.setAdapter(adapter);

        // Check if the user's email ends with "@std.yildiz.edu.tr"
        if (mUser != null) {
            String currentUserEmail = mUser.getEmail();
            if (currentUserEmail != null && currentUserEmail.endsWith("@std.yildiz.edu.tr")) {
                addCourseBtn.setVisibility(View.GONE);

                loadStudentCourses(currentUserEmail);
            }else{
                loadTeacherCourses();
                showPollsBtn.setVisibility(View.GONE);
                showExamBtn.setVisibility(View.GONE);
            }
        } else {
            // User is not logged in, redirect to login activity
            Toast.makeText(this, "User not logged in. Redirecting to login...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MyCoursesTeacherActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Load teacher's courses from Firebase
    }

    public void addCourse(View view) {
        startActivity(new Intent(MyCoursesTeacherActivity.this, AddCourseActivity.class));
        finish();
    }
    public void showPolls(View view){
        startActivity(new Intent(MyCoursesTeacherActivity.this, ShowPollsStdActivity.class));
    }

    public void showExam(View view){
        startActivity(new Intent(MyCoursesTeacherActivity.this, ViewExamActivity.class));
    }

    private void loadTeacherCourses() {
        if (mUser == null) {
            Toast.makeText(MyCoursesTeacherActivity.this, "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUserEmail = mUser.getEmail();
        if (currentUserEmail == null) {
            Toast.makeText(MyCoursesTeacherActivity.this, "Email not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        mReference.child("Dersler").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseList.clear();
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    Course course = courseSnapshot.getValue(Course.class);
                    if (course != null && course.getCourseGroups() != null) {
                        if (course.getEmailOfInstructor().equals(currentUserEmail)) {
                            courseList.add(course);
                        } else {
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
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyCoursesTeacherActivity.this, "Failed to load courses.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadStudentCourses(String studentEmail) {
        mReference.child("Dersler").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseList.clear();
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    Course course = courseSnapshot.getValue(Course.class);
                    if (course != null && course.getCourseGroups() != null) {
                        for (DataSnapshot groupSnapshot : courseSnapshot.child("courseGroups").getChildren()) {
                            for (DataSnapshot studentSnapshot : groupSnapshot.child("Kayıtlı Öğrenciler").getChildren()) {
                                String registeredStudentEmail = studentSnapshot.getValue(String.class);
                                if (registeredStudentEmail.equals(studentEmail)) {
                                    courseList.add(course);
                                    break;
                                }
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyCoursesTeacherActivity.this, "Failed to load student courses.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
