package com.example.courseassistantapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddStudentActivity extends AppCompatActivity {

    private ListView listViewStudents;
    private Button btnAddSelectedStudents;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> studentEmails;
    private ArrayList<Student> studentList;
    private DatabaseReference mReference;
    private String courseId,groupNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        courseId = getIntent().getStringExtra("courseId");
        groupNumber = getIntent().getStringExtra("groupNumber");
        mReference = FirebaseDatabase.getInstance().getReference();

        listViewStudents = findViewById(R.id.list_view_students);
        btnAddSelectedStudents = findViewById(R.id.btn_add_selected_students);
        studentEmails = new ArrayList<>();
        studentList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, studentEmails);
        listViewStudents.setAdapter(adapter);
        listViewStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        loadStudents();

        btnAddSelectedStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSelectedStudentsToCourse();
            }
        });
    }

    private void loadStudents() {
        mReference.child("Öğrenciler").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear();
                studentEmails.clear();
                for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
                    Student student = studentSnapshot.getValue(Student.class);
                    if (student != null) {
                        studentList.add(student);
                        studentEmails.add(student.getEmail());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddStudentActivity.this, "Failed to load students.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addSelectedStudentsToCourse() {

        mReference.child("Dersler").child(courseId).child("courseGroups")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                            String currentGroupNumber = groupSnapshot.child("groupNumber").getValue(String.class);
                            if (currentGroupNumber != null && currentGroupNumber.equals(groupNumber)) {
                                List<String> studentEmailsList = new ArrayList<>();
                                for (int i = 0; i < listViewStudents.getCount(); i++) {
                                    if (listViewStudents.isItemChecked(i)) {
                                        String email = studentEmails.get(i);
                                        studentEmailsList.add(email);
                                        addCourseToStudent(email, courseId);  // Öğrencinin kayıtlı derslerine ekleme fonksiyonu
                                    }
                                }
                                groupSnapshot.getRef().child("Kayıtlı Öğrenciler").setValue(studentEmailsList);
                                Toast.makeText(AddStudentActivity.this, "Students added to the group.", Toast.LENGTH_SHORT).show();
                                finish();
                                return;
                            }
                        }
                        Toast.makeText(AddStudentActivity.this, "Group not found.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(AddStudentActivity.this, "Failed to add students.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addCourseToStudent(String email, String courseId) {
        // Email'i Firebase'de depolanan formata dönüştürme
        String formattedEmail = email.replace(".", "_").replace("@", "_at_");

        // Öğrencinin kayıtlı derslerine kurs id'sini ekleme
        mReference.child("Öğrenciler").child(formattedEmail).child("Kayıtlı Dersler").child(courseId).setValue(true);
    }

}