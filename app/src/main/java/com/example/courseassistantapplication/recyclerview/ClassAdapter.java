
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
import com.example.courseassistantapplication.activity.ViewCourseSiteActivity;
import com.example.courseassistantapplication.model.Course;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.CourseViewHolder> {
    private final List<Course> courseList;
    private final Context context;
    private final FirebaseUser mUser;

    public ClassAdapter(List<Course> courseList, Context context, FirebaseUser mUser) {
        this.courseList = courseList;
        this.context = context;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_classes, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.classCode.setText(course.getCourseId());
        holder.className.setText(course.getCourseName());
        holder.cardView.setOnClickListener(v -> {
            Toast.makeText(context, "Card Clicked: " + course.getCourseName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, ViewCourseSiteActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView classCode, className;
        CardView cardView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            classCode = itemView.findViewById(R.id.tvClassCode);
            className = itemView.findViewById(R.id.tvClassName);
            cardView = itemView.findViewById(R.id.rwClasses);
        }
    }
}
