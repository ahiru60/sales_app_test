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

import androidx.activity.OnBackPressedCallback;
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
import com.example.salesapp.Models.DBItem;
import com.example.salesapp.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "EditProductFragment";
    private DBItem item;
    private View view;
    private EditText productName,stock, cost, sellingPrice;
    private Button editProductBtn, clearBtn;
    private ImageView productImage;
    private DbHandler dbHandler;
    private Bitmap productImageBtmp;
    private MainActivity mainActivity;
    private HomeFragment homeFragment;
    private FragmentManager fragmentManager;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private PreviewView previewView;
    private ImageCapture imageCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private FrameLayout cameraLayout;
    private ImageButton captureBtn,cameraFlipBtn;
    private int rotation = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProductFragment newInstance(String param1, String param2) {
        EditProductFragment fragment = new EditProductFragment();
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
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                //mainActivity.setArrow(true);
                fragmentManager.beginTransaction()
                        .remove(new EditProductFragment());
                fragmentManager.popBackStack();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_product, container, false);
        initDb();
        initViews();
        setupBtn();
        return view;
    }
    private void initViews() {
        mainActivity = (MainActivity) getActivity();
        mainActivity.setArrow(false);
        previewView = view.findViewById(R.id.previewView);
        cameraLayout = view.findViewById(R.id.capture_camera);
        captureBtn = view.findViewById(R.id.captureBtn);
        cameraFlipBtn = view.findViewById(R.id.cameraChange);
        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        fragmentManager = getParentFragmentManager();
        homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(HomeFragment.TAG);
        item = homeFragment.getItem();
        productImage = view.findViewById(R.id.editProductImage);
        productName = view.findViewById(R.id.editText_editProductName);
        stock = view.findViewById(R.id.editText_editStock);
        cost = view.findViewById(R.id.editText_editCost);
        sellingPrice = view.findViewById(R.id.editText_editSellingPrice);
        editProductBtn = view.findViewById(R.id.Btn_editProduct);
        clearBtn = view.findViewById(R.id.Btn_editClear);
        productName.setText(item.getItemName());
        stock.setText(item.getStock());
        cost.setText(item.getCost());
        sellingPrice.setText(item.getSellingPrice());
        if(item.getImageBtmp() == null){
            productImage.setImageResource(R.drawable.baseline_image_24);
        }else{
            productImage.setImageBitmap(item.getImageBtmp());
        }
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
        editProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap image;
                if(productImageBtmp != null){
                    image = productImageBtmp;
                }else{
                    image = item.getImageBtmp();
                }

                String productStr = productName.getText().toString();
                String stockStr = stock.getText().toString();
                String costStr = cost.getText().toString();
                String sellingPriceStr = sellingPrice.getText().toString();
                if(productStr.equals("") || stockStr.equals("") || costStr.equals("") || sellingPriceStr.equals("")){
                    Toast.makeText(getContext(),"Fill all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean added = dbHandler.updateItem(item.getItemId(),image,productStr,stockStr,costStr,sellingPriceStr);
                    if(added){
                        Toast.makeText(getContext(),productStr +" edited succenssfully", Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        fragmentManager.beginTransaction()
                                .remove(new EditProductFragment())
                                .commit();
                        fragmentManager.popBackStack();

                        fragmentManager.beginTransaction()
                                .add(R.id.fragment_container, HomeFragment.class,null,HomeFragment.TAG)
                                .setReorderingAllowed(true)
                                .addToBackStack("HomeFragment")
                                .commit();
                    }else{
                        Toast.makeText(getContext(),"Failed to update product", Toast.LENGTH_SHORT).show();
                    }

                }




            }
        });
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAndRequestPermissions(getActivity())){
                    chooseImage(getActivity());
                }
            }
        });
    }
    private void chooseImage(Context context){
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
                    }, ContextCompat.getMainExecutor(getContext()));
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }
    // function to check permission
    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    // Handled permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity().getApplicationContext(),
                                    "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(getActivity());
                }
                break;
        }
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
                                productImageBtmp = BitmapFactory.decodeFile(picturePath);
                                productImage.setImageBitmap(productImageBtmp);
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }
    private Bitmap rotateBitmap(Bitmap sourceBitmap,float angle) {
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
        File photoFile = new File(requireContext().getExternalFilesDir(null), "photo.jpg");

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