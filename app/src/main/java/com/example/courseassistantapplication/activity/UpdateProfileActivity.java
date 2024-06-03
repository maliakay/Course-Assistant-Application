package com.example.courseassistantapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.Student;
import com.example.courseassistantapplication.model.Instructor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfileActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mReference;

    EditText name, surname, studentID, phoneNumber, education;
    TextView update_studentID_label, update_education_label, user;
    private boolean isStudent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        name = findViewById(R.id.update_name);
        user = findViewById(R.id.update_user);
        surname = findViewById(R.id.update_surname);
        studentID = findViewById(R.id.update_studentID);
        phoneNumber = findViewById(R.id.update_phoneNumber);
        education = findViewById(R.id.update_education);
        update_education_label = findViewById(R.id.update_education_label);
        update_studentID_label = findViewById(R.id.update_studentID_label);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference();

        if (mUser != null) {
            String email = mUser.getEmail();
            if (email != null) {
                String formattedEmail = email.replace(".", "_").replace("@", "_at_");

                mReference.child("Öğrenciler").child(formattedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            isStudent = true;
                            Student student = snapshot.getValue(Student.class);
                            if (student != null) {
                                user.setText("Student");
                                name.setText(student.getName());
                                surname.setText(student.getSurname());
                                studentID.setText(student.getStudentId());
                                phoneNumber.setText(student.getPhone());
                                education.setText(student.getOngoingEducation());
                            }
                        } else {
                            mReference.child("Öğretmenler").child(formattedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        isStudent = false;
                                        Instructor instructor = snapshot.getValue(Instructor.class);
                                        if (instructor != null) {
                                            user.setText("Instructor");
                                            name.setText(instructor.getName());
                                            surname.setText(instructor.getSurname());
                                            phoneNumber.setText(instructor.getPhone());
                                            update_studentID_label.setVisibility(View.GONE);
                                            studentID.setVisibility(View.GONE);
                                            education.setVisibility(View.GONE);
                                            update_education_label.setVisibility(View.GONE);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(UpdateProfileActivity.this, "Hata oluştu", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(UpdateProfileActivity.this, "Hata oluştu", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public void update(View view) {
        if (mUser != null) {
            String email = mUser.getEmail();
            if (email != null) {
                String formattedEmail = email.replace(".", "_").replace("@", "_at_");

                if (isStudent) {
                    Student updatedStudent = new Student();
                    updatedStudent.setName(name.getText().toString());
                    updatedStudent.setSurname(surname.getText().toString());
                    updatedStudent.setStudentId(studentID.getText().toString());
                    updatedStudent.setPhone(phoneNumber.getText().toString());
                    updatedStudent.setOngoingEducation(education.getText().toString());
                    updatedStudent.setEmail(email);
                    updatedStudent.setUserId(mUser.getUid());

                    mReference.child("Öğrenciler").child(formattedEmail).setValue(updatedStudent)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UpdateProfileActivity.this, "Bilgiler başarıyla güncellendi", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UpdateProfileActivity.this, "Güncelleme başarısız", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Instructor updatedInstructor = new Instructor();
                    updatedInstructor.setName(name.getText().toString());
                    updatedInstructor.setSurname(surname.getText().toString());
                    updatedInstructor.setPhone(phoneNumber.getText().toString());
                    updatedInstructor.setEmail(email);
                    updatedInstructor.setUserId(mUser.getUid());

                    mReference.child("Öğretmenler").child(formattedEmail).setValue(updatedInstructor)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UpdateProfileActivity.this, "Bilgiler başarıyla güncellendi", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UpdateProfileActivity.this, "Güncelleme başarısız", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        }
        startActivity(new Intent(UpdateProfileActivity.this, ViewProfileActivity.class));
        finish();
    }
}
