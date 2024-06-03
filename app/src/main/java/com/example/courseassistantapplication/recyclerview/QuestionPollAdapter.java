package com.example.courseassistantapplication.recyclerview;

// QuestionPollAdapter.java
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;

import java.util.List;

public class QuestionPollAdapter extends RecyclerView.Adapter<QuestionPollAdapter.QuestionViewHolder> {

    private List<String> questionsList;

    public QuestionPollAdapter(List<String> questionsList) {
        this.questionsList = questionsList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poll_question, parent, false);
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

        private EditText editTextQuestion;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextQuestion = itemView.findViewById(R.id.editTextQuestion);
        }

        public void bind(int position) {
            editTextQuestion.setText(questionsList.get(position));
            editTextQuestion.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    questionsList.set(position, s.toString());
                }
            });
        }
    }
}
