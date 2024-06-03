
package com.example.courseassistantapplication.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.activity.AnnouncementActivity;
import com.example.courseassistantapplication.activity.CreateAnnouncementActivity;
import com.example.courseassistantapplication.activity.LoginActivity;
import com.example.courseassistantapplication.activity.ViewClassesActivity;
import com.example.courseassistantapplication.model.Course;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ClassNameAdapter extends RecyclerView.Adapter<ClassNameAdapter.ClassNameViewHolder> {
    private List<Course> courseList;
    private Context context;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public ClassNameAdapter(List<Course> courseList, Context context, FirebaseUser mUser) {
        this.courseList = courseList;
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.mUser = this.mAuth.getCurrentUser();
    }


    @NonNull
    @Override
    public ClassNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_classes, parent, false);
        return new ClassNameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassNameViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.classCode.setText(course.getCourseId());
        holder.className.setText(course.getCourseName());
        holder.cardView.setOnClickListener(v -> {
                    if (mUser != null) {
                        String currentUserEmail = mUser.getEmail();
                        if (currentUserEmail != null && currentUserEmail.endsWith("@std.yildiz.edu.tr")) {
                            Intent intent = new Intent(context, AnnouncementActivity.class);
                            intent.putExtra("courseId",course.getCourseId());
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, CreateAnnouncementActivity.class);
                            intent.putExtra("courseId",course.getCourseId());
                            context.startActivity(intent);
                        }
                    } else {
                        Toast.makeText(context, "User not logged in. Redirecting to login...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class ClassNameViewHolder extends RecyclerView.ViewHolder {
        TextView classCode, className;
        CardView cardView;

        public ClassNameViewHolder(@NonNull View itemView) {
            super(itemView);
            classCode = itemView.findViewById(R.id.tvClassCode);
            className = itemView.findViewById(R.id.tvClassName);
            cardView = itemView.findViewById(R.id.cwRecycle);
        }
    }
}
