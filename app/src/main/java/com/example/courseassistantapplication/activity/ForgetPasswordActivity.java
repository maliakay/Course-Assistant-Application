package com.example.courseassistantapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.courseassistantapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText emailField;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailField = findViewById(R.id.forget_email);
        mAuth = FirebaseAuth.getInstance();
    }

    public void resetPassword(View view) {
        String email = emailField.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(ForgetPasswordActivity.this, "Email alanı boş olamaz", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgetPasswordActivity.this, "Şifre sıfırlama talimatları email adresinize gönderildi", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgetPasswordActivity.this, "Şifre sıfırlama işlemi başarısız: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
        finish();
    }
}
