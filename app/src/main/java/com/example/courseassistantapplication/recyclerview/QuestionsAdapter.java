package com.example.courseassistantapplication.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {

    private List<String> questionsList;

    public QuestionsAdapter(List<String> questionsList) {
        this.questionsList = questionsList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.questionText.setText(questionsList.get(position));
        holder.questionNumber.setText("Question " + (position + 1));
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView questionNumber;
        EditText questionText;
        EditText[] answerOptions;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionNumber = itemView.findViewById(R.id.questionNumber);
            questionText = itemView.findViewById(R.id.questionText);
            answerOptions = new EditText[5];
            answerOptions[0] = itemView.findViewById(R.id.answerOptionA);
            answerOptions[1] = itemView.findViewById(R.id.answerOptionB);
            answerOptions[2] = itemView.findViewById(R.id.answerOptionC);
            answerOptions[3] = itemView.findViewById(R.id.answerOptionD);
            answerOptions[4] = itemView.findViewById(R.id.answerOptionE);
        }
    }
}
