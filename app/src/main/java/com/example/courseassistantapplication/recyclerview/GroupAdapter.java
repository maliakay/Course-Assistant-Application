package com.example.courseassistantapplication.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseassistantapplication.R;
import com.example.courseassistantapplication.model.Group;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private List<Group> groupList;
    private List<String> instructorEmails;

    public GroupAdapter(List<Group> groupList) {
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groupList.get(position);
        holder.groupName.setText(group.getGroupNumber());

        if (instructorEmails != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(holder.itemView.getContext(), android.R.layout.simple_spinner_item, instructorEmails);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.instructorSpinner.setAdapter(adapter);

            holder.instructorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    String selectedInstructor = (String) parent.getItemAtPosition(pos);
                    group.setInstructorEmail(selectedInstructor);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            // Set the spinner selection to the current instructor email if it exists
            if (group.getInstructorEmail() != null) {
                int spinnerPosition = adapter.getPosition(group.getInstructorEmail());
                holder.instructorSpinner.setSelection(spinnerPosition);
            }
        }
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public void setInstructorEmails(List<String> instructorEmails) {
        this.instructorEmails = instructorEmails;
        notifyDataSetChanged();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        TextView groupName;
        Spinner instructorSpinner;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.group_name);
            instructorSpinner = itemView.findViewById(R.id.instructor_spinner);
        }
    }
}
