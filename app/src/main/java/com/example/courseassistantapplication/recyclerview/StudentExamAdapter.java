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
import com.example.courseassistantapplication.model.Question;

import java.util.List;

public class StudentExamAdapter extends RecyclerView.Adapter<StudentExamAdapter.QuestionViewHolder> {

    private List<Question> questions;

    public StudentExamAdapter(List<Question> questions) {
        this.questions = questions;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.questionText.setText(question.getQuestionText());
        holder.answerA.setText(question.getOptions().get("A"));
        holder.answerB.setText(question.getOptions().get("B"));
        holder.answerC.setText(question.getOptions().get("C"));
        holder.answerD.setText(question.getOptions().get("D"));
        holder.answerE.setText(question.getOptions().get("E"));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView questionText;
        RadioButton answerA, answerB, answerC, answerD, answerE;
        RadioGroup answersGroup;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.questionText);
            answersGroup = itemView.findViewById(R.id.answersGroup);
            answerA = itemView.findViewById(R.id.answerA);
            answerB = itemView.findViewById(R.id.answerB);
            answerC = itemView.findViewById(R.id.answerC);
            answerD = itemView.findViewById(R.id.answerD);
            answerE = itemView.findViewById(R.id.answerE);
        }
    }
}
