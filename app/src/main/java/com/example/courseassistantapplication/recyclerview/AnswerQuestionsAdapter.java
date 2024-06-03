package com.example.courseassistantapplication.recyclerview;

// AnswerQuestionsAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;

import java.util.List;

public class AnswerQuestionsAdapter extends RecyclerView.Adapter<AnswerQuestionsAdapter.QuestionViewHolder> {

    private List<String> questionsList;
    private List<String> answersList;
    private String[] answerOptions = {"Cok iyi", "Iyi", "Orta", "Kotu", "Cok kotu"};

    public AnswerQuestionsAdapter(List<String> questionsList, List<String> answersList) {
        this.questionsList = questionsList;
        this.answersList = answersList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewQuestion;
        private Spinner spinnerAnswer;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewQuestion = itemView.findViewById(R.id.textViewQuestion);
            spinnerAnswer = itemView.findViewById(R.id.spinnerAnswer);
        }

        public void bind(int position) {
            textViewQuestion.setText(questionsList.get(position));

            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_item, answerOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAnswer.setAdapter(adapter);

            spinnerAnswer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    answersList.set(position, answerOptions[pos]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }
}
