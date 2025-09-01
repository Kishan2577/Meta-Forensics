package com.example.finalyearproject.Network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import java.util.List;

public interface UploadService {
    @Multipart
    @POST("authenticate")
    Call<ResponseBody> uploadFrames(@Part List<MultipartBody.Part> images);

}
