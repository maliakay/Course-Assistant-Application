package com.example.courseassistantapplication.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

        private TextView textViewPollTitle;
        private TextView textViewQuestion;
        private TextView textViewResults;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPollTitle = itemView.findViewById(R.id.textViewPollTitle);
            textViewQuestion = itemView.findViewById(R.id.textViewQuestion);
            textViewResults = itemView.findViewById(R.id.textViewResults);
        }

        public void bind(QuestionResult questionResult) {
            textViewPollTitle.setText(questionResult.getPollTitle());
            StringBuilder questionsAndAnswers = new StringBuilder();
            for (int i = 0; i < questionResult.getQuestions().size(); i++) {
                questionsAndAnswers.append(questionResult.getQuestions().get(i)).append("\n");
                for (Map.Entry<String, Integer> entry : questionResult.getAnswerCounts().get(i).entrySet()) {
                    questionsAndAnswers.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
                questionsAndAnswers.append("\n");
            }
            textViewQuestion.setText(questionsAndAnswers.toString().trim());
        }
    }
}
