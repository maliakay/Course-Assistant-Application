package com.example.courseassistantapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.courseassistantapplication.GMailSender;
import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.Course;
import com.example.courseassistantapplication.model.Report;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class createNewReport extends AppCompatActivity {

    private EditText editTextReportScope, editTextCourseName, editTextReportSubject, editTextReportBody;
    Spinner spinnerRecipient;
    private Button buttonCreateReport;
    private DatabaseReference courseReference;
    private DatabaseReference reportReference;
    private String recipient;
    private Course tmpCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_report);

        courseReference = FirebaseDatabase.getInstance().getReference("Dersler");
        reportReference = FirebaseDatabase.getInstance().getReference("Şikayetler");

        editTextReportScope = findViewById(R.id.editTextReportScope);
        editTextCourseName = findViewById(R.id.editTextCourseName);
        spinnerRecipient = findViewById(R.id.spinnerRecipient);
        editTextReportSubject = findViewById(R.id.editTextReportSubject);
        editTextReportBody = findViewById(R.id.editTextReportBody);
        buttonCreateReport = findViewById(R.id.buttonCreateReport);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.recipients_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecipient.setAdapter(adapter);

        buttonCreateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reportScope = editTextReportScope.getText().toString();
                String courseName = editTextCourseName.getText().toString();
                String reportSubject = editTextReportSubject.getText().toString();
                String reportBody = editTextReportBody.getText().toString();
                Date reportDate = new Date();

                courseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(spinnerRecipient.getSelectedItem().toString().equals("Akademisyen")){
                           for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                               tmpCourse = childSnapshot.getValue(Course.class);
                               if (tmpCourse.getCourseId().equalsIgnoreCase(courseName.replaceAll(" ",""))){
                                   recipient = tmpCourse.getEmailOfInstructor();
                               };
                           }
                           if (recipient.isEmpty()){
                               recipient = "Akademisyen mailine ulaşılamıyor";
                           }
                        }else{
                            //Admin seçilmiş
                            recipient = "emirozturk46@outlook.com";
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(createNewReport.this, error.toException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                if (!reportScope.isEmpty() && !courseName.isEmpty() && !reportSubject.isEmpty() &&
                        !reportBody.isEmpty()) {
                    Report report = new Report(reportScope, courseName, recipient, reportSubject, reportBody, reportDate);
                    reportReference.push().setValue(report)
                            .addOnCompleteListener(createNewReport.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Email gönderme işlevi
                                        sendEmail(report);
                                        Toast.makeText(createNewReport.this, "Şikayet Oluşturuldu", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(createNewReport.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(createNewReport.this, "Yukarıdaki Alanlardan Herhangi Biri Boş Bırakılamaz", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendEmail(Report report) {
        final String email = "emiremre953@gmail.com"; // Gönderen email adresi
        final String password = "omer2020"; // Gönderen email adresinin şifresi

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(email, password);
                    sender.sendMail(report.getReportSubject(), report.getReportBody(), email, "emirozturk46@outlook.com");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }
        }).start();
    }
}