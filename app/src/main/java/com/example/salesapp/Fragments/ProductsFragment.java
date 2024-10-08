package com.example.salesapp.Fragments;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.salesapp.Database.DbHandler;
import com.example.salesapp.MainActivity;
import com.example.salesapp.R;
import com.example.salesapp.Tools.BitmapReader;
import com.example.salesapp.Tools.ImagePicker;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "ProductsFragment";

    private View view;
    private EditText productName,stock, cost, sellingPrice;
    private Button addProductBtn, clearBtn;
    private ImageView productImage;
    private DbHandler dbHandler;
    private Bitmap productImageBtmp;
    private MainActivity mainActivity;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private PreviewView previewView;
    private ImageCapture imageCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private FrameLayout cameraLayout;
    private ImageButton captureBtn,cameraFlipBtn;
    private int rotation = 0;
    private String mParam1;
    private String mParam2;

    public ProductsFragment() {
        // Required empty public constructor
    }
    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_products, container, false);
        initDb();
        initViews();
        setupBtn();
        return view;
    }

    private void initDb() {
        dbHandler = new DbHandler(getContext());
    }

    private void setupBtn() {
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName.setText("");
                stock.setText("");
                cost.setText("");
                sellingPrice.setText("");
            }
        });
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap image = productImageBtmp;
                String productStr = productName.getText().toString();
                String stockStr = stock.getText().toString();
                String costStr = cost.getText().toString();
                String sellingPriceStr = sellingPrice.getText().toString();
                if(productStr.equals("") || stockStr.equals("") || costStr.equals("") || sellingPriceStr.equals("")){
                    Toast.makeText(getContext(),"Fill all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    String ImageURL= "";
                    if(image != null){
                        ImageURL =  BitmapReader.saveImage(image,productStr);
                    }

                    dbHandler.addItemtoItems(ImageURL,productStr,stockStr,costStr,sellingPriceStr);
                    Toast.makeText(getContext(),productStr +" added succenssfully", Toast.LENGTH_SHORT).show();
                    productName.setText("");
                    stock.setText("");
                    cost.setText("");
                    sellingPrice.setText("");
                    mainActivity.backFragments();
                }

                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .remove(new ProductsFragment())
                        .commit();
                fragmentManager.popBackStack();

                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, HomeFragment.class,null,HomeFragment.TAG)
                        .setReorderingAllowed(true)
                        .addToBackStack("HomeFragment")
                        .commit();


            }
        });
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (ContextCompat.checkSelfPermission(
                            getContext(), Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                            getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED) {
                        // You can use the API that requires the permission.
                        chooseImage();
                    } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                            getActivity(), Manifest.permission.CAMERA)) {
                        Toast.makeText(mainActivity, "permission has denied", Toast.LENGTH_SHORT).show();
                    } else {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        requestMultiplePermissions.launch(
                                new String[]{
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                });
                    }
            }
        });
    }

    private void initViews() {
        mainActivity = (MainActivity) getActivity();
        previewView = view.findViewById(R.id.previewView);
        cameraLayout = view.findViewById(R.id.capture_camera);
        captureBtn = view.findViewById(R.id.captureBtn);
        cameraFlipBtn = view.findViewById(R.id.cameraChange);
        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        productImage = view.findViewById(R.id.productImage);
        productName = view.findViewById(R.id.editText_productName);
        stock = view.findViewById(R.id.editText_stock);
        cost = view.findViewById(R.id.editText_cost);
        sellingPrice = view.findViewById(R.id.editText_sellingPrice);
        addProductBtn = view.findViewById(R.id.Btn_addProducts);
        clearBtn = view.findViewById(R.id.Btn_clear);
        String productStr = productName.getText().toString();
        String stockStr = stock.getText().toString();
        String costStr = cost.getText().toString();
        String sellingPriceStr = sellingPrice.getText().toString();
    }

    // Handled permission Result
    private ActivityResultLauncher<String[]> requestMultiplePermissions = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
                Boolean cameraPermissionGranted = result.getOrDefault(Manifest.permission.CAMERA, false);
                Boolean storagePermissionGranted = result.getOrDefault(Manifest.permission.WRITE_EXTERNAL_STORAGE, false);

                if (cameraPermissionGranted && storagePermissionGranted) {
                    chooseImage();
                } else {
                    if (!cameraPermissionGranted) {
                        Toast.makeText(getActivity(), "FlagUp Requires Access to Camera.", Toast.LENGTH_SHORT).show();
                    }
                    if (!storagePermissionGranted) {
                        Toast.makeText(getActivity(), "FlagUp Requires Access to Your Storage.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    private void chooseImage(){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                    }, ContextCompat.getMainExecutor(getContext()));

                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    getActivity().startActivityForResult(pickPhoto , 1);
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
                .setTargetRotation(getActivity().getWindowManager().getDefaultDisplay().getRotation())
                .build();
        // Select the back camera
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(cameraSelection)
                .build();

        // Unbind all use cases before rebinding
        cameraProvider.unbindAll();

        // Bind use cases to the camera with the lifecycle
        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }
    private void capturePhoto() {
        // Create a temporary file for the captured image
        File photoFile = new File(getActivity().getExternalFilesDir(null), "photo.jpg");

        // Set up the output options for ImageCapture
        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        // Capture the photo and handle the result
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(getActivity()),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        // Load the captured image into the ImageView
                        productImageBtmp = rotateBitmap(BitmapFactory.decodeFile(photoFile.getAbsolutePath()),rotation);
                        productImage.setImageBitmap(productImageBtmp);
                        Toast.makeText(getContext(), "Photo captured successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Log.e("CameraXApp", "Photo capture failed: " + exception.getMessage(), exception);
                        Toast.makeText(getContext(), "Failed to capture photo", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}