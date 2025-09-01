package com.example.finalyearproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * Profile Fragment - Displays user profile information with responsive design
 */
public class Profile extends Fragment {

    // UI Components
    private ImageButton backButton, logoutButton;
    private TextView profileName, profileEmail;
    private LinearLayout usernameItem, notificationsItem, settingsItem;
    
    // Statistics data
    private TextView activeCount, pendingCount, completeCount;

    public Profile() {
        // Required empty public constructor
    }

    public static Profile newInstance() {
        return new Profile();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        // Initialize UI components
        initializeViews(view);
        
        // Set up click listeners
        setupClickListeners();
        
        // Load profile data
        loadProfileData();
        
        return view;
    }

    private void initializeViews(View view) {
        // Navigation buttons
        backButton = view.findViewById(R.id.backButton);
        logoutButton = view.findViewById(R.id.logoutButton);
        
        // Profile information
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        
        // Statistics cards
        activeCount = view.findViewById(R.id.activeCount);
        pendingCount = view.findViewById(R.id.pendingCount);
        completeCount = view.findViewById(R.id.completeCount);
        
        // Settings items
        usernameItem = view.findViewById(R.id.usernameItem);
        notificationsItem = view.findViewById(R.id.notificationsItem);
        settingsItem = view.findViewById(R.id.settingsItem);
    }

    private void setupClickListeners() {
        // Back button
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        // Logout button
        logoutButton.setOnClickListener(v -> {
            showLogoutDialog();
        });

        // Username settings
        usernameItem.setOnClickListener(v -> {
            showToast("Username settings clicked");
            // TODO: Navigate to username settings
        });

        // Notifications settings
        notificationsItem.setOnClickListener(v -> {
            showToast("Notifications settings clicked");
            // TODO: Navigate to notifications settings
        });

        // General settings
        settingsItem.setOnClickListener(v -> {
            showToast("Settings clicked");
            // TODO: Navigate to general settings
        });
    }

    private void loadProfileData() {
        // Load user profile data
        // In a real app, this would come from a database or API
        
        // Load statistics
        updateStatistics(14, 6, 25);
    }

    private void updateStatistics(int active, int pending, int completed) {
        // Update statistics display
        if (activeCount != null) activeCount.setText(String.valueOf(active));
        if (pendingCount != null) pendingCount.setText(String.valueOf(pending));
        if (completeCount != null) completeCount.setText(String.valueOf(completed));
    }

    private void showLogoutDialog() {
        // Show logout confirmation dialog
        if (getActivity() != null) {
            new android.app.AlertDialog.Builder(getActivity())
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // TODO: Implement logout logic
                        showToast("Logging out...");
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
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
        loadProfileData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up references
        backButton = null;
        logoutButton = null;
        profileName = null;
        profileEmail = null;
        usernameItem = null;
        notificationsItem = null;
        settingsItem = null;
    }
}