package com.example.courseassistantapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class ViewProfileActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mReference;

    TextView name, surname, studentIDLabel, studentID, phoneNumber, education, mail, educationLabel, user, showCourses_btn;
    Button btn_report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        name = findViewById(R.id.name);
        user = findViewById(R.id.user);
        surname = findViewById(R.id.surname);
        studentIDLabel = findViewById(R.id.studentIDLabel);
        studentID = findViewById(R.id.studentID);

        phoneNumber = findViewById(R.id.phoneNumber);
        education = findViewById(R.id.education);
        educationLabel = findViewById(R.id.educationLabel);
        mail = findViewById(R.id.mail);
        btn_report = findViewById(R.id.report_button);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference();

        if (mUser != null) {
            String email = mUser.getEmail();
            if (email != null) {
                String formattedEmail = email.replace(".", "_").replace("@", "_at_");

                mReference.child("Öğrenciler").child(formattedEmail).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            btn_report.setVisibility(View.GONE);
                            Student student = snapshot.getValue(Student.class);
                            if (student != null) {
                                user.setText("Student");
                                name.setText(student.getName());
                                surname.setText(student.getSurname());
                                studentID.setText(student.getStudentId());
                                phoneNumber.setText(student.getPhone());
                                education.setText(student.getOngoingEducation());
                                mail.setText(student.getEmail());
                            }
                        } else {
                            mReference.child("Öğretmenler").child(formattedEmail).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Instructor instructor = snapshot.getValue(Instructor.class);
                                        if (instructor != null) {
                                            user.setText("Instructor");
                                            name.setText(instructor.getName());
                                            surname.setText(instructor.getSurname());
                                            phoneNumber.setText(instructor.getPhone());
                                            education.setText("N/A");
                                            mail.setText(instructor.getEmail());

                                            // Hide Student ID section
                                            studentIDLabel.setVisibility(View.GONE);
                                            studentID.setVisibility(View.GONE);
                                            education.setVisibility(View.GONE);
                                            educationLabel.setVisibility(View.GONE);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(ViewProfileActivity.this, "Hata oluştu", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ViewProfileActivity.this, "Hata oluştu", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public void update(View view) {
        startActivity(new Intent(ViewProfileActivity.this, UpdateProfileActivity.class));
    }

    public void callPhoneNumber(View view) {
        String phone = phoneNumber.getText().toString();
        if (!phone.isEmpty()) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return;
            }
            startActivity(callIntent);
        } else {
            Toast.makeText(this, "Phone number is not available", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMail(View view) {
        String email = mail.getText().toString();
        if (!email.isEmpty()) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } else {
            Toast.makeText(this, "Email is not available", Toast.LENGTH_SHORT).show();
        }
    }

    public void openWhatsApp(View view) {
        String phone = phoneNumber.getText().toString();
        if (!phone.isEmpty()) {
            // Başındaki '0'ı çıkar ve ülke kodunu ekle
            if (phone.startsWith("0")) {
                phone = phone.substring(1); // İlk karakteri çıkar
            }
            String fullPhoneNumber = "+90" + phone; // Türkiye için ülke kodu ekle

            try {
                String url = "https://api.whatsapp.com/send?phone=" + fullPhoneNumber;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "WhatsApp is not installed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Phone number is not available", Toast.LENGTH_SHORT).show();
        }
    }


    public void listReports(View v){
        startActivity(new Intent(ViewProfileActivity.this, ListReportsActivity.class));
    }

    public void showCourses(View view){
        startActivity(new Intent(ViewProfileActivity.this, MyCoursesTeacherActivity.class));
    }
}
