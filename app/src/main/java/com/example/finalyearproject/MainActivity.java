package com.example.finalyearproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.finalyearproject.CameraModule.CameraManager;
import com.example.finalyearproject.CameraModule.FrameBuffer;
import com.example.finalyearproject.Network.FrameUploader;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MainActivity extends AppCompatActivity {

//    private PreviewView previewView;
//    private Button startButton;
//
//    private ExecutorService cameraExecutor;
//    private ScheduledExecutorService scheduler;
//
//    private FrameBuffer frameBuffer;
//    private CameraManager cameraManager;
//    private FrameUploader frameUploader;
//
//    private boolean cameraStarted = false;

    BottomNavigationView bottomNavigationView;
//    private final ActivityResultLauncher<String> requestCameraPermission =
//            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//                if (isGranted) {
//                    startCamera();
//                } else {
//                    Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
//                }
//            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new Home())
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_item1) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Home())
                        .commit();
                return true;
            } else if (id == R.id.nav_item2) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new LinkTree())
                        .commit();
                return true;
            } else if (id == R.id.nav_item3) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Profile())
                        .commit();
                return true;
            }
            else if (id == R.id.nav_item4) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AllReports())
                    .commit();
            return true;
        }

            return false;
        });


//        previewView = findViewById(R.id.previewView);
//        startButton = findViewById(R.id.startButton);
//
//        cameraExecutor = Executors.newSingleThreadExecutor();
//        scheduler = Executors.newSingleThreadScheduledExecutor();
//
//        frameBuffer = new FrameBuffer(100);
//        cameraManager = new CameraManager(this, previewView, cameraExecutor, frameBuffer);
//        frameUploader = new FrameUploader(this, frameBuffer);
//
//        startButton.setOnClickListener(v -> {
//            if (!hasCameraPermission()) {
//                requestCameraPermission.launch(Manifest.permission.CAMERA);
//            } else if (!cameraStarted) {
//                startCamera();
//            } else {
//                Toast.makeText(this, "Camera already running & uploading...", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

//    private boolean hasCameraPermission() {
//        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void startCamera() {
//        cameraManager.startCamera(() -> {
//            cameraStarted = true;
//            frameUploader.scheduleUploads(scheduler); // upload every 5 sec
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        cameraExecutor.shutdown();
//        scheduler.shutdown();
//    }
}
