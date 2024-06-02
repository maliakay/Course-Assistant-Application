package com.example.courseassistantapplication.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.activity.AddStudentActivity;
import com.example.courseassistantapplication.model.Course;
import com.example.courseassistantapplication.model.Group;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class CourseGroupAdapter extends RecyclerView.Adapter<CourseGroupAdapter.CourseGroupViewHolder> {

    private List<Course> courseList;
    private Context context;
    private FirebaseUser mUser;

    public CourseGroupAdapter(List<Course> courseList, Context context, FirebaseUser mUser) {
        this.courseList = courseList;
        this.context = context;
        this.mUser = mUser;
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
        if (mUser != null && mUser.getEmail() != null && course.getEmailOfInstructor().equals(mUser.getEmail())) {
            holder.groupNumbers.setText("owner");
        } else {
            StringBuilder groupNumbers = new StringBuilder();
            for (Group group : course.getCourseGroups()) {
                groupNumbers.append(group.getGroupNumber()).append("\n");
                x = group.getGroupNumber();
            }
            holder.groupNumbers.setText(groupNumbers.toString().trim());
        }
        String finalX = x;
        holder.btn_add_student.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddStudentActivity.class);
            intent.putExtra("courseId", course.getCourseId());
            intent.putExtra("groupNumber", finalX);
            context.startActivity(intent);
        });
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
