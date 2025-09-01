package com.example.finalyearproject.Network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.finalyearproject.CameraModule.FrameBuffer;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FrameUploader {

    private static final String TAG = "TESTINGA";
    private final Context context;
    private final FrameBuffer frameBuffer;

    public FrameUploader(Context context, FrameBuffer frameBuffer) {
        this.context = context;
        this.frameBuffer = frameBuffer;
    }

    public void scheduleUploads(ScheduledExecutorService scheduler) {
        scheduler.scheduleAtFixedRate(this::uploadLatestFrames, 2, 5, TimeUnit.SECONDS);
    }

    private void uploadLatestFrames() {
        try {
            List<byte[]> frames = frameBuffer.takeLatest(10);

            if (frames.isEmpty()) {
                Log.d(TAG, "No frames to upload yet.");
                return;
            }

            List<MultipartBody.Part> parts = new ArrayList<>();
            int idx = 0;
            for (byte[] data : frames) {
                RequestBody req = RequestBody.create(data, MediaType.parse("image/jpeg"));
                MultipartBody.Part part = MultipartBody.Part.createFormData(
                        "images", "frame_" + (idx++) + ".jpg", req);
                parts.add(part);
                if(idx == 3)
                {
                    break;
                }

            }
            Log.e(TAG," IMAGES "+parts.size());

            // Add city field
            //RequestBody cityBody = RequestBody.create("Nashik", MediaType.parse("text/plain"));

            UploadService service = ApiClient.getClient().create(UploadService.class);
            Call<ResponseBody> call = service.uploadFrames(parts);

            call.enqueue(new Callback<ResponseBody>() {
                @Override public void onResponse(@NonNull Call<ResponseBody> call,
                                                 @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.i(TAG, "Upload OK: " + response.code());
                        JsonObject responseDemo = new JsonObject();
                        responseDemo.addProperty("name","kishan");
                        responseDemo.addProperty("age",22);
                        responseDemo.addProperty("city","Dhule");
                        responseDemo.addProperty("accuracy",98);



                        
                    } else {
                        Log.w(TAG, "Upload failed: " + response.code());

                    }
                }

                @Override public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.e(TAG, "Upload error: " + t.getMessage());
                }
            });

        } catch (Throwable t) {
            Log.e(TAG, "uploadLatestFrames error", t);
        }
    }

}
