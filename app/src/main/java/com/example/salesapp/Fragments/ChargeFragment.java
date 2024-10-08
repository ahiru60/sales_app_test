package com.example.salesapp.Fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.salesapp.Database.DbHandler;
import com.example.salesapp.MainActivity;
import com.example.salesapp.Models.Receipt;
import com.example.salesapp.Models.SessionItem;
import com.example.salesapp.Models.User;
import com.example.salesapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ChargeFragment extends Fragment {
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
    private ImageButton backArrowBtn,addUserBtn;
    private View actionBar;
    private LinearLayout viewCartBtn;
    private MainActivity mainActivity;
    private FragmentManager fragmentManager;
    private Button newSalesBtn;
    private User user;
    private Double totalPrice;

    public ChargeFragment() {
        // Required empty public constructor
    }
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
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                mainActivity.onNavItemHome();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_charge, container, false);
        setupView();
        calcTotal();
        setupNewSales();
        setupLogic();
        return view;
    }
    private void setupView(){
        mainActivity = (MainActivity) getActivity();
        actionBar = mainActivity.getSupportActionBar().getCustomView();
        fragmentManager = mainActivity.getSupportFragmentManager();
        backArrowBtn = actionBar.findViewById(R.id.backArrow);
        addUserBtn = actionBar.findViewById(R.id.action_bar_add_user);
        backArrowBtn.setVisibility(View.GONE);
        viewCartBtn = actionBar.findViewById(R.id.viewCartBtn);
        viewCartBtn.setVisibility(View.GONE);
        newSalesBtn = view.findViewById(R.id.newSale);
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
    private void setupLogic(){
        user =homeFragment.getUser();
        String orderId = user.getOrderId();
        DbHandler dbHandler = new DbHandler(getContext());
        dbHandler.saveReceipt(new Receipt(null,String.valueOf(totalPrice),user.getUserId(),null));
        if(orderId != null){
            dbHandler.deleteOrder(orderId);
        }
    }
    private void calcTotal(){
        total = view.findViewById(R.id.total);
        homeFragment = (HomeFragment) getParentFragmentManager().findFragmentByTag(HomeFragment.TAG);
        items = homeFragment.getUser().getUserItems();
        String textTotal;
        Double price = 0.00;
        totalPrice = homeFragment.getTotal(items);
//        for(SessionItem item : items){
//            totalPrice = totalPrice + (Double) Double.parseDouble(item.getPrice())*Integer.parseInt(item.getQuantity());
//        }
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        total.setText(df.format(totalPrice)+"$");
    }
    private void setupNewSales(){
        newSalesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.clear();
                user = new User("","","","","",null,null);
                mainActivity.homeSetActions(true);
                mainActivity.refreshFragments(HomeFragment.TAG);
                mainActivity.onNavItemHome();
            }
        });
    }

    private Double getTotal(ArrayList<SessionItem> items){
        Double total = 0.00;
        for (SessionItem item : items){
            Double price = Double.parseDouble(item.getPrice());
            Double quantity = Double.parseDouble(item.getQuantity());
            total = total + price*quantity;
        }
        return total;
    }
}