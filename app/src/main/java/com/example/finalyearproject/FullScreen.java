package com.example.finalyearproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalyearproject.CameraModule.CameraManager;
import com.example.finalyearproject.CameraModule.FrameBuffer;
import com.example.finalyearproject.Network.FrameUploader;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class FullScreen extends AppCompatActivity {

    private PreviewView previewView;
    //private Button startButton;

    private ExecutorService cameraExecutor;
    private ScheduledExecutorService scheduler;

    private FrameBuffer frameBuffer;
    private CameraManager cameraManager;
    private FrameUploader frameUploader;

    private boolean cameraStarted = false;


    FloatingActionButton zoom_in;
        private final ActivityResultLauncher<String> requestCameraPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startCamera();
                } else {
                    Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_full_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        previewView = findViewById(R.id.previewView);
        zoom_in = findViewById(R.id.zoom_in);

        zoom_in.setOnClickListener(v->{
            startActivity(new Intent(FullScreen.this, MainActivity.class));
        });


        cameraExecutor = Executors.newSingleThreadExecutor();
        scheduler = Executors.newSingleThreadScheduledExecutor();

        frameBuffer = new FrameBuffer(100);
        cameraManager = new CameraManager(this, previewView, cameraExecutor, frameBuffer);
        frameUploader = new FrameUploader(this, frameBuffer);

        startCamera();
//        startButton.setOnClickListener(v -> {
//            if (!hasCameraPermission()) {
//                requestCameraPermission.launch(android.Manifest.permission.CAMERA);
//            } else if (!cameraStarted) {
//
//            } else {
//                Toast.makeText(this, "Camera already running & uploading...", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void startCamera() {
        cameraManager.startCamera(() -> {
            cameraStarted = true;
            frameUploader.scheduleUploads(scheduler); // upload every 5 sec
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCameraAndUploads(); // stop fully when leaving fullscreen
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCameraAndUploads();
        cameraExecutor.shutdown();
    }

    private void stopCameraAndUploads() {
        if (cameraStarted) {
            cameraManager.stopCamera();
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdownNow();
            }
            scheduler = Executors.newSingleThreadScheduledExecutor();
            cameraStarted = false;
        }
    }

}