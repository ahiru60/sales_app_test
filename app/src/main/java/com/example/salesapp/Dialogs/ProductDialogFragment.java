package com.example.salesapp.Dialogs;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.salesapp.Adapters.DbItemsListAdapter;
import com.example.salesapp.Fragments.EditProductFragment;
import com.example.salesapp.Fragments.HomeFragment;
import com.example.salesapp.Fragments.ProductsFragment;
import com.example.salesapp.Models.DBItem;
import com.example.salesapp.Models.User;
import com.example.salesapp.R;

public class ProductDialogFragment extends DialogFragment {
    public static final String TAG = "ProductDialogFragment";
    private View view;
    private TextView cartItemsNumber,totalTxt,discountTxt,DbxProductName,DbxStock,DbxProductCode,DbxProductPrice,DbxProductCost;
    private Button BtnEdit,BtnClose;
    private ImageView DbxProductImage;
    private DBItem item;
    private FragmentManager fragmentManager;
    private ProductDbxListener productDbxListener;
    public ProductDialogFragment(DBItem item, ProductDbxListener productDbxListener) {
        this.item = item;
        this.productDbxListener = productDbxListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.product_dialogbox, container, false);
        fragmentManager = getParentFragmentManager();
        DbxProductImage = view.findViewById(R.id.userImage);
        DbxProductName = view.findViewById(R.id.Txt_productName);
        DbxStock = view.findViewById(R.id.Txt_stock);
        DbxProductCode =view.findViewById(R.id.Txt_productCode);
        DbxProductPrice = view.findViewById(R.id.Txt_productPrice);
        DbxProductCost = view.findViewById(R.id.Txt_productCost);
        BtnEdit = view.findViewById(R.id.Btn_edit);
        BtnClose = view.findViewById(R.id.Btn_close);

        DbxProductImage.setImageBitmap(item.getImageBtmp());
        DbxProductName.setText(item.getItemName());
        DbxStock.setText(item.getStock()+"in stock");
        DbxProductCode.setText(item.getItemId());
        DbxProductPrice.setText(item.getSellingPrice());
        DbxProductCost.setText(item.getCost());

        BtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDbxListener.editBtnClickListener();
                dismiss();
            }
        });
        BtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
    public interface ProductDbxListener{
        void editBtnClickListener();
    }
}