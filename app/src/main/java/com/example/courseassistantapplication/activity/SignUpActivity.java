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

    //private String txtEmail, txtPassword, txtName, txtSurname, txtID, txtEducation, txtPhoneNumber;
    //private HashMap<String, Object> mData;
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


    //Öğrenci kayıt olma
    public void kayitOl(View v){
        Student student = new Student();

        student.setEmail(signUpEmail.getText().toString());
        student.setStudentId(signUp_ID.getText().toString());
        student.setPhone(sign_phoneNumber.getText().toString());
        student.setName(signUpName.getText().toString());
        student.setSurname(signUp_surname.getText().toString());
        student.setOngoingEducation(sign_education.getText().toString());
        student.setPassword(signUpPassword.getText().toString());

        if (!TextUtils.isEmpty(student.getEmail()) && !TextUtils.isEmpty(student.getPassword())
                && !TextUtils.isEmpty(student.getName()) && !TextUtils.isEmpty(student.getSurname())){
            //Kullanıcı email ve şifreyle kaydetme
            mAuth.createUserWithEmailAndPassword(student.getEmail(),student.getPassword())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                mUser = mAuth.getCurrentUser();
                                mUser.sendEmailVerification();

                                student.setUserId(mUser.getUid());


                                mReference.child("Öğrenciler").child(student.getUserId())
                                        .setValue(student)
                                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(SignUpActivity.this, "Kayıt İşlemi başarılı", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(SignUpActivity.this, "Kayıt işlemi başarısız", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }else{
            Toast.makeText(this, "Email Ve Şifre Boş Olamaz", Toast.LENGTH_SHORT).show();
        }
    }
    public void login(View v){
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    };
}