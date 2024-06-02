package com.example.courseassistantapplication.recyclerview;

// PollResultsAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.QuestionResult;

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

        private TextView textViewQuestion;
        private TextView textViewResults;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewQuestion = itemView.findViewById(R.id.textViewQuestion);
            textViewResults = itemView.findViewById(R.id.textViewResults);
        }

        public void bind(QuestionResult questionResult) {
            textViewQuestion.setText(questionResult.getQuestion());
            StringBuilder results = new StringBuilder();
            for (Map.Entry<String, Integer> entry : questionResult.getAnswerCounts().entrySet()) {
                results.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            textViewResults.setText(results.toString().trim());
        }
    }
}
