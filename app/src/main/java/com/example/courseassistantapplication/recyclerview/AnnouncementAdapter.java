package com.example.courseassistantapplication.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.activity.AddCommentsActivity;
import com.example.courseassistantapplication.model.Announcement;
import com.example.courseassistantapplication.model.Comment;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {
    private List<Announcement> announcementList;
    private Context context;

    public AnnouncementAdapter(List<Announcement> announcementList) {
        this.announcementList = announcementList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Announcement announcement = announcementList.get(position);
        holder.announcementContent.setText(announcement.getContent());
        holder.commentButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddCommentsActivity.class);
            intent.putExtra("announcementId", announcement.getAnnouncementId());
            context.startActivity(intent);
        });

        holder.loadComments(announcement.getComment());
    }

    @Override
    public int getItemCount() {
        return announcementList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView announcementContent;
        public Button commentButton;
        public RecyclerView commentsRecyclerView;
        public ViewHolder(View view) {
            super(view);
            announcementContent = view.findViewById(R.id.announcement_content);
            commentButton = view.findViewById(R.id.comment_button);
            commentsRecyclerView = view.findViewById(R.id.recycler_view);//

        }
        public void loadComments(List<Comment> commentList) {
            CommentAdapter commentAdapter = new CommentAdapter(commentList);
            commentsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            commentsRecyclerView.setAdapter(commentAdapter);
        }
    }

}
