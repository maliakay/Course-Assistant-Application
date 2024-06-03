package com.example.courseassistantapplication.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.Report;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Report> reportList;

    public ReportAdapter(List<Report> reportList) {
        this.reportList = reportList;
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_list_item, parent, false);
        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReportViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.reportSubject.setText(report.getReportSubject());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String formattedDate = simpleDateFormat.format(report.getReportDate());
        holder.reportDate.setText(formattedDate);
        holder.reportScope.setText(report.getReportScope());
        holder.courseName.setText(report.getCourseName());
        holder.reportBody.setText(report.getReportBody());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder {
        public TextView reportSubject, reportDate, reportScope, courseName, recipient, reportBody;

        public ReportViewHolder(View view) {
            super(view);
            reportSubject = view.findViewById(R.id.reportSubject);
            reportDate = view.findViewById(R.id.reportDate);
            reportScope = view.findViewById(R.id.reportScope);
            courseName = view.findViewById(R.id.courseName);
            reportBody = view.findViewById(R.id.reportBody);
        }
    }
}

