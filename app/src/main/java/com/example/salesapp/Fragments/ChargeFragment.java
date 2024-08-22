package com.example.salesapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.salesapp.MainActivity;
import com.example.salesapp.Models.SessionItem;
import com.example.salesapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChargeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChargeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "ChargeFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView total;
    private ArrayList<SessionItem> items = new ArrayList<>();
    private HomeFragment homeFragment;
    private View view;
    private ImageButton chargeBackArrowBtn;
    private View actionBar;
    private LinearLayout viewCartBtn;
    private MainActivity mainActivity;
    private FragmentManager fragmentManager;
    private Button newSalesBtn;

    public ChargeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChargeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChargeFragment newInstance(String param1, String param2) {
        ChargeFragment fragment = new ChargeFragment();
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
        view = inflater.inflate(R.layout.fragment_charge, container, false);
        setupView();
        calcTotal();
        setupNewSales();
        clearitems();
        return view;
    }
    private void setupView(){
        mainActivity = (MainActivity) getActivity();
        actionBar = mainActivity.getSupportActionBar().getCustomView();
        viewCartBtn = actionBar.findViewById(R.id.viewCartBtn);
        viewCartBtn.setVisibility(View.GONE);
        newSalesBtn = view.findViewById(R.id.newSale);
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
    private void calcTotal(){
        total = view.findViewById(R.id.total);
        homeFragment = (HomeFragment) getParentFragmentManager().findFragmentByTag(HomeFragment.TAG);
        items = homeFragment.getUser().getUserCartItems();
        String textTotal;
        Double price = 0.00;
        Double totalPrice = 0.00;
        for(SessionItem item : items){
            totalPrice = totalPrice + (Double) Double.parseDouble(item.getPrice())*Integer.parseInt(item.getQuantity());
        }
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        total.setText(df.format(totalPrice)+"$");
    }
    private void setupNewSales(){
        newSalesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.clear();
                viewCartBtn.setVisibility(View.VISIBLE);
                //homeFragment.setIsCarting(false);
                mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                fragmentManager = getParentFragmentManager();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction()
                        .remove(new HomeFragment())
                        .commit();
            }
        });
    }
    private void clearitems(){
        items.clear();
    }
}