package com.example.courseassistantapplication.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.recyclerview.GroupAdapter;
import com.example.courseassistantapplication.model.Course;
import com.example.courseassistantapplication.model.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddCourseActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private FirebaseUser mUser;
    private EditText course_id, course_name, group_number, course_date;
    private Button create_course_btn;
    private Calendar calendar;
    private RecyclerView recyclerView;
    private GroupAdapter groupAdapter;
    private List<Group> groupList;
    private List<String> instructorEmails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        // Firebase auth and database reference initialization
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mUser = mAuth.getCurrentUser();



        // View bindings
        course_id = findViewById(R.id.course_id);
        course_name = findViewById(R.id.course_name);
        course_date = findViewById(R.id.course_Date);
        group_number = findViewById(R.id.group_number);
        create_course_btn = findViewById(R.id.create_course_btn);
        recyclerView = findViewById(R.id.recycler_view);

        // Initialize Calendar
        calendar = Calendar.getInstance();

        // Initialize RecyclerView
        groupList = new ArrayList<>();
        groupAdapter = new GroupAdapter(groupList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(groupAdapter);

        // Set onClickListener for the date EditText
        course_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddCourseActivity.this, dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // TextWatcher for group_number EditText
        group_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String input = s.toString().trim();
                    if (!input.isEmpty()) {
                        int totalGroups = Integer.parseInt(input);
                        if (totalGroups >= 0) {
                            updateGroupList(totalGroups);
                        }
                    } else {
                        updateGroupList(0);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });



        // Load instructors from Firebase
        loadInstructors();
    }
    public void update(View v) {
        createCourse();
        startActivity(new Intent(AddCourseActivity.this,MyCoursesTeacherActivity.class));
        finish();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd"; // Tarih formatı
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            course_date.setText(sdf.format(calendar.getTime()));
        }
    };

    private void updateGroupList(int totalGroups) {
        groupList.clear();
        for (int i = 0; i < totalGroups; i++) {
            Group group = new Group();
            group.setGroupNumber("Gr " + i);
            groupList.add(group);
        }
        groupAdapter.notifyDataSetChanged();
    }

    private void loadInstructors() {
        mReference.child("Öğretmenler").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                instructorEmails = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String email = snapshot.child("email").getValue(String.class);
                    if (email != null) {
                        instructorEmails.add(email);
                    }
                }
                groupAdapter.setInstructorEmails(instructorEmails);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddCourseActivity.this, "Failed to load instructors.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createCourse() {
        String courseId = course_id.getText().toString().trim();
        String courseName = course_name.getText().toString().trim();
        String courseDateStr = course_date.getText().toString().trim();
        String currentUserEmail = mUser.getEmail();

        if (courseId.isEmpty() || courseName.isEmpty() || courseDateStr.isEmpty() || groupList.isEmpty()) {
            Toast.makeText(this, "Please fill all fields and add at least one group.", Toast.LENGTH_SHORT).show();
            return;
        }
        String currentUserEmailKey = currentUserEmail.replace(".", "_").replace("@", "_at_");

        // Create the course object
        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseName(courseName);
        course.setDate(courseDateStr);
        course.setNumberOfGroups(groupList.size());
        course.setEmailOfInstructor(currentUserEmail);
        course.setCourseGroups(groupList);

        mReference.child("Dersler").child(courseId).setValue(course.toMap()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mReference.child("Öğretmenler").child(currentUserEmailKey).child("Dersler").child(courseId).setValue("Owner");

                for(Group group : groupList) {

                    String instructorEmailKey = group.getInstructorEmail().replace(".", "_").replace("@", "_at_");

                    DatabaseReference teacherCoursesRef = mReference.child("Öğretmenler").child(instructorEmailKey).child("Dersler").child(courseId).child("Grup Numarası");

                    teacherCoursesRef.setValue(group.getGroupNumber()).addOnCompleteListener(teacherTask -> {
                        if (teacherTask.isSuccessful()) {
                            Toast.makeText(AddCourseActivity.this, "Course created successfully.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddCourseActivity.this, "Failed to add course to teacher's list.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } else {
                Toast.makeText(AddCourseActivity.this, "Failed to create course.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
