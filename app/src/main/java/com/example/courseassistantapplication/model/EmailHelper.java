package com.example.courseassistantapplication.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class EmailHelper {

    public static void sendEmailWithAttachment(Uri csvUri, String recipientEmail, Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/csv");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Anket Sonuçları");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Anket sonuçları ektedir.");
        emailIntent.putExtra(Intent.EXTRA_STREAM, csvUri);

        context.startActivity(Intent.createChooser(emailIntent, "Email gönder"));
    }
}
