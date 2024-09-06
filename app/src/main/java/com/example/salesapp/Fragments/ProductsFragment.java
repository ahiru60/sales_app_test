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

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.salesapp.Database.DbHandler;
import com.example.salesapp.MainActivity;
import com.example.salesapp.R;

import java.util.ArrayList;
import java.util.List;

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
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
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
                    dbHandler.addItemtoItems(image,productStr,stockStr,costStr,sellingPriceStr);
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
                if(checkAndRequestPermissions(getActivity())){
                    chooseImage(getActivity());
                }
            }
        });
    }

    private void initViews() {
        mainActivity = (MainActivity) getActivity();
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
        if(productStr == "" && stockStr == "" && costStr == "" && sellingPriceStr == ""){
            addProductBtn.setEnabled(false);

        }
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
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
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
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        productImageBtmp = (Bitmap) data.getExtras().get("data");
                        productImageBtmp = rotateBitmap(productImageBtmp,90);
                        productImage.setImageBitmap(productImageBtmp);
                    }
                    break;
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
}