package com.example.courseassistantapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private String txtEmail, txtPassword, txtName, txtSurname, txtID, txtEducation, txtPhoneNumber;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mReference;
    private HashMap<String, Object> mData;


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
    public void login(View v){

        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

    };

    public void kayitOl(View v){
        txtEmail= signUpEmail.getText().toString();
        txtPassword = signUpPassword.getText().toString();
        txtName= signUpName.getText().toString();
        txtSurname = signUp_surname.getText().toString();
        txtID = signUp_ID.getText().toString();
        txtEducation= sign_education.getText().toString();
        txtPhoneNumber= sign_phoneNumber.getText().toString();



        if (!TextUtils.isEmpty(txtEmail) && !TextUtils.isEmpty(txtPassword) && !TextUtils.isEmpty(txtName) && !TextUtils.isEmpty(txtSurname)){
            mAuth.createUserWithEmailAndPassword(txtEmail,txtPassword)//Kullanıcı email ve şifreyle kaydetme
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){


                                mUser = mAuth.getCurrentUser();
                                mUser.sendEmailVerification();

                                mData = new HashMap<>();
                                mData.put("KullanıcıAdı", txtName);
                                mData.put("KullanıcıSoyadı", txtSurname);
                                mData.put("KullanıcıEmail", txtEmail);
                                mData.put("KullanıcıŞifre", txtPassword);
                                mData.put("KullanıcıNumarası", txtID);
                                mData.put("KullanıcıTelefonu", txtPhoneNumber);
                                mData.put("KullanıcıEğitimDüzeyi", txtEducation);
                                mData.put("KullaniciID", mUser.getUid());

                                mReference.child("Kullanıcılar").child(mUser.getUid())
                                        .setValue(mData)
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



    } }