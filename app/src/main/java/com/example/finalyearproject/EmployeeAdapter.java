package com.example.finalyearproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<AllReports.Employee> employees;
    private final AllReports.EmployeeAdapterListener listener;

    public EmployeeAdapter(List<AllReports.Employee> employees, AllReports.EmployeeAdapterListener listener) {
        this.employees = employees;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_list_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        AllReports.Employee employee = employees.get(position);
        holder.bind(employee);
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public void updateEmployees(List<AllReports.Employee> newEmployees) {
        this.employees = newEmployees;
        notifyDataSetChanged();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {
        private final ImageView employeeProfilePicture;
        private final TextView employeeName, employeeId, employeeStatus, employeePerformance;
        private final View statusDot;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeProfilePicture = itemView.findViewById(R.id.employeeProfilePicture);
            employeeName = itemView.findViewById(R.id.employeeName);
            employeeId = itemView.findViewById(R.id.employeeId);
            employeeStatus = itemView.findViewById(R.id.employeeStatus);
            employeePerformance = itemView.findViewById(R.id.employeePerformance);
            statusDot = itemView.findViewById(R.id.statusDot);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEmployeeClicked(employees.get(position));
                }
            });
        }

        public void bind(AllReports.Employee employee) {
            employeeName.setText(employee.getName());
            employeeId.setText(employee.getId());
            employeeStatus.setText(employee.getStatus() + " Status");
            employeePerformance.setText(employee.getPerformance());

            // Set status dot color based on performance
            switch (employee.getStatus()) {
                case "Good":
                    statusDot.setBackgroundResource(R.drawable.status_dot_good);
                    employeeStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.statusGood));
                    break;
                case "Average":
                    statusDot.setBackgroundResource(R.drawable.status_dot_average);
                    employeeStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.statusAverage));
                    break;
                case "Poor":
                    statusDot.setBackgroundResource(R.drawable.status_dot_poor);
                    employeeStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.statusPoor));
                    break;
            }
        }
    }
}
