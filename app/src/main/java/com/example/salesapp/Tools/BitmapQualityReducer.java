package com.example.salesapp.Tools;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;

public class BitmapQualityReducer {

    public static Bitmap reduceBitmapQualityAndSize(Bitmap originalBitmap, int quality, int maxWidth, int maxHeight) {
        if (originalBitmap == null) {
            return null;
        }

        // Resize the bitmap
        Bitmap resizedBitmap = resizeBitmap(originalBitmap, maxWidth, maxHeight);

        // Compress the resized bitmap to JPEG format with the specified quality
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

        // Convert the ByteArrayOutputStream to a byte array
        byte[] bitmapData = outputStream.toByteArray();

        // Decode the compressed byte array back into a bitmap
        return BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
    }

    private static Bitmap resizeBitmap(Bitmap originalBitmap, int maxWidth, int maxHeight) {
        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();

        // Calculate the aspect ratio
        float aspectRatio = (float) originalWidth / originalHeight;

        // Calculate the new dimensions
        int newWidth = maxWidth;
        int newHeight = (int) (maxWidth / aspectRatio);

        if (newHeight > maxHeight) {
            newHeight = maxHeight;
            newWidth = (int) (maxHeight * aspectRatio);
        }

        // Resize the bitmap
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        return resizedBitmap;
    }
}

