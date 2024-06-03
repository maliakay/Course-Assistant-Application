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
import com.example.courseassistantapplication.activity.AddPollsActivity;
import com.example.courseassistantapplication.activity.AddStudentActivity;
import com.example.courseassistantapplication.activity.CreateNewReport;
import com.example.courseassistantapplication.activity.JoinAttendanceActivity;
import com.example.courseassistantapplication.activity.PollResultsActivity;
import com.example.courseassistantapplication.activity.StartAttendanceActivity;
import com.example.courseassistantapplication.activity.CreateExamActivity;
import com.example.courseassistantapplication.activity.ViewExamResponsesActivity;
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
        holder.btn_join_attendance.setOnClickListener(v -> {
            Intent intent = new Intent(context, JoinAttendanceActivity.class);
            intent.putExtra("courseId", course.getCourseId());
            intent.putExtra("userMail", mUser.getEmail());
            context.startActivity(intent);
        });
        holder.btn_create_report.setOnClickListener(v -> {
            Intent intent = new Intent(context, CreateNewReport.class);
            intent.putExtra("courseId", course.getCourseId());
            intent.putExtra("instructorMail", course.getEmailOfInstructor());
            context.startActivity(intent);
        });

        String x = null;
        StringBuilder groupNumbers = new StringBuilder();
        for (Group group : course.getCourseGroups()) {
            groupNumbers.append(group.getGroupNumber()).append("\n");
            x = group.getGroupNumber();
        }

        if (!(mUser.getEmail().endsWith("@std.yildiz.edu.tr"))){
            // Display the groups for the course
            if (mUser.getEmail() != null && course.getEmailOfInstructor().equals(mUser.getEmail())) {
                holder.groupNumbers.setText("owner");
                holder.btn_add_student.setVisibility(View.GONE);
            }
            else {
                holder.groupNumbers.setText(groupNumbers.toString().trim());
            }

            String finalX = x;
            holder.btn_add_student.setOnClickListener(v -> {
                Intent intent = new Intent(context, AddStudentActivity.class);
                intent.putExtra("courseId", course.getCourseId());
                intent.putExtra("groupNumber", finalX);
                context.startActivity(intent);
            });
            holder.btn_show_responses.setOnClickListener(v -> {
                Intent intent = new Intent(context, ViewExamResponsesActivity.class);
                intent.putExtra("courseId", course.getCourseId());
                intent.putExtra("groupNumber", finalX);
                context.startActivity(intent);
            });
            holder.btn_add_poll.setOnClickListener(v ->{
                Intent intent = new Intent(context, AddPollsActivity.class);
                intent.putExtra("courseId", course.getCourseId());
                context.startActivity(intent);
            });
            holder.btn_show_poll.setOnClickListener(v ->{
                Intent intent = new Intent(context, PollResultsActivity.class);
                intent.putExtra("courseId", course.getCourseId());
                context.startActivity(intent);
            });
            holder.btn_start_attendance.setOnClickListener(v ->{
                Intent intent = new Intent(context, StartAttendanceActivity.class);
                intent.putExtra("courseId", course.getCourseId());
                context.startActivity(intent);
            });
            holder.btn_join_attendance.setVisibility(View.GONE);
            holder.btn_create_report.setVisibility(View.GONE);
            holder.btn_create_exam.setOnClickListener(v ->{
                Intent intent = new Intent(context, CreateExamActivity.class);
                intent.putExtra("courseId", course.getCourseId());
                context.startActivity(intent);
            });
        }
        else{
            holder.groupNumbers.setText(x);
            holder.btn_add_student.setVisibility(View.GONE);
            holder.btn_add_poll.setVisibility(View.GONE);
            holder.btn_create_exam.setVisibility(View.GONE);
            holder.btn_show_responses.setVisibility(View.GONE);// Öğrenci ise butonu gizle
            holder.btn_start_attendance.setVisibility(View.GONE);
            holder.btn_add_poll.setVisibility(View.GONE);
            holder.btn_show_poll.setVisibility(View.GONE);// Öğrenci ise butonu gizle
        }

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseGroupViewHolder extends RecyclerView.ViewHolder {

        TextView courseName, courseId, courseDate, groupNumbers;
        Button btn_add_student,btn_add_poll, btn_create_exam, btn_show_responses;
        Button btn_show_poll,btn_start_attendance,btn_join_attendance,btn_create_report;

        public CourseGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_name);
            courseId = itemView.findViewById(R.id.course_id);
            courseDate = itemView.findViewById(R.id.course_date);
            groupNumbers = itemView.findViewById(R.id.group_numbers);
            btn_add_student = itemView.findViewById(R.id.btn_add_student);
            btn_add_poll = itemView.findViewById(R.id.btn_add_poll);
            btn_show_poll = itemView.findViewById(R.id.btn_show_poll);
            btn_start_attendance = itemView.findViewById(R.id.btn_start_attendance);
            btn_join_attendance = itemView.findViewById(R.id.btn_join_attendance);
            btn_create_report = itemView.findViewById(R.id.create_report);
            btn_create_exam  = itemView.findViewById(R.id.btn_create_exam);
            btn_show_responses = itemView.findViewById(R.id.show_responses_btn);
        }
    }
}
