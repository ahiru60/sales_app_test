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
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
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

import com.example.salesapp.Database.DbHandler;
import com.example.salesapp.MainActivity;
import com.example.salesapp.Models.User;
import com.example.salesapp.R;
import com.example.salesapp.Tools.BitmapReader;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
public class AddUserFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "AddUserFragment";
    private String mParam1;
    private String mParam2;
    private User user;
    private HomeFragment homeFragment;
    private FragmentManager fragmentManager;
    private EditText firstName, lastName, gender, location;
    private Button clear, addUser;
    private View actionBar;
    private ImageButton imageButton;
    private View view;
    private ImageButton backArrow, actionBarAddUserBtn;
    private ImageView userImage;
    private MainActivity mainActivity;
    private LinearLayout viewCartBtn;
    private Bitmap userImageBtmp;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private PreviewView previewView;
    private ImageCapture imageCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private FrameLayout cameraLayout;
    private ImageButton captureBtn, cameraFlipBtn;
    private int rotation = 0;
    private int quality = 90;
    private int maxWidth = 250;
    private int maxHeight = 250;

    public AddUserFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                mainActivity.homeSetActions(true);
                fragmentManager.beginTransaction()
                        .remove(new AddUserFragment())
                        .commit();
                fragmentManager.popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_user, container, false);
        homeFragment = (HomeFragment) getActivity().getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
        user = homeFragment.getUser();
        setupViews();
        return view;
    }

    private void setupViews() {
        fragmentManager = getParentFragmentManager();
        mainActivity = (MainActivity) getActivity();
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        previewView = view.findViewById(R.id.previewView);
        cameraLayout = view.findViewById(R.id.capture_camera);
        captureBtn = view.findViewById(R.id.captureBtn);
        cameraFlipBtn = view.findViewById(R.id.cameraChange);
        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        actionBar = mainActivity.getSupportActionBar().getCustomView();
        viewCartBtn = actionBar.findViewById(R.id.viewCartBtn);
        viewCartBtn.setVisibility(View.GONE);
        userImage = view.findViewById(R.id.userImage);
        firstName = view.findViewById(R.id.editText_firstName);
        lastName = view.findViewById(R.id.editText_lastName);
        gender = view.findViewById(R.id.editText_gender);
        location = view.findViewById(R.id.editText_address);
        actionBarAddUserBtn = actionBar.findViewById(R.id.action_bar_add_user);
        addUser = view.findViewById(R.id.addUser);

        userImage.setOnClickListener(new View.OnClickListener() {
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
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHandler dbHandler = new DbHandler(getContext());
                if (firstName != null || lastName != null || gender != null || location != null) {
                    //user.setUserName(firstName.getText().toString()+" "+lastName.getText().toString());
                    //user.setGender(gender.getText().toString());
                    //user.setLocation(location.getText().toString());
                    //user.setUserId(String.valueOf(userId));
                    String imageURL = "";
                    String addUserName = firstName.getText().toString() + " " + lastName.getText().toString();
                    String addGender = gender.getText().toString();
                    String addLocation = location.getText().toString();
                    if (userImageBtmp != null) {
                        imageURL = BitmapReader.saveImage(userImageBtmp, addUserName);
                    }
                    long userId = dbHandler.addUser(imageURL, addUserName, addGender, addLocation);

                    //actionBarAddUserBtn.setImageDrawable(getResources().getDrawable(R.drawable.baseline_person_24));
                    mainActivity.backFragments();
                    mainActivity.backFragments();
                    fragmentManager.beginTransaction()
                            .add(R.id.fragment_container, UsersFragment.class, null, UsersFragment.TAG)
                            .setReorderingAllowed(true)
                            .addToBackStack("UsersFragment")
                            .commit();

                } else {
                    Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_LONG);
                }

            }
        });
        clear = view.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName.setText("");
                lastName.setText("");
                gender.setText("");
                location.setText("");
            }
        });
    }

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
    public void chooseImage(){
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
                        userImageBtmp = rotateBitmap(BitmapFactory.decodeFile(photoFile.getAbsolutePath()),rotation);
                        userImage.setImageBitmap(userImageBtmp);
                        Toast.makeText(getContext(), "Photo captured successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Log.e("CameraXApp", "Photo capture failed: " + exception.getMessage(), exception);
                        Toast.makeText(getContext(), "Failed to capture photo", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                userImageBtmp = BitmapFactory.decodeFile(picturePath);
                                userImage.setImageBitmap(userImageBtmp);
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }
}