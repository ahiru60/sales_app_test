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
 * Use the {@link ReceiptFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReceiptFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "ReceiptFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView receiptRecyclerView;
    private ReceiptsAdapter receiptsAdapter;
    private DbHandler dbHandler;
    private LinearLayoutManager linearLayoutManager;
    private EditText searchReceipts;
    public String searchTime = "";

    public ReceiptFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReceiptFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReceiptFragment newInstance(String param1, String param2) {
        ReceiptFragment fragment = new ReceiptFragment();
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
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);
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