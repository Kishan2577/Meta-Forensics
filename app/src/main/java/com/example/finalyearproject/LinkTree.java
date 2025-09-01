package com.example.finalyearproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.example.finalyearproject.LinkTreeHelper.BranchLinesView;

import java.util.ArrayList;
import java.util.List;

public class LinkTree extends Fragment {

    private ImageButton centerButton;
    private ImageButton[] branchButtons = new ImageButton[10];
    private BranchLinesView branchLinesView;
    private boolean isExpanded = false;
    private Handler handler = new Handler(Looper.getMainLooper());

    // Dialog components
    private Dialog cardDialog;
    private TextView dialogCardTitle, dialogCardDescription, dialogCardStatus, dialogCardTime;
    private ImageView dialogCardIcon;
    private Button dialogCardActionButton1, dialogCardActionButton2;
    private ImageButton dialogCloseButton;

    // Track which button's dialog is currently open
    private int currentOpenDialog = -1;

    // Button labels
    private final String[] buttonLabels = {
            "Camera", "Call", "Edit", "Send", "Share",
            "Gallery", "Zoom", "Help", "Settings", "Preferences"
    };

    // Demo data
    private final String[][] demoData = {
            {"Camera", "Capture photos and videos with high quality. Supports multiple modes including portrait, night, and pro mode.", "Status: Active", "2 min ago", "Take Photo", "Record Video"},
            {"Call", "Make voice and video calls to contacts. Supports conference calls and call recording features.", "Status: Available", "5 min ago", "Call Now", "Schedule Call"},
            {"Edit", "Edit documents, images, and files with professional tools. Supports multiple formats and collaboration.", "Status: Busy", "1 min ago", "Edit File", "Share Edit"},
            {"Send", "Send messages, emails, and files to contacts. Supports scheduled sending and delivery reports.", "Status: Online", "3 min ago", "Send Now", "Schedule Send"},
            {"Share", "Share content across multiple platforms. Supports social media, cloud storage, and direct sharing.", "Status: Active", "4 min ago", "Share Now", "Copy Link"},
            {"Gallery", "Browse and organize your photos and videos. Supports albums, tags, and cloud backup.", "Status: Available", "6 min ago", "View Album", "Create Album"},
            {"Zoom", "Zoom in and out of content with smooth transitions. Supports gesture controls and accessibility.", "Status: Active", "2 min ago", "Zoom In", "Zoom Out"},
            {"Help", "Get assistance and support for all features. Includes tutorials, FAQs, and live chat support.", "Status: Online", "1 min ago", "Get Help", "Contact Support"},
            {"Settings", "Customize app preferences and configurations. Manage privacy, notifications, and appearance.", "Status: Available", "3 min ago", "General", "Advanced"},
            {"Preferences", "Set personal preferences and customize your experience. Includes themes, shortcuts, and layouts.", "Status: Active", "5 min ago", "Save Prefs", "Reset Defaults"}
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ Handle back press properly in Fragment
        requireActivity().getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (currentOpenDialog != -1) {
                            hideDialog();
                        } else if (isExpanded) {
                            collapseMenu();
                        } else {
                            setEnabled(false);
                            requireActivity().onBackPressed();
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_link_tree, container, false);

        centerButton = view.findViewById(R.id.centerButton);
        branchLinesView = view.findViewById(R.id.branchLines);

        // Initialize dialog
        initializeDialog();

        // Map buttons
        for (int i = 0; i < 10; i++) {
            String id = "btn" + (i + 1);
            int resId = getResources().getIdentifier(id, "id", requireContext().getPackageName());
            branchButtons[i] = view.findViewById(resId);

            branchButtons[i].setVisibility(View.INVISIBLE);
            branchButtons[i].setAlpha(0f);
            branchButtons[i].setScaleX(0.5f);
            branchButtons[i].setScaleY(0.5f);

            final int buttonIndex = i;
            branchButtons[i].setOnClickListener(v -> onBranchButtonClick(buttonIndex));
            branchButtons[i].setOnLongClickListener(v -> {
                Toast.makeText(requireContext(), buttonLabels[buttonIndex], Toast.LENGTH_SHORT).show();
                return true;
            });
        }

        centerButton.post(() -> {
            float cx = centerButton.getX() + centerButton.getWidth() / 2f;
            float cy = centerButton.getY() + centerButton.getHeight() / 2f;
            branchLinesView.setCenter(cx, cy);
            centerButton.bringToFront();
        });

        centerButton.setOnClickListener(v -> toggleMenu());

        return view;
    }

    private void initializeDialog() {
        cardDialog = new Dialog(requireContext());
        cardDialog.setContentView(R.layout.dialog_card_view);

        Window window = cardDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setGravity(Gravity.CENTER);
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        dialogCardTitle = cardDialog.findViewById(R.id.dialogCardTitle);
        dialogCardDescription = cardDialog.findViewById(R.id.dialogCardDescription);
        dialogCardStatus = cardDialog.findViewById(R.id.dialogCardStatus);
        dialogCardTime = cardDialog.findViewById(R.id.dialogCardTime);
        dialogCardIcon = cardDialog.findViewById(R.id.dialogCardIcon);
        dialogCardActionButton1 = cardDialog.findViewById(R.id.dialogCardActionButton1);
        dialogCardActionButton2 = cardDialog.findViewById(R.id.dialogCardActionButton2);
        dialogCloseButton = cardDialog.findViewById(R.id.dialogCloseButton);

        dialogCloseButton.setOnClickListener(v -> hideDialog());

        dialogCardActionButton1.setOnClickListener(v ->
                Toast.makeText(requireContext(), "Action 1 clicked!", Toast.LENGTH_SHORT).show());

        dialogCardActionButton2.setOnClickListener(v ->
                Toast.makeText(requireContext(), "Action 2 clicked!", Toast.LENGTH_SHORT).show());

        cardDialog.setCancelable(true);
        cardDialog.setOnCancelListener(dialog -> hideDialog());
    }

    private void onBranchButtonClick(int buttonIndex) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            centerButton.performHapticFeedback(android.view.HapticFeedbackConstants.CONTEXT_CLICK);
        }

        if (currentOpenDialog == buttonIndex) {
            hideDialog();
        } else {
            showCardDialog(buttonIndex);
        }

        branchButtons[buttonIndex].animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction(() -> branchButtons[buttonIndex].animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start())
                .start();
    }

    private void showCardDialog(int buttonIndex) {
        if (currentOpenDialog != -1) hideDialog();

        String[] data = demoData[buttonIndex];
        dialogCardTitle.setText(data[0]);
        dialogCardDescription.setText(data[1]);
        dialogCardStatus.setText(data[2]);
        dialogCardTime.setText(data[3]);
        dialogCardActionButton1.setText(data[4]);
        dialogCardActionButton2.setText(data[5]);
        dialogCardIcon.setImageResource(getButtonIcon(buttonIndex));
        cardDialog.setCanceledOnTouchOutside(false);

        cardDialog.show();
        currentOpenDialog = buttonIndex;
    }

    private void hideDialog() {
        if (currentOpenDialog != -1 && cardDialog.isShowing()) {
            cardDialog.dismiss();
            currentOpenDialog = -1;
        }
    }

    private int getButtonIcon(int buttonIndex) {
        switch (buttonIndex) {
            case 0: return android.R.drawable.ic_menu_camera;
            case 1: return android.R.drawable.ic_menu_call;
            case 2: return android.R.drawable.ic_menu_edit;
            case 3: return android.R.drawable.ic_menu_send;
            case 4: return android.R.drawable.ic_menu_share;
            case 5: return android.R.drawable.ic_menu_gallery;
            case 6: return android.R.drawable.ic_menu_zoom;
            case 7: return android.R.drawable.ic_menu_help;
            case 8: return android.R.drawable.ic_menu_manage;
            case 9: return android.R.drawable.ic_menu_preferences;
            default: return android.R.drawable.ic_menu_zoom;
        }
    }

    private void toggleMenu() {
        if (isExpanded) collapseMenu();
        else expandMenu();
    }

    private void expandMenu() {
        isExpanded = true;
        hideDialog();

        WindowManager wm = (WindowManager) requireContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        float screenWidth = displayMetrics.widthPixels;
        float screenHeight = displayMetrics.heightPixels;

        float horizontalDist = getResponsiveHorizontalDistance(screenWidth);
        float verticalStep = getResponsiveVerticalStep(screenHeight);
        float startY = -2 * verticalStep;

        float cx = centerButton.getX() + centerButton.getWidth() / 2f;
        float cy = centerButton.getY() + centerButton.getHeight() / 2f;

        List<float[]> positions = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            float x, y;
            if (i < 5) {
                x = -horizontalDist;
                y = startY + i * verticalStep;
            } else {
                x = horizontalDist;
                y = startY + (i - 5) * verticalStep;
            }
            positions.add(new float[]{cx + x, cy + y});
        }

        branchLinesView.setBranches(positions);

        handler.postDelayed(() -> {
            for (int i = 0; i < branchButtons.length; i++) {
                final int buttonIndex = i;
                handler.postDelayed(() ->
                        animateButtonAppear(branchButtons[buttonIndex],
                                positions.get(buttonIndex)[0] - cx,
                                positions.get(buttonIndex)[1] - cy), i * 120);
            }
        }, 800);
    }

    private float getResponsiveHorizontalDistance(float screenWidth) {
        if (screenWidth < 480) return screenWidth * 0.25f;
        else if (screenWidth < 720) return screenWidth * 0.30f;
        else if (screenWidth < 960) return screenWidth * 0.35f;
        else return Math.min(screenWidth * 0.40f, 500f);
    }

    private float getResponsiveVerticalStep(float screenHeight) {
        if (screenHeight < 640) return screenHeight * 0.10f;
        else if (screenHeight < 960) return screenHeight * 0.12f;
        else if (screenHeight < 1280) return screenHeight * 0.14f;
        else return Math.min(screenHeight * 0.16f, 300f);
    }

    private void collapseMenu() {
        isExpanded = false;
        hideDialog();

        for (int i = 0; i < branchButtons.length; i++) {
            final int buttonIndex = i;
            handler.postDelayed(() -> animateButtonDisappear(branchButtons[buttonIndex]), i * 50);
        }

        handler.postDelayed(() -> branchLinesView.clearBranches(), 400);
    }

    private void animateButtonAppear(View button, float targetX, float targetY) {
        button.setVisibility(View.VISIBLE);
        button.setTranslationX(targetX);
        button.setTranslationY(targetY);
        button.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500)
                .setInterpolator(new OvershootInterpolator(1.2f))
                .start();
    }

    private void animateButtonDisappear(View button) {
        button.animate()
                .alpha(0f)
                .scaleX(0.5f)
                .scaleY(0.5f)
                .setDuration(300)
                .withEndAction(() -> {
                    button.setVisibility(View.INVISIBLE);
                    button.setTranslationX(0);
                    button.setTranslationY(0);
                    button.setScaleX(0.5f);
                    button.setScaleY(0.5f);
                })
                .start();
    }
}
