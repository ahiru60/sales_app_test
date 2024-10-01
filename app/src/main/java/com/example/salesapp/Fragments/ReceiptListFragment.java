package com.example.salesapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.salesapp.Adapters.ReceiptsAdapter;
import com.example.salesapp.Database.DbHandler;
import com.example.salesapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReceiptListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReceiptListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "ReceiptFragment";
    private String mParam1;
    private String mParam2;
    private RecyclerView receiptRecyclerView;
    private ReceiptsAdapter receiptsAdapter;
    private DbHandler dbHandler;
    private LinearLayoutManager linearLayoutManager;
    private EditText searchReceipts;
    public String searchTime = "";

    public ReceiptListFragment() {
        // Required empty public constructor
    }
    public static ReceiptListFragment newInstance(String param1, String param2) {
        ReceiptListFragment fragment = new ReceiptListFragment();
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
        View view = inflater.inflate(R.layout.fragment_receipt_list, container, false);
        dbHandler = new DbHandler(getContext());
        linearLayoutManager = new LinearLayoutManager(getContext());
        receiptRecyclerView = view.findViewById(R.id.receiptRecyclerView);
        receiptsAdapter = new ReceiptsAdapter(dbHandler.getReceipts(),true);
        receiptRecyclerView.setLayoutManager(linearLayoutManager);
        receiptRecyclerView.setAdapter(receiptsAdapter);
        searchReceipts = view.findViewById(R.id.search_receipt);
        searchReceipts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    receiptsAdapter = new ReceiptsAdapter(dbHandler.searchReceiptByName(s.toString(),searchTime),false);
                    receiptRecyclerView.setLayoutManager(linearLayoutManager);
                    receiptRecyclerView.setAdapter(receiptsAdapter);
                    searchReceipts = view.findViewById(R.id.search_receipt);
                }else {
                    receiptsAdapter = new ReceiptsAdapter(dbHandler.getReceipts(),true);
                    receiptRecyclerView.setLayoutManager(linearLayoutManager);
                    receiptRecyclerView.setAdapter(receiptsAdapter);
                    searchReceipts = view.findViewById(R.id.search_receipt);
                }
            }
        });
        return view;
    }
}