package com.example.courseassistantapplication.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.QuestionWithIndex;

import java.util.List;

public class StudentResultAdapter extends RecyclerView.Adapter<StudentResultAdapter.ResultViewHolder> {

    private List<QuestionWithIndex> questionsList;

    public StudentResultAdapter(List<QuestionWithIndex> questionsList) {
        this.questionsList = questionsList;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        QuestionWithIndex questionWithIndex = questionsList.get(position);
        holder.bind(questionWithIndex);
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        private TextView questionTextView;
        private TextView answerTextView;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            answerTextView = itemView.findViewById(R.id.answerTextView);
        }

        public void bind(QuestionWithIndex questionWithIndex) {
            questionTextView.setText(questionWithIndex.getQuestion().getQuestionText());
            answerTextView.setText("Your answer: " + questionWithIndex.getQuestion().getUserAnswer());
        }
    }
}
