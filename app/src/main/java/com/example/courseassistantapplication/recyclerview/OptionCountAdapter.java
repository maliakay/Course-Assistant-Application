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

public class OptionCountAdapter extends RecyclerView.Adapter<OptionCountAdapter.OptionCountViewHolder> {

    private List<String> options;
    private Map<String, Integer> optionCounts;

    public OptionCountAdapter(List<String> options, Map<String, Integer> optionCounts) {
        this.options = options;
        this.optionCounts = optionCounts;
    }

    @NonNull
    @Override
    public OptionCountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option_count, parent, false);
        return new OptionCountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionCountViewHolder holder, int position) {
        String option = options.get(position);
        int count = optionCounts.get(option);

        holder.optionText.setText(option);
        holder.countText.setText(String.valueOf(count));
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    static class OptionCountViewHolder extends RecyclerView.ViewHolder {

        TextView optionText;
        TextView countText;

        public OptionCountViewHolder(@NonNull View itemView) {
            super(itemView);
            optionText = itemView.findViewById(R.id.optionText);
            countText = itemView.findViewById(R.id.countText);
        }
    }
}
