package com.example.courseassistantapplication.recyclerview;

// PollsAdapter.java
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.activity.TakePollActivity;
import com.example.courseassistantapplication.model.Poll;

import java.util.List;

public class PollsAdapter extends RecyclerView.Adapter<PollsAdapter.PollViewHolder> {

    private List<Poll> pollsList;

    public PollsAdapter(List<Poll> pollsList) {
        this.pollsList = pollsList;
    }

    @NonNull
    @Override
    public PollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poll, parent, false);
        return new PollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PollViewHolder holder, int position) {
        Poll poll = pollsList.get(position);
        holder.bind(poll);
    }

    @Override
    public int getItemCount() {
        return pollsList.size();
    }

    class PollViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;

        public PollViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }

        public void bind(Poll poll) {
            textViewTitle.setText(poll.getTitle());
            itemView.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, TakePollActivity.class);
                intent.putExtra("pollId", poll.getPollId());
                intent.putExtra("pollName",poll.getTitle());
                context.startActivity(intent);
            });
        }
    }
}

