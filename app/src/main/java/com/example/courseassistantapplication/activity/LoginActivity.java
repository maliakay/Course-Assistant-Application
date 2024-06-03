package com.example.courseassistantapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.courseassistantapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private String txtEmail, txtPassword;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mReference;
    private HashMap<String, Object> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);



        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }
    public void signUp(View v){
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }
    public void forgetPassword(View v){
        startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
    }
    public void girisYap(View view){
        txtEmail = loginEmail.getText().toString();
        txtPassword = loginPassword.getText().toString();

        if(!TextUtils.isEmpty(txtEmail)  &&  !TextUtils.isEmpty(txtPassword)){
            mAuth.signInWithEmailAndPassword(txtEmail,txtPassword)
                    .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            mUser = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, ViewProfileActivity.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

        }else {
            Toast.makeText(LoginActivity.this, "Email veya şifre boş olamaz", Toast.LENGTH_SHORT).show();

        }

    }


}

