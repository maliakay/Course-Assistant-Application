package com.example.courseassistantapplication.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;

import java.util.List;
import java.util.Map;

public class StudentResponseAdapter extends RecyclerView.Adapter<StudentResponseAdapter.ResponseViewHolder> {

    private List<String> studentIds;
    private List<String> studentNames;
    private List<Map<String, String>> studentResponses;

    public StudentResponseAdapter(List<String> studentIds, List<String> studentNames, List<Map<String, String>> studentResponses) {
        this.studentIds = studentIds;
        this.studentNames = studentNames;
        this.studentResponses = studentResponses;
    }

    @NonNull
    @Override
    public ResponseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_response, parent, false);
        return new ResponseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResponseViewHolder holder, int position) {
        String studentId = studentIds.get(position);
        String studentName = studentNames.get(position);
        Map<String, String> responses = studentResponses.get(position);

        holder.studentIdTextView.setText(studentId);
        holder.studentNameTextView.setText(studentName);
        holder.responsesTextView.setText(responses.toString());
    }

    @Override
    public int getItemCount() {
        return studentIds.size();
    }

    static class ResponseViewHolder extends RecyclerView.ViewHolder {

        TextView studentIdTextView;
        TextView studentNameTextView;
        TextView responsesTextView;

        public ResponseViewHolder(@NonNull View itemView) {
            super(itemView);
            studentIdTextView = itemView.findViewById(R.id.studentId);
            studentNameTextView = itemView.findViewById(R.id.studentName);
            responsesTextView = itemView.findViewById(R.id.responses);
        }
    }
}

