package com.example.courseassistantapplication.recyclerview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.QuestionPollResult;
import com.example.courseassistantapplication.model.CSVHelper;
import com.example.courseassistantapplication.model.EmailHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PollResultsAdapter extends RecyclerView.Adapter<PollResultsAdapter.ResultViewHolder> {

    private static final String TAG = "PollResultsAdapter";
    private List<QuestionPollResult> resultsList;
    private Context context;

    public PollResultsAdapter(List<QuestionPollResult> resultsList, Context context) {
        this.resultsList = resultsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poll_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        holder.bind(resultsList.get(position));
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewPollTitle;
        private LinearLayout chartContainer;
        private Button buttonExportCSV;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPollTitle = itemView.findViewById(R.id.textViewPollTitle);
            chartContainer = itemView.findViewById(R.id.chartContainer);
            buttonExportCSV = itemView.findViewById(R.id.buttonExportCSV);
        }

        public void bind(QuestionPollResult QuestionPollResult) {
            textViewPollTitle.setText(QuestionPollResult.getPollTitle());
            chartContainer.removeAllViews(); // Clear previous charts if any

            for (int i = 0; i < QuestionPollResult.getQuestions().size(); i++) {
                String question = QuestionPollResult.getQuestions().get(i);
                Map<String, Integer> answerCounts = QuestionPollResult.getAnswerCounts().get(i);

                TextView questionTextView = new TextView(itemView.getContext());
                questionTextView.setText(question);
                questionTextView.setTextSize(16);
                questionTextView.setPadding(0, 16, 0, 16);
                chartContainer.addView(questionTextView);

                PieChart pieChart = new PieChart(itemView.getContext());
                chartContainer.addView(pieChart, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 600));

                setupPieChart(pieChart, answerCounts, question);
            }

            buttonExportCSV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Uri csvUri = CSVHelper.createCSVFile(QuestionPollResult.getPollTitle(), QuestionPollResult.getQuestions(), QuestionPollResult.getAnswerCounts(), context);
                        EmailHelper.sendEmailWithAttachment(csvUri, FirebaseAuth.getInstance().getCurrentUser().getEmail(), context);
                        Toast.makeText(context, "CSV file created and email sent", Toast.LENGTH_SHORT).show();
                        showOpenCSVDialog(csvUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to create CSV file", e);
                        Toast.makeText(context, "Failed to create CSV file", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void setupPieChart(PieChart pieChart, Map<String, Integer> answerCounts, String question) {
            List<PieEntry> pieEntries = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : answerCounts.entrySet()) {
                pieEntries.add(new PieEntry(entry.getValue(), entry.getKey()));
            }

            PieDataSet dataSet = new PieDataSet(pieEntries, "Anket Sonuçları");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            PieData data = new PieData(dataSet);

            pieChart.setData(data);
            pieChart.invalidate(); // refresh
            pieChart.getDescription().setEnabled(false);
            pieChart.setCenterText(question);
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.setEntryLabelTextSize(12f);
        }

        private void showOpenCSVDialog(Uri csvUri) {
            new AlertDialog.Builder(context)
                    .setTitle("CSV Dosyası Oluşturuldu")
                    .setMessage("CSV dosyası oluşturuldu. Açmak ister misiniz?")
                    .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            openCSVFile(csvUri);
                        }
                    })
                    .setNegativeButton("Hayır", null)
                    .show();
        }

        private void openCSVFile(Uri csvUri) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(csvUri, "text/csv");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
