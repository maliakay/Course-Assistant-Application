package com.example.courseassistantapplication.model;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class CSVHelper {

    private static final String TAG = "CSVHelper";

    public static Uri createCSVFile(String pollTitle, List<String> questions, List<Map<String, Integer>> answers, Context context) throws IOException {
        File csvFile;
        Uri csvUri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, pollTitle + "_results.csv");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "text/csv");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS);

            csvUri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);

            if (csvUri != null) {
                try (OutputStream outputStream = context.getContentResolver().openOutputStream(csvUri)) {
                    if (outputStream != null) {
                        writeCSVData(outputStream, questions, answers);
                        Log.d(TAG, "CSV file created successfully");
                        return csvUri;
                    } else {
                        throw new IOException("Failed to get output stream");
                    }
                }
            } else {
                throw new IOException("Failed to create new MediaStore record.");
            }
        } else {
            csvFile = new File(Environment.getExternalStorageDirectory(), pollTitle + "_results.csv");
            Log.d(TAG, "Creating CSV file at: " + csvFile.getAbsolutePath());

            if (!csvFile.getParentFile().exists()) {
                boolean dirCreated = csvFile.getParentFile().mkdirs();
                Log.d(TAG, "Directory created: " + dirCreated);
            }

            try (FileWriter writer = new FileWriter(csvFile)) {
                writeCSVData(writer, questions, answers);
                Log.d(TAG, "CSV file created successfully");
            } catch (IOException e) {
                Log.e(TAG, "Error writing to CSV file", e);
                throw e;
            }

            csvUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", csvFile);
        }

        return csvUri;
    }

    private static void writeCSVData(OutputStream outputStream, List<String> questions, List<Map<String, Integer>> answers) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Question,Answer,Count\n");

        for (int i = 0; i < questions.size(); i++) {
            String question = questions.get(i);
            Map<String, Integer> answerCounts = answers.get(i);

            for (Map.Entry<String, Integer> entry : answerCounts.entrySet()) {
                sb.append(question)
                        .append(",")
                        .append(entry.getKey())
                        .append(",")
                        .append(entry.getValue().toString())
                        .append("\n");
            }
        }

        outputStream.write(sb.toString().getBytes());
        outputStream.flush();
    }

    private static void writeCSVData(FileWriter writer, List<String> questions, List<Map<String, Integer>> answers) throws IOException {
        writer.append("Question,Answer,Count\n");

        for (int i = 0; i < questions.size(); i++) {
            String question = questions.get(i);
            Map<String, Integer> answerCounts = answers.get(i);

            for (Map.Entry<String, Integer> entry : answerCounts.entrySet()) {
                writer.append(question)
                        .append(",")
                        .append(entry.getKey())
                        .append(",")
                        .append(entry.getValue().toString())
                        .append("\n");
            }
        }

        writer.flush();
    }
}
