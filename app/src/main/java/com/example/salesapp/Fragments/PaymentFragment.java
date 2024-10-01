package com.example.salesapp.Fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salesapp.MainActivity;
import com.example.salesapp.Models.SessionItem;
import com.example.salesapp.Models.User;
import com.example.salesapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
public class PaymentFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "PaymentFragment";
    private String mParam1;
    private String mParam2;
    private View view;
    private Button cashBtn,cardBtn,reditBtn,chargeBtn;
    private TextView total,editTextTotal;
    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private MainActivity mainActivity;
    private User user;

    public PaymentFragment() {
        // Required empty public constructor
    }
    public static PaymentFragment newInstance(String param1, String param2) {
        PaymentFragment fragment = new PaymentFragment();
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
//                mainActivity.setArrow(true);
//                fragmentManager.beginTransaction()
//                        .remove(new PaymentFragment())
//                        .commit();
//                fragmentManager.popBackStack();
                mainActivity.backFragments();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payment, container, false);
        initViews();
        setupBtnClicks();
        calcTotal();
        return view;
    }

    private void initViews(){
        mainActivity = (MainActivity) getActivity();
        fragmentManager = getParentFragmentManager();
        homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(HomeFragment.TAG);
        user = homeFragment.getUser();
        total = view.findViewById(R.id.total);
        editTextTotal = view.findViewById(R.id.editText_Total);
        cashBtn = view.findViewById(R.id.paymentMethodCashBtn);
        cardBtn = view.findViewById(R.id.paymentMethodCardBtn);
        reditBtn = view.findViewById(R.id.paymentMethodReditBtn);
        chargeBtn = view.findViewById(R.id.chargeBtn);
    }

    private void setupBtnClicks(){
        user.setPaymentMethod("Cash");
        cashBtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button_bg_selected));
        cashBtn.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
        cardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setPaymentMethod("Card");
                cardBtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button_bg_selected));
                cashBtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.white_round_squre));
                reditBtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.white_round_squre));
                cardBtn.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
                cashBtn.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                reditBtn.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
            }
        });
        cashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setPaymentMethod("Cash");
                cardBtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.white_round_squre));
                cashBtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button_bg_selected));
                reditBtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.white_round_squre));
                cardBtn.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                cashBtn.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
                reditBtn.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
            }
        });
        reditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setPaymentMethod("Redit");
                cardBtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.white_round_squre));
                cashBtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.white_round_squre));
                reditBtn.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button_bg_selected));
                cardBtn.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                cashBtn.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                reditBtn.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
            }
        });
        chargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getPaymentMethod() != null){
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, ChargeFragment.class,null,ChargeFragment.TAG)
                        .setReorderingAllowed(true)
                        .addToBackStack("ChargeFragment")
                        .commit();
                }else {
                    Toast.makeText(getContext(),"Select a payment method",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void calcTotal(){
        Double totalDouble = homeFragment.getTotal(user.getUserItems());
        DecimalFormat df = new DecimalFormat("0.00");
        total.setText(df.format(totalDouble));
        editTextTotal.setText(df.format(totalDouble));
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