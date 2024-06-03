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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentExamAdapter extends RecyclerView.Adapter<StudentExamAdapter.QuestionViewHolder> {

    private List<QuestionWithIndex> questionList;

    public StudentExamAdapter(List<QuestionWithIndex> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_exam_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        QuestionWithIndex questionWithIndex = questionList.get(position);
        holder.questionText.setText(questionWithIndex.getQuestion().getQuestionText());

        Map<String, String> optionsMap = questionWithIndex.getQuestion().getOptions();
        List<String> options = new ArrayList<>(optionsMap.values());
        if (options.size() == 5) {
            holder.optionA.setText(options.get(0));
            holder.optionB.setText(options.get(1));
            holder.optionC.setText(options.get(2));
            holder.optionD.setText(options.get(3));
            holder.optionE.setText(options.get(4));
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView questionText;
        RadioGroup answersGroup;
        RadioButton optionA, optionB, optionC, optionD, optionE;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.questionText);
            answersGroup = itemView.findViewById(R.id.answersGroup);
            optionA = itemView.findViewById(R.id.answerOptionA);
            optionB = itemView.findViewById(R.id.answerOptionB);
            optionC = itemView.findViewById(R.id.answerOptionC);
            optionD = itemView.findViewById(R.id.answerOptionD);
            optionE = itemView.findViewById(R.id.answerOptionE);
        }
    }
}
