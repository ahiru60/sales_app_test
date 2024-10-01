package com.example.salesapp.Dialogs;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.salesapp.Adapters.OrderListAdapter;
import com.example.salesapp.Database.DbHandler;
import com.example.salesapp.Fragments.HomeFragment;
import com.example.salesapp.Models.Order;
import com.example.salesapp.R;

import java.util.ArrayList;

public class OrderDialogFragment extends DialogFragment {
    public static final String TAG = "OrderDialogFragment";
    private View view;
    private LinearLayoutManager linearLayoutManagerOrder;
    private RecyclerView recyclerViewOrder;
    private ImageButton orderCloseBtn,orderSearchBtn;
    private EditText orderSearchEditText;
    private boolean isOrderSearching = true;
    private ArrayList<Order> orderItems = new ArrayList<Order>();
    private DbHandler dbHandler;
    private OrderListAdapter orderListAdapter;
    private HomeFragment homeFragment;
    private FragmentManager fragmentManager;
    public OrderDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.orders_dialogbox, container, false);
        dbHandler = new DbHandler(getActivity());
        fragmentManager = getActivity().getSupportFragmentManager();
        homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(HomeFragment.TAG);
        recyclerViewOrder = view.findViewById(R.id.recyclerViewOrder);
        orderCloseBtn = view.findViewById(R.id.closeBtn);
        orderSearchBtn = view.findViewById(R.id.searchBtn);
        orderSearchEditText = view.findViewById(R.id.orderSearchBar);
        linearLayoutManagerOrder = new LinearLayoutManager(getContext());
        recyclerViewOrder.setLayoutManager(linearLayoutManagerOrder);
        orderItems = dbHandler.getOrders();
        orderListAdapter = new OrderListAdapter(homeFragment,homeFragment,orderItems);
        recyclerViewOrder.setAdapter(orderListAdapter);

        orderCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        orderSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOrderSearching){
                    orderSearchEditText.setVisibility(View.GONE);
                    isOrderSearching = false;
                }else{
                    orderSearchEditText.setVisibility(View.VISIBLE);
                }
            }
        });

        orderSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                orderItems = dbHandler.searchOrders(s.toString());
                orderListAdapter = new OrderListAdapter(homeFragment,homeFragment,orderItems);
                recyclerViewOrder.setAdapter(orderListAdapter);
            }
        });
        return view;
    }
}