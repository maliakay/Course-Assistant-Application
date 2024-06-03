package com.example.courseassistantapplication.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;

import java.util.List;

public class AttandenceAdapter extends RecyclerView.Adapter<AttandenceAdapter.AttandenceViewHolder> {

    private List<String> studentList;

    public static class AttandenceViewHolder extends RecyclerView.ViewHolder {
        public TextView emailTextView;

        public AttandenceViewHolder(View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
        }
    }

    public AttandenceAdapter(List<String> studentList) {
        this.studentList = studentList;
    }

    @Override
    public AttandenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attandence, parent, false);
        return new AttandenceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AttandenceViewHolder holder, int position) {
        String student = studentList.get(position);
        holder.emailTextView.setText(student);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}

