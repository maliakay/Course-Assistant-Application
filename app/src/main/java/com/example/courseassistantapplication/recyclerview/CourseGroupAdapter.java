package com.example.courseassistantapplication.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.activity.AddStudentActivity;
import com.example.courseassistantapplication.model.Course;
import com.example.courseassistantapplication.model.Group;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CourseGroupAdapter extends RecyclerView.Adapter<CourseGroupAdapter.CourseGroupViewHolder> {

    private List<Course> courseList;
    private Context context;

    public CourseGroupAdapter(List<Course> courseList,Context context) {
        this.courseList = courseList;
        this.context = context;
    }

    @NonNull
    @Override
    public CourseGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_group, parent, false);
        return new CourseGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseGroupViewHolder holder, int position) {
        Course course = courseList.get(position);
        // Display the course details
        holder.courseName.setText(course.getCourseName());
        holder.courseId.setText(course.getCourseId());
        holder.courseDate.setText(course.getDate());
        String x = null;
        // Display the groups for the course
        for (Group group : course.getCourseGroups()) {
            holder.groupNumbers.setText(group.getGroupNumber());
             x = group.getGroupNumber();
        }
        String finalX = x;
        holder.btn_add_student.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddStudentActivity.class);
            intent.putExtra("courseId", course.getCourseId());
            intent.putExtra("groupNumber", finalX);
            context.startActivity(intent);
        });

        // Check if the current user is the instructor of the course
        if (course.getEmailOfInstructor().equals(currentUser.getEmail())) {
            holder.groupNumbers.setText("owner");
        } else {
            StringBuilder groupNumbers = new StringBuilder();
            for (Group group : course.getCourseGroups()) {
                groupNumbers.append(group.getGroupNumber()).append("\n");
            }
            holder.groupNumbers.setText(groupNumbers.toString().trim());
        }
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseGroupViewHolder extends RecyclerView.ViewHolder {

        TextView courseName, courseId, courseDate, groupNumbers;
        Button btn_add_student;

        public CourseGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_name);
            courseId = itemView.findViewById(R.id.course_id);
            courseDate = itemView.findViewById(R.id.course_date);
            groupNumbers = itemView.findViewById(R.id.group_numbers);
            btn_add_student = itemView.findViewById(R.id.btn_add_student);

        }
    }
}
