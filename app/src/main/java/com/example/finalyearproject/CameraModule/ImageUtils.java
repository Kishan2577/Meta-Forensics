package com.example.finalyearproject.CameraModule;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;

import androidx.camera.core.ImageProxy;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class ImageUtils {

    public static byte[] imageProxyToJpeg(ImageProxy image, int quality) {
        if (image.getFormat() != ImageFormat.YUV_420_888) return null;

        // Convert YUV_420_888 -> NV21
        byte[] nv21 = yuv420888ToNv21(image);

        YuvImage yuvImage = new YuvImage(
                nv21,
                ImageFormat.NV21,
                image.getWidth(),
                image.getHeight(),
                null
        );

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), quality, out);
        return out.toByteArray();
    }

    private static byte[] yuv420888ToNv21(ImageProxy image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int ySize = width * height;
        int uvSize = width * height / 4;

        byte[] nv21 = new byte[ySize + uvSize * 2];

        ByteBuffer yBuffer = image.getPlanes()[0].getBuffer(); // Y
        ByteBuffer uBuffer = image.getPlanes()[1].getBuffer(); // U
        ByteBuffer vBuffer = image.getPlanes()[2].getBuffer(); // V

        int rowStride = image.getPlanes()[0].getRowStride();
        int pos = 0;

        // Copy Y channel
        for (int row = 0; row < height; row++) {
            yBuffer.position(row * rowStride);
            yBuffer.get(nv21, pos, width);
            pos += width;
        }

        // Copy UV interleaved (VU order for NV21)
        int chromaRowStride = image.getPlanes()[1].getRowStride();
        int chromaPixelStride = image.getPlanes()[1].getPixelStride();

        for (int row = 0; row < height / 2; row++) {
            for (int col = 0; col < width / 2; col++) {
                int uIndex = row * chromaRowStride + col * chromaPixelStride;
                int vIndex = row * image.getPlanes()[2].getRowStride() + col * image.getPlanes()[2].getPixelStride();

                nv21[pos++] = vBuffer.get(vIndex);
                nv21[pos++] = uBuffer.get(uIndex);
            }
        }

        return nv21;
    }
}
