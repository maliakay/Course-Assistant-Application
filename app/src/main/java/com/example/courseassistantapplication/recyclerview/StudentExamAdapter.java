package com.example.courseassistantapplication.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.QuestionWithIndex;

import java.util.List;

public class StudentExamAdapter extends RecyclerView.Adapter<StudentExamAdapter.QuestionViewHolder> {

    private List<QuestionWithIndex> questionsList;

    public StudentExamAdapter(List<QuestionWithIndex> questionsList) {
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
        QuestionWithIndex questionWithIndex = questionsList.get(position);
        holder.questionText.setText(questionWithIndex.getQuestion().getQuestionText());
        holder.optionA.setText(questionWithIndex.getQuestion().getOptions().get("A"));
        holder.optionB.setText(questionWithIndex.getQuestion().getOptions().get("B"));
        holder.optionC.setText(questionWithIndex.getQuestion().getOptions().get("C"));
        holder.optionD.setText(questionWithIndex.getQuestion().getOptions().get("D"));
        holder.optionE.setText(questionWithIndex.getQuestion().getOptions().get("E"));
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView questionText;
        RadioButton optionA, optionB, optionC, optionD, optionE;
        RadioGroup answersGroup;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.questionText);
            optionA = itemView.findViewById(R.id.answerOptionA);
            optionB = itemView.findViewById(R.id.answerOptionB);
            optionC = itemView.findViewById(R.id.answerOptionC);
            optionD = itemView.findViewById(R.id.answerOptionD);
            optionE = itemView.findViewById(R.id.answerOptionE);
            answersGroup = itemView.findViewById(R.id.answersGroup);
        }
    }
}
