package com.example.finalyearproject.CameraModule;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutorService;

public class CameraManager {

    private static final String TAG = "CameraManager";

    private final Context context;
    private final PreviewView previewView;
    private final ExecutorService cameraExecutor;
    private final FrameBuffer frameBuffer;

    private ProcessCameraProvider cameraProvider;
    public CameraManager(Context context, PreviewView previewView,
                         ExecutorService cameraExecutor, FrameBuffer frameBuffer) {
        this.context = context;
        this.previewView = previewView;
        this.cameraExecutor = cameraExecutor;
        this.frameBuffer = frameBuffer;
    }

    public void startCamera(Runnable onCameraStarted) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(context);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                ImageAnalysis analysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                        .build();

                analysis.setAnalyzer(cameraExecutor, this::onFrameAvailable);

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle((androidx.lifecycle.LifecycleOwner) context,
                        cameraSelector, preview, analysis);

                Toast.makeText(context, "Camera started", Toast.LENGTH_SHORT).show();
                onCameraStarted.run();

            } catch (Exception e) {
                Log.e(TAG, "Failed to start camera", e);
                Toast.makeText(context, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, ContextCompat.getMainExecutor(context));
    }

    public void stopCamera() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
            Toast.makeText(context, "Camera stopped", Toast.LENGTH_SHORT).show();
        } else {
            Log.w(TAG, "stopCamera called but cameraProvider is null");
        }
    }



    private void onFrameAvailable(@NonNull ImageProxy image) {
        try {
            byte[] jpegBytes = ImageUtils.imageProxyToJpeg(image, 100);
            if (jpegBytes != null) {
                frameBuffer.add(jpegBytes);
            }
        } catch (Throwable t) {
            Log.w(TAG, "Frame conversion failed: " + t.getMessage());
        } finally {
            image.close();
        }
    }
}
