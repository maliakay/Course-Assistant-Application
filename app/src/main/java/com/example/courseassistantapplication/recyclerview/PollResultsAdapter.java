package com.example.courseassistantapplication.recyclerview;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.QuestionResult;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PollResultsAdapter extends RecyclerView.Adapter<PollResultsAdapter.ResultViewHolder> {

    private List<QuestionResult> resultsList;

    public PollResultsAdapter(List<QuestionResult> resultsList) {
        this.resultsList = resultsList;
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

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPollTitle = itemView.findViewById(R.id.textViewPollTitle);
            chartContainer = itemView.findViewById(R.id.chartContainer);
        }

        public void bind(QuestionResult questionResult) {
            textViewPollTitle.setText(questionResult.getPollTitle());
            chartContainer.removeAllViews(); // Clear previous charts if any

            for (int i = 0; i < questionResult.getQuestions().size(); i++) {
                String question = questionResult.getQuestions().get(i);
                Map<String, Integer> answerCounts = questionResult.getAnswerCounts().get(i);

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
    }
}
