package com.example.finalyearproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * AllReports Fragment - Displays employee performance dashboard
 */
public class AllReports extends Fragment {

    // UI Components
    private ImageButton notificationButton;
    private TextView employeesPresentCount, employeesOnLeaveCount;
    private TextView goodTab, averageTab, poorTab;
    private RecyclerView employeeRecyclerView;
    
    // Data
    private EmployeeAdapter employeeAdapter;
    private List<Employee> allEmployees;
    private List<Employee> filteredEmployees;
    private String currentFilter = "Good";

    public AllReports() {
        // Required empty public constructor
    }

    public static AllReports newInstance() {
        return new AllReports();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_reports, container, false);
        
        // Initialize UI components
        initializeViews(view);
        
        // Set up click listeners
        setupClickListeners();
        
        // Load data
        loadEmployeeData();
        
        // Set up RecyclerView
        setupRecyclerView();
        
        return view;
    }

    private void initializeViews(View view) {
        notificationButton = view.findViewById(R.id.notificationButton);
        employeesPresentCount = view.findViewById(R.id.employeesPresentCount);
        employeesOnLeaveCount = view.findViewById(R.id.employeesOnLeaveCount);
        goodTab = view.findViewById(R.id.goodTab);
        averageTab = view.findViewById(R.id.averageTab);
        poorTab = view.findViewById(R.id.poorTab);
        employeeRecyclerView = view.findViewById(R.id.employeeRecyclerView);
    }

    private void setupClickListeners() {
        // Notification button
        notificationButton.setOnClickListener(v -> {
            showToast("Notifications clicked");
            // TODO: Show notifications
        });

        // Performance tabs
        goodTab.setOnClickListener(v -> filterEmployees("Good"));
        averageTab.setOnClickListener(v -> filterEmployees("Average"));
        poorTab.setOnClickListener(v -> filterEmployees("Poor"));
    }

    private void loadEmployeeData() {
        // Initialize employee data
        allEmployees = new ArrayList<>();
        
        // Add sample employees
        allEmployees.add(new Employee("Jack Smith", "23232233", "Good", "30 Units per minute"));
        allEmployees.add(new Employee("Ethan Brown", "35434322", "Good", "20 Units per minute"));
        allEmployees.add(new Employee("Liam Johnson", "31303233", "Good", "24 Units per minute"));
        allEmployees.add(new Employee("Noah Wilson", "34212324", "Good", "30 Units per minute"));
        allEmployees.add(new Employee("Mason Taylor", "34332223", "Good", "30 Units per minute"));
        allEmployees.add(new Employee("Lucas Martinez", "34332224", "Good", "28 Units per minute"));
        allEmployees.add(new Employee("Oliver Anderson", "34332225", "Average", "15 Units per minute"));
        allEmployees.add(new Employee("Elijah Thomas", "34332226", "Average", "18 Units per minute"));
        allEmployees.add(new Employee("William Jackson", "34332227", "Average", "16 Units per minute"));
        allEmployees.add(new Employee("James White", "34332228", "Poor", "8 Units per minute"));
        allEmployees.add(new Employee("Benjamin Harris", "34332229", "Poor", "10 Units per minute"));
        allEmployees.add(new Employee("Sebastian Clark", "34332230", "Poor", "12 Units per minute"));
        
        filteredEmployees = new ArrayList<>(allEmployees);
        
        // Update statistics
        updateStatistics();
    }

    private void setupRecyclerView() {
        employeeAdapter = new EmployeeAdapter(filteredEmployees, employee -> {
            showToast("Selected: " + employee.getName());
            // TODO: Navigate to employee detail
        });
        
        employeeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        employeeRecyclerView.setAdapter(employeeAdapter);
    }

    private void filterEmployees(String filter) {
        currentFilter = filter;
        
        // Update tab appearances
        updateTabAppearances(filter);
        
        // Filter employees
        filteredEmployees.clear();
        for (Employee employee : allEmployees) {
            if (employee.getStatus().equals(filter)) {
                filteredEmployees.add(employee);
            }
        }
        
        // Update RecyclerView
        employeeAdapter.updateEmployees(filteredEmployees);
        
        // Update tab counts
        updateTabCounts();
    }

    private void updateTabAppearances(String activeFilter) {
        // Reset all tabs to inactive
        goodTab.setBackgroundResource(R.drawable.tab_background_inactive);
        goodTab.setTextColor(getResources().getColor(R.color.black));
        averageTab.setBackgroundResource(R.drawable.tab_background_inactive);
        averageTab.setTextColor(getResources().getColor(R.color.black));
        poorTab.setBackgroundResource(R.drawable.tab_background_inactive);
        poorTab.setTextColor(getResources().getColor(R.color.black));
        
        // Set active tab
        switch (activeFilter) {
            case "Good":
                goodTab.setBackgroundResource(R.drawable.tab_background_active);
                goodTab.setTextColor(getResources().getColor(R.color.white));
                break;
            case "Average":
                averageTab.setBackgroundResource(R.drawable.tab_background_active);
                averageTab.setTextColor(getResources().getColor(R.color.white));
                break;
            case "Poor":
                poorTab.setBackgroundResource(R.drawable.tab_background_active);
                poorTab.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    private void updateTabCounts() {
        int goodCount = 0, averageCount = 0, poorCount = 0;
        
        for (Employee employee : allEmployees) {
            switch (employee.getStatus()) {
                case "Good":
                    goodCount++;
                    break;
                case "Average":
                    averageCount++;
                    break;
                case "Poor":
                    poorCount++;
                    break;
            }
        }
        
        goodTab.setText("Good (" + goodCount + ")");
        averageTab.setText("Average (" + averageCount + ")");
        poorTab.setText("Poor (" + poorCount + ")");
    }

    private void updateStatistics() {
        int presentCount = 0, onLeaveCount = 0;
        
        for (Employee employee : allEmployees) {
            if (employee.getStatus().equals("Good") || employee.getStatus().equals("Average")) {
                presentCount++;
            } else {
                onLeaveCount++;
            }
        }
        
        employeesPresentCount.setText(String.valueOf(presentCount));
        employeesOnLeaveCount.setText(String.valueOf(onLeaveCount));
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when fragment becomes visible
        if (employeeAdapter != null) {
            employeeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up references
        notificationButton = null;
        employeesPresentCount = null;
        employeesOnLeaveCount = null;
        goodTab = null;
        averageTab = null;
        poorTab = null;
        employeeRecyclerView = null;
        employeeAdapter = null;
    }

    // Employee data class
    public static class Employee {
        private String name, id, status, performance;

        public Employee(String name, String id, String status, String performance) {
            this.name = name;
            this.id = id;
            this.status = status;
            this.performance = performance;
        }

        public String getName() { return name; }
        public String getId() { return id; }
        public String getStatus() { return status; }
        public String getPerformance() { return performance; }
    }

    // Employee adapter interface
    public interface EmployeeAdapterListener {
        void onEmployeeClicked(Employee employee);
    }
}