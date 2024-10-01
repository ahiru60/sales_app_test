package com.example.salesapp.Dialogs;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.example.salesapp.Models.Discount;
import com.example.salesapp.R;

import java.util.ArrayList;

public class DiscountDialogFragment extends DialogFragment {
    private View view;
    private Button oneBtn, twoBtn, threeBtn, fourBtn, fiveBtn, sixBtn,sevenBtn, eightBtn, nineBtn, zeroBtn, clearBtn,addBtn;
    private ImageButton closeBtn;
    private RadioButton valueRBtn,percentageRBtn;
    private EditText display;
    private Discount discount;
    private DiscountDbxListener discountDbxListener;
    private ArrayList<Character> digits = new ArrayList<>();
    private boolean isValue = true;
    private int cursor;
    public static final String TAG = "DiscountDialogFragment";
    public DiscountDialogFragment(Discount discount,DiscountDbxListener discountDbxListener) {
        this.discountDbxListener =discountDbxListener;
        this.discount = discount;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.discount_dialogbox, container, false);
        closeBtn = view.findViewById(R.id.closeBtn);
        addBtn = view.findViewById(R.id.addBtn);
        display = view.findViewById(R.id.display);
        oneBtn = view.findViewById(R.id.one);
        twoBtn = view.findViewById(R.id.two);
        threeBtn = view.findViewById(R.id.three);
        fourBtn = view.findViewById(R.id.four);
        fiveBtn = view.findViewById(R.id.five);
        sixBtn = view.findViewById(R.id.six);
        sevenBtn = view.findViewById(R.id.seven);
        eightBtn = view.findViewById(R.id.eight);
        nineBtn = view.findViewById(R.id.nine);
        zeroBtn = view.findViewById(R.id.zero);
        clearBtn = view.findViewById(R.id.clear);
        valueRBtn = view.findViewById(R.id.value);
        percentageRBtn =view.findViewById(R.id.percentage);
        valueRBtn.toggle();
        display.setText(printToDisplay());
        if(discount != null){
            this.isValue = discount.isValue();
            if(!isValue){
                valueRBtn.toggle();
                percentageRBtn.toggle();
            }
            String text = discount.getDiscount().toString();
            display.setText(text);
            Log.d(TAG, "onCreateView: "+text);
        }
        
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discount = new Discount();
                if(isValue){
                    String text = display.getText().toString();
                    if(!text.isEmpty()){
                        discount.setValue(Double.parseDouble(text));
                    }else{
                        discount.setValue(0.00);
                    }
                }else {
                    String [] split = display.getText().toString().split("%");
                    discount.setPercentage(Double.parseDouble(split[0]));
                }

                digits.clear();
                cursor =0;
                discountDbxListener.addBtnClick(discount);
                dismiss();


            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        valueRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isValue = true;
                discountDbxListener.rbValueBtnClick(isValue);
                digits.clear();
                cursor = 0;
                display.setText(printToDisplay());
                display.setHint("0.00");
            }
        });
        percentageRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isValue = false;
                discountDbxListener.rbValueBtnClick(isValue);
                digits.clear();
                cursor = 0;
                display.setText("");
                display.setHint("0.00%");

            }
        });


        oneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type('1');
            }
        });
        twoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type('2');
            }
        });
        threeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type('3');
            }
        });
        fourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type('4');
            }
        });
        fiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type('5');
            }
        });
        sixBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type('6');
            }
        });
        sevenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type('7');
            }
        });
        eightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type('8');
            }
        });
        nineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type('9');
            }
        });
        zeroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type('0');
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor = 0;
                digits.clear();
                if(isValue){
                    display.setText("0.00");
                }else {
                    display.setText("0.00%");
                }
            }
        });
        return view;
    }
    private String printToDisplay() {
        String text = "";
        int index = 0;
        for(Character digit : digits){
            if(index == digits.size()-3){
                text += digit;
                text += '.';
            }else{
                text += digit;
            }
            index++;
        }
        if(!isValue){
            text+= '%';
        }
        if(digits.size()== 1){
            return "0.0"+text;
        }
        if(digits.size()== 2){
            return "0."+text;
        }
        return text;
    }
    private void type(Character value) {
        if(isValue){
            if (cursor < 12){
                digits.add(value);
                display.setText(printToDisplay());
                cursor++;
            }
        }else{
            if (cursor < 5){
                digits.add(value);
                display.setText(printToDisplay());
                String text = "";
                for(Character digit : digits){
                    text+= digit;
                }
                int perc = Integer.parseInt(text);
                if(perc > 10000){
                    digits.clear();
                    digits.add('1');
                    digits.add('0');
                    digits.add('0');
                    digits.add('0');
                    digits.add('0');
                    display.setText(printToDisplay());
                    cursor = 5;
                }
                cursor++;
            }
        }
    }
    public interface DiscountDbxListener{
        void rbValueBtnClick(boolean isValue);
        void addBtnClick(Discount discount);
    }
}