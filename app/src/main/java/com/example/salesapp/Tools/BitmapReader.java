package com.example.salesapp.Tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class BitmapReader {
    public static String saveImage(Bitmap finalBitmap, String productName) {
        if(finalBitmap != null){
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/SalesApp/images/";
            path = path + productName.toLowerCase() + ".jpg";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();  // This will create the folder and any necessary parent directories
            }
            if (file.exists()) file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return path;
        }
        return "";
    }

    public static Bitmap readImage(String ImageURL, int quality, int maxWidth, int maxHeight) {
        Bitmap bitmap = null;
        if (!ImageURL.equals("")) {
            File imgFile = new File(ImageURL);

            if (imgFile.exists()) {
                bitmap = BitmapQualityReducer.reduceBitmapQualityAndSize(BitmapFactory.decodeFile(imgFile.getAbsolutePath()), quality, maxWidth, maxHeight);
            }
        }
        return bitmap;
    }
}
