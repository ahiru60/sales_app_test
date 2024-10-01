package com.example.salesapp.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class ImagePicker {
    private Activity context;
    private FrameLayout cameraLayout;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageButton captureBtn;
    private ImageButton cameraFlipBtn;
    private PreviewView previewView;
    private ImageCapture imageCapture;
    private ImageView productImage;
    private Bitmap productImageBtmp;
    private int rotation = 0;
    private Fragment fragment;

    public ImagePicker(Fragment fragment,Activity context, FrameLayout cameraLayout, ListenableFuture<ProcessCameraProvider> cameraProviderFuture, ImageButton captureBtn, ImageButton cameraFlipBtn, PreviewView previewView, ImageCapture imageCapture, ImageView productImage) {
        this.fragment = fragment;
        this.context = context;
        this.cameraLayout = cameraLayout;
        this.cameraProviderFuture = cameraProviderFuture;
        this.captureBtn = captureBtn;
        this.cameraFlipBtn = cameraFlipBtn;
        this.previewView = previewView;
        this.imageCapture = imageCapture;
        this.productImage = productImage;
    }

    public void chooseImage(){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    final int[] camera_lens = {CameraSelector.LENS_FACING_BACK};
                    cameraLayout.setVisibility(View.VISIBLE);
                    cameraProviderFuture.addListener(() -> {
                        try {
                            rotation = 90;
                            ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                            startCamera(cameraProvider, camera_lens[0]);
                            captureBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    capturePhoto();
                                    cameraLayout.setVisibility(View.GONE);
                                }
                            });
                            cameraFlipBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(camera_lens[0] == CameraSelector.LENS_FACING_BACK){
                                        camera_lens[0] = CameraSelector.LENS_FACING_FRONT;
                                        rotation = -90;
                                        startCamera(cameraProvider, camera_lens[0]);
                                    }else{
                                        camera_lens[0] = CameraSelector.LENS_FACING_BACK;
                                        rotation = 90;
                                        startCamera(cameraProvider,camera_lens[0]);
                                    }

                                }
                            });

                        } catch (ExecutionException | InterruptedException e) {
                            Log.e("CameraXApp", "Failed to bind camera use cases", e);
                        }
                    }, ContextCompat.getMainExecutor(context));

                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    context.startActivityForResult(pickPhoto , 1);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }
    private Bitmap rotateBitmap(Bitmap sourceBitmap, float angle) {
        Bitmap bitmap = sourceBitmap;
        Bitmap rotateBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(rotateBitmap);
        Matrix matrix = new Matrix();
        matrix.postRotate(angle, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        canvas.drawBitmap(bitmap, matrix, null);
        return rotateBitmap;
    }
    private void startCamera(@NonNull ProcessCameraProvider cameraProvider, int cameraSelection) {
        // Preview Use Case
        Preview preview = new Preview.Builder().build();

        // Bind the Preview Use Case to the PreviewView
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        // Set up the ImageCapture use case
        imageCapture = new ImageCapture.Builder()
                .setTargetRotation(context.getWindowManager().getDefaultDisplay().getRotation())
                .build();
        // Select the back camera
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(cameraSelection)
                .build();

        // Unbind all use cases before rebinding
        cameraProvider.unbindAll();

        // Bind use cases to the camera with the lifecycle
        Camera camera = cameraProvider.bindToLifecycle(fragment, cameraSelector, preview, imageCapture);
    }
    private void capturePhoto() {
        // Create a temporary file for the captured image
        File photoFile = new File(context.getExternalFilesDir(null), "photo.jpg");

        // Set up the output options for ImageCapture
        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        // Capture the photo and handle the result
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(context),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        // Load the captured image into the ImageView
                        productImageBtmp = rotateBitmap(BitmapFactory.decodeFile(photoFile.getAbsolutePath()),rotation);
                        productImage.setImageBitmap(productImageBtmp);
                        Toast.makeText(context, "Photo captured successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Log.e("CameraXApp", "Photo capture failed: " + exception.getMessage(), exception);
                        Toast.makeText(context, "Failed to capture photo", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
