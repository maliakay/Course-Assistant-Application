package com.example.courseassistantapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.Student;
import com.example.courseassistantapplication.model.Instructor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private EditText signUpEmail, signUpPassword, signUpName, signUp_surname, signUp_ID, sign_education, sign_phoneNumber;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUpEmail = findViewById(R.id.signUp_email);
        signUpPassword = findViewById(R.id.signUp_password);
        signUpName = findViewById(R.id.signUp_name);
        signUp_surname = findViewById(R.id.signUp_surname);
        signUp_ID = findViewById(R.id.signUp_ID);
        sign_education = findViewById(R.id.sign_education);
        sign_phoneNumber = findViewById(R.id.sign_phoneNumber);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
    }

    public void kayitOl(View v){
        String email = signUpEmail.getText().toString();
        String password = signUpPassword.getText().toString();
        String name = signUpName.getText().toString();
        String surname = signUp_surname.getText().toString();
        String studentId = signUp_ID.getText().toString();
        String phone = sign_phoneNumber.getText().toString();
        String education = sign_education.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname)){

            if (!phone.startsWith("0")) {
                Toast.makeText(SignUpActivity.this, "Telefon numarası '0' ile başlamalıdır", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                mUser = mAuth.getCurrentUser();
                                assert mUser != null;
                                mUser.sendEmailVerification();


                                String userId = mUser.getUid();

                                if (email.endsWith("@std.yildiz.edu.tr")) {
                                    Student student = new Student(name, surname, email, studentId);
                                    student.setPhone(phone);
                                    student.setOngoingEducation(education);
                                    student.setUserId(userId);

                                    String formattedEmail = email.replace(".", "_").replace("@", "_at_");

                                    mReference.child("Öğrenciler").child(formattedEmail)
                                            .setValue(student)
                                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(SignUpActivity.this, "Kayıt İşlemi başarılı", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                } else if (email.endsWith("@yildiz.edu.tr")) {
                                    Instructor instructor = new Instructor();
                                    instructor.setName(name);
                                    instructor.setSurname(surname);
                                    instructor.setEmail(email);
                                    instructor.setPhone(phone);
                                    instructor.setUserId(userId);

                                    // E-mail adresini uygun bir düğüm adı yapmak için formatlama
                                    String formattedEmail = email.replace(".", "_").replace("@", "_at_");

                                    mReference.child("Öğretmenler").child(formattedEmail)
                                            .setValue(instructor)
                                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(SignUpActivity.this, "Kayıt İşlemi başarılı", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });

                                } else {
                                    Toast.makeText(SignUpActivity.this, "Geçersiz email domaini", Toast.LENGTH_SHORT).show();
                                }

                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignUpActivity.this, "Kayıt işlemi başarısız", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Email ve şifre boş olamaz", Toast.LENGTH_SHORT).show();
        }
    }

    public void login(View v){
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }


}
