package com.example.courseassistantapplication.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;

public class ExamQuestionAdapter extends RecyclerView.Adapter<ExamQuestionAdapter.QuestionViewHolder> {

    private int questionCount;

    public ExamQuestionAdapter(int questionCount) {
        this.questionCount = questionCount;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.questionNumber.setText("Question " + (position + 1));
    }

    @Override
    public int getItemCount() {
        return questionCount;
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView questionNumber;
        EditText questionText;
        EditText[] answerOptions = new EditText[5];

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionNumber = itemView.findViewById(R.id.questionNumber);
            questionText = itemView.findViewById(R.id.questionText);
            answerOptions[0] = itemView.findViewById(R.id.answerOptionA);
            answerOptions[1] = itemView.findViewById(R.id.answerOptionB);
            answerOptions[2] = itemView.findViewById(R.id.answerOptionC);
            answerOptions[3] = itemView.findViewById(R.id.answerOptionD);
            answerOptions[4] = itemView.findViewById(R.id.answerOptionE);
        }
    }
}
