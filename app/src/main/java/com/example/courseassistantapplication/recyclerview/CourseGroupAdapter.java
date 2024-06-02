package com.example.courseassistantapplication.recyclerview;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.Course;
import com.example.courseassistantapplication.model.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class CourseGroupAdapter extends RecyclerView.Adapter<CourseGroupAdapter.CourseGroupViewHolder> {
    private List<Course> courseList;
    private FirebaseUser currentUser;

    public CourseGroupAdapter(List<Course> courseList) {
        this.courseList = courseList;
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
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

        public CourseGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_name);
            courseId = itemView.findViewById(R.id.course_id);
            courseDate = itemView.findViewById(R.id.course_date);
            groupNumbers = itemView.findViewById(R.id.group_numbers);
        }
    }
}

