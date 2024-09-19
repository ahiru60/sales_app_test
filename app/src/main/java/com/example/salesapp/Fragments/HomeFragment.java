package com.example.salesapp.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesapp.Adapters.DbItemsListAdapter;
import com.example.salesapp.Adapters.OrderListAdapter;
import com.example.salesapp.Adapters.SessionCartListAdapter;
import com.example.salesapp.Database.DbHandler;
import com.example.salesapp.MainActivity;
import com.example.salesapp.Models.DBItem;
import com.example.salesapp.Models.Discount;
import com.example.salesapp.Models.Order;
import com.example.salesapp.Models.SessionItem;
import com.example.salesapp.Models.User;
import com.example.salesapp.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements DbItemsListAdapter.OnClickListener, OrderListAdapter.OnClickListner{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "HomeFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private ArrayList<DBItem> items = new ArrayList<>();
    private ArrayList<Order> orderItems = new ArrayList<>();
    private ArrayList<DBItem> searchItems = new ArrayList<>();
    private ArrayList<SessionItem> sessionItems = new ArrayList<>();
    private DbItemsListAdapter itemListAdapter, searchListAdapter;
    private SessionCartListAdapter cartListAdapter;
    private RecyclerView recyclerViewItems, recyclerViewSearch, recyclerViewCart,recyclerViewOrder;
    private LinearLayoutManager linearLayoutManagerItems, linearLayoutManagerCart, linearLayoutManagerOrder;
    private AlertDialog.Builder showDiscountDialogBuilder,productDialogBuilder,orderDialogBuilder;
    private AlertDialog showDiscountDialog,showProductDialog,showOrderDialog;
    private DbHandler dbHandler;
    private User user;
    private Order order;
    private Discount discount;
    private Button chargeBtn,saveBtn,addBtn,BtnEdit,BtnClose;
    private Button oneBtn, twoBtn, threeBtn, fourBtn, fiveBtn, sixBtn,sevenBtn, eightBtn, nineBtn, zeroBtn, clearBtn;
    private EditText display;
    private FragmentManager fragmentManager;
    private ImageButton searchBtn, addUserBtn, cartBackArrow, discountBtn,closeBtn,orderCloseBtn,orderSearchBtn;
    private ImageView DbxProductImage;
    private View actionBar, discountCustomLayout,productCustomLayout,orderCustomLayout;
    private FrameLayout searchBar;
    private EditText searchEditText, totalEditTxt, orderSearchEditText;
    private TextView cartItemsNumber,totalTxt,discountTxt,DbxProductName,DbxStock,DbxProductCode,DbxProductPrice,DbxProductCost;
    private MainActivity mainActivity;
    private LinearLayout viewCartBtn, listsView, salesViews, paymentView;
    private ConstraintLayout cartViewsHolder;
    private RadioButton valueRBtn,percentageRBtn;
    private int itemCount;
    private boolean isSearching = false, isCarting = false, isOrderSearching = false;
    private ArrayList<Character> digits = new ArrayList<>();
    private EditProductFragment editProductFragment;
    private int cursor;
    private boolean isValue = true;
    private NumberFormat formatter = new DecimalFormat("#0.00");
    private DBItem item;
    private OrderListAdapter orderListAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

                if (!isCarting) {
                    mainActivity.backFragments();
                } else {
                    hideCartView(true);
                    isCarting = false;
                }

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mainActivity.homeSetActions(true);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        dbHandler = new DbHandler(getActivity());
        //dbHandler.close();
       //getContext().deleteDatabase("sales.db");
//        dbHandler.addItemtoItems(null,"Apple", "1500", "2.90","3");
//        dbHandler.addItemtoItems(null,"Orange", "1000", "2","2.50");
//        dbHandler.addItemtoItems(null,"Mango", "1000", "3","3.10");
//        dbHandler.addItemtoItems(null,"Coconut", "300", "5","5.50");
//        dbHandler.addItemtoItems(null,"Chocolate", "100", "3","3.01");
//        dbHandler.addItemtoItems(null,"Candles", "500", "2","2.40");
//        dbHandler.addItemtoItems(null,"King coconut", "1500", "4","4.50");
//        dbHandler.addItemtoItems(null,"Beef", "1000", "5","5.20");
//        dbHandler.addItemtoItems(null,"Avocado", "500", "3","3.10");
//        dbHandler.addItemtoItems(null,"Fruit juice", "400", "2","3");
//        dbHandler.addItemtoItems(null,"Pineapple", "300", "3","3.20");
//        dbHandler.addItemtoItems(null,"Soap", "500", "1","1.50");
        setupLogics();
        setupDatabase();
        setupItemsRecyclerView();
        setupViews();
        setupCartRecyclerView();
        setupDialog();
        return view;
    }

    private void setupLogics() {
        user = new User(null,null,null,null, null, sessionItems,null);
        itemCount = user.getUserItems().size();
        cursor = 0;
    }

    private void setupViews() {
        mainActivity = (MainActivity) getActivity();
        mainActivity.homeSetActions(true);
        fragmentManager = getParentFragmentManager();
        actionBar = mainActivity.getSupportActionBar().getCustomView();
        discountBtn = actionBar.findViewById(R.id.discountBtn);
        cartBackArrow = actionBar.findViewById(R.id.cartBackArrow);
        cartItemsNumber = actionBar.findViewById(R.id.cartItemNumber);
        cartItemsNumber.setText(String.valueOf(itemCount));
        paymentView = view.findViewById(R.id.paymentView);
        totalEditTxt = view.findViewById(R.id.editText_Total);
        totalTxt = view.findViewById(R.id.textView_Total);
        discountTxt = view.findViewById(R.id.textView_Discount);
        listsView = view.findViewById(R.id.listsView);
        searchBar = view.findViewById(R.id.searchbar);
        addUserBtn = actionBar.findViewById(R.id.action_bar_add_user);
        addUserBtn.setImageDrawable(getResources().getDrawable(R.drawable.baseline_person_add_alt_24));
        viewCartBtn = actionBar.findViewById(R.id.viewCartBtn);
        salesViews = view.findViewById(R.id.salesViews);
        chargeBtn = view.findViewById(R.id.chargeBtn);
        saveBtn = view.findViewById(R.id.saveBtn);
        setupSearch();
        setupButtonClicks();

    }

    private void setupItemsRecyclerView() {
        itemListAdapter = new DbItemsListAdapter(items, this);
        linearLayoutManagerItems = new LinearLayoutManager(getActivity());
        recyclerViewItems = view.findViewById(R.id.recyclerViewItems);
        recyclerViewItems.setLayoutManager(linearLayoutManagerItems);
        recyclerViewItems.setAdapter(itemListAdapter);
    }

    private void setupSearch() {
        searchBtn = view.findViewById(R.id.searchBtn);
        searchEditText = view.findViewById(R.id.searchEditText);
        recyclerViewSearch = view.findViewById(R.id.recyclerViewSearch);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != "") {
                    searchItems.clear();
                    searchItems = dbHandler.searchItemByName(s.toString());
                    linearLayoutManagerCart = new LinearLayoutManager(getActivity());
                    recyclerViewSearch.setLayoutManager(linearLayoutManagerCart);
                    searchListAdapter = new DbItemsListAdapter(searchItems, HomeFragment.this);
                    recyclerViewSearch.setAdapter(searchListAdapter);
                    searchListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setupCartRecyclerView() {
        cartViewsHolder = view.findViewById(R.id.CartViewsHolder);
        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
    }

    private void setupDialog() {

        showDiscountDialogBuilder = new AlertDialog.Builder(getContext());
        productDialogBuilder = new AlertDialog.Builder(getContext());
        orderDialogBuilder = new AlertDialog.Builder(getContext(),android.R.style.Widget_Material);

        discountCustomLayout = getLayoutInflater().inflate(R.layout.discount_dialogbox, null);
        productCustomLayout = getLayoutInflater().inflate(R.layout.product_dialogbox,null);
        orderCustomLayout = getLayoutInflater().inflate(R.layout.orders_dialogbox,null);

        showDiscountDialogBuilder.setView(discountCustomLayout);
        showDiscountDialog = showDiscountDialogBuilder.create();

        productDialogBuilder.setView(productCustomLayout);
        showProductDialog = productDialogBuilder.create();

        orderDialogBuilder.setView(orderCustomLayout);
        showOrderDialog = orderDialogBuilder.create();
        recyclerViewOrder = orderCustomLayout.findViewById(R.id.recyclerViewOrder);
        orderCloseBtn = orderCustomLayout.findViewById(R.id.closeBtn);
        orderSearchBtn = orderCustomLayout.findViewById(R.id.searchBtn);
        orderSearchEditText = orderCustomLayout.findViewById(R.id.orderSearchBar);
        linearLayoutManagerOrder = new LinearLayoutManager(getContext());
        recyclerViewOrder.setLayoutManager(linearLayoutManagerOrder);
        orderCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderDialog.cancel();
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
                orderListAdapter = new OrderListAdapter(HomeFragment.this,HomeFragment.this,orderItems);
                recyclerViewOrder.setAdapter(orderListAdapter);
                showOrderDialog.show();
            }
        });

        DbxProductImage = productCustomLayout.findViewById(R.id.userImage);
        DbxProductName = productCustomLayout.findViewById(R.id.Txt_productName);
        DbxStock = productCustomLayout.findViewById(R.id.Txt_stock);
        DbxProductCode =productCustomLayout.findViewById(R.id.Txt_productCode);
        DbxProductPrice = productCustomLayout.findViewById(R.id.Txt_productPrice);
        DbxProductCost = productCustomLayout.findViewById(R.id.Txt_productCost);
        BtnEdit = productCustomLayout.findViewById(R.id.Btn_edit);
        BtnClose = productCustomLayout.findViewById(R.id.Btn_close);
        BtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container,EditProductFragment.class,null,EditProductFragment.TAG)
                        .setReorderingAllowed(true)
                        .addToBackStack("EditProductFragment")
                        .commit();
                showProductDialog.cancel();
            }
        });


        closeBtn = discountCustomLayout.findViewById(R.id.closeBtn);
        addBtn = discountCustomLayout.findViewById(R.id.addBtn);
        display = discountCustomLayout.findViewById(R.id.display);
        oneBtn = discountCustomLayout.findViewById(R.id.one);
        twoBtn = discountCustomLayout.findViewById(R.id.two);
        threeBtn = discountCustomLayout.findViewById(R.id.three);
        fourBtn = discountCustomLayout.findViewById(R.id.four);
        fiveBtn = discountCustomLayout.findViewById(R.id.five);
        sixBtn = discountCustomLayout.findViewById(R.id.six);
        sevenBtn = discountCustomLayout.findViewById(R.id.seven);
        eightBtn = discountCustomLayout.findViewById(R.id.eight);
        nineBtn = discountCustomLayout.findViewById(R.id.nine);
        zeroBtn = discountCustomLayout.findViewById(R.id.zero);
        clearBtn = discountCustomLayout.findViewById(R.id.clear);
        valueRBtn = discountCustomLayout.findViewById(R.id.value);
        percentageRBtn =discountCustomLayout.findViewById(R.id.percentage);
        valueRBtn.toggle();
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
                user.setDiscount(discount);
                digits.clear();
                cursor =0;
                showDiscountDialog.cancel();
                chargeBtn.setText("CHARGE\n" + formatter.format(getTotal(user.getUserItems())) + " $");
                discountTxt.setText(getDiscount(Double.valueOf(getTotal(user.getUserItems()).toString())).toString());

            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiscountDialog.cancel();
            }
        });
        valueRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isValue = true;
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
                digits.clear();
                cursor = 0;
                display.setText("");
                display.setHint("0.00%");

            }
        });

        display.setText(printToDisplay());
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


    }

    private void setupButtonClicks() {

        chargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCarting = false;
                hideCartView(true);
                cartBackArrow.setVisibility(View.GONE);
                viewCartBtn.setVisibility(View.GONE);
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, PaymentFragment.class, null, PaymentFragment.TAG)
                        .setReorderingAllowed(true)
                        .addToBackStack("PaymentFragment")
                        .commit();

                System.out.println("h");
                mainActivity.homeSetActions(false);

            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = user.getUserItems().size();
                String discount = "0";
                boolean isValue = false;
                if(user.getDiscount() != null){
                    discount = user.getDiscount().getDiscount().toString();
                    isValue = user.getDiscount().isValue();
                }
                if(size > 0){
                    if(user.getOrderId() == null){

                        dbHandler.saveOrder(user.getUserItems(),discount,isValue,user.getUserId());
                        user.getUserItems().clear();

                    }else{
                        dbHandler.updateOrder(user.getOrderId(),user.getUserId(),user.getUserItems(),discount,isValue);
                    }
                    cartBackArrow.setVisibility(View.GONE);
                    user = new User(null,null,null,null,null,null,null);
//                    fragmentManager.beginTransaction()
//                            .add(R.id.fragment_container, HomeFragment.class, null, HomeFragment.TAG)
//                            .setReorderingAllowed(true)
//                            .addToBackStack("HomeFragment")
//                            .commit();
                    mainActivity.refreshFragments(HomeFragment.TAG);
                    //mainActivity.setArrow(true);
                }else{
                    orderItems = dbHandler.getOrders();
                    orderListAdapter = new OrderListAdapter(HomeFragment.this,HomeFragment.this,orderItems);
                    recyclerViewOrder.setAdapter(orderListAdapter);
                    showOrderDialog.show();
                }
            }
        });
        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getUserName() != null && user.getGender() != null && user.getLocation() != null) {
                    isCarting = false;
                    hideCartView(true);
                    cartBackArrow.setVisibility(View.GONE);
                    mainActivity.homeSetActions(false);
                    Fragment fragment = fragmentManager.findFragmentByTag(UserFragment.TAG);
                    if(fragment == null){
                    fragmentManager.beginTransaction()
                            .add(R.id.fragment_container, UserFragment.class, null, UserFragment.TAG)
                            .setReorderingAllowed(true)
                            .addToBackStack("UserFragment")
                            .commit();
                    }
                    //Toast.makeText(getContext(), "name: " + user.getUserName() + " gender: " + user.getGender() + " location: " + user.getLocation(), Toast.LENGTH_LONG).show();
                } else {
                    //add to userfrag
//                    mainActivity.setArrow(false);
//                    fragmentManager.beginTransaction()
//                            .add(R.id.fragment_container, AddUserFragment.class, null, AddUserFragment.TAG)
//                            .setReorderingAllowed(true)
//                            .addToBackStack("AddUserFragment")
//                            .commit();
                    isCarting = false;
                    hideCartView(true);
                    cartBackArrow.setVisibility(View.GONE);
                    mainActivity.homeSetActions(false);
                    Fragment fragment = fragmentManager.findFragmentByTag(UsersFragment.TAG);
                    if(fragment == null){
                        fragmentManager.beginTransaction()
                                .add(R.id.fragment_container, UsersFragment.class, null, UsersFragment.TAG)
                                .setReorderingAllowed(true)
                                .addToBackStack("UsersFragment")
                                .commit();
                    }

                }
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearching) {
                    recyclerViewItems.setVisibility(View.VISIBLE);
                    recyclerViewSearch.setVisibility(View.GONE);
                    searchEditText.setText("");
                    searchEditText.setVisibility(View.GONE);
                    searchBtn.setImageResource(R.drawable.baseline_search_24);
                    isSearching = false;
                } else {
                    recyclerViewItems.setVisibility(View.GONE);
                    recyclerViewSearch.setVisibility(View.VISIBLE);
                    searchEditText.setVisibility(View.VISIBLE);
                    searchBtn.setImageResource(R.drawable.baseline_close_24);
                    isSearching = true;
                }
            }
        });
        viewCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCarting) {
                    mainActivity.presstime = 0;
                    cartBackArrow.setVisibility(View.VISIBLE);
                    hideCartView(false);
                    mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    isCarting = true;
                }

            }
        });
        cartBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCarting) {
                    cartBackArrow.setVisibility(View.GONE);
                    hideCartView(true);
                    mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    isCarting = false;
                }
            }
        });
        discountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiscountDialog.show();
            }
        });
    }

    private void hideCartView(boolean doHide) {
        if (doHide) {
            viewCartBtn.setVisibility(View.VISIBLE);
            searchBar.setVisibility(View.VISIBLE);
            recyclerViewItems.setVisibility(View.VISIBLE);
            recyclerViewSearch.setVisibility(View.VISIBLE);
            cartViewsHolder.setVisibility(View.GONE);
            cartBackArrow.setVisibility(View.GONE);
            mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            viewCartBtn.setVisibility(View.GONE);
            searchBar.setVisibility(View.GONE);
            recyclerViewItems.setVisibility(View.GONE);
            recyclerViewSearch.setVisibility(View.GONE);
            cartViewsHolder.setVisibility(View.VISIBLE);
            recyclerViewCart.setLayoutManager(new LinearLayoutManager(getActivity()));
            cartListAdapter = new SessionCartListAdapter(user.getUserItems());
            recyclerViewCart.setAdapter(cartListAdapter);
            cartBackArrow.setVisibility(View.VISIBLE);
            mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            if (user.getUserItems().size() > 0) {
                String totalString = formatter.format(getTotal(user.getUserItems()));
                totalTxt.setText(totalString);
                discountTxt.setText(formatter.format(getDiscount(getTotal(user.getUserItems()))));
            }

        }
    }

    private void setupDatabase() {
        dbHandler = new DbHandler(getActivity());
        if(dbHandler.getItems() !=null){
            items = dbHandler.getItems();
        }
    }

    public User getUser() {
        return user;
    }

    @Override
    public User onItemClick() {
        return user;
    }

    @Override
    public void afterItemClick() {
        //itemCount = user.getUserItems().size();
        itemCount++;
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        Double total = 0.00;
        total = getTotal(user.getUserItems());
        chargeBtn.setText("CHARGE\n" + df.format(total) + " $");
        cartItemsNumber.setText(String.valueOf(itemCount));
        if(itemCount>0){
            saveBtn.setText("SAVE");
        }else{
            saveBtn.setText("OPEN ORDERS");
        }
    }

    @Override
    public void productInfoClick(DBItem item) {
        this.item = item;
        DbxProductImage.setImageBitmap(item.getImageBtmp());
        DbxProductName.setText(item.getItemName());
        DbxStock.setText(item.getStock()+"in stock");
        DbxProductCode.setText(item.getItemId());
        DbxProductPrice.setText(item.getSellingPrice());
        DbxProductCost.setText(item.getCost());

        BtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductDialog.cancel();
            }
        });
        showProductDialog.show();
    }

    public Double getTotal(ArrayList<SessionItem> items) {
        Double total = 0.00;
        if(items != null){
            for (SessionItem item : items) {
                Double price = Double.parseDouble(item.getPrice());
                Double quantity = Double.parseDouble(item.getQuantity());
                total = total + price * quantity;

            }
        }
        if(discount != null){
            if(discount.isValue()){
                total = total - discount.getDiscount();
            }
            if(!discount.isValue()){
                total = total - (total/100) * discount.getDiscount();
            }
        }

        return total;
    }
    private Double getDiscount(Double total) {
        if(discount != null){
            if(isValue){
                return discount.getDiscount();
            }else {
                return (total/100) * discount.getDiscount();
            }
        }
        return 0.00;
    }
    public DBItem getItem(){
        return item;
    }

    public void setIsCarting(boolean is) {
        isCarting = is;
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

    public void removeUser(){
        user.setUserId(null);
        user.setLocation(null);
        user.setGender(null);
        user.setImage(null);
        user.setUserName(null);
        addUserBtn.setImageDrawable(getResources().getDrawable(R.drawable.baseline_person_add_alt_24));
        mainActivity.backFragments();
    }

    public boolean isCarting(){
        return isCarting;
    }

    @Override
    public void orderItemClick(String orderId) {
        user.getUserItems().clear();
        user.setUserItems(dbHandler.getOrder(user,orderId));
        discount = user.getDiscount();
        if(discount != null){
            isValue = discount.isValue();
        display.setText(discount.getDiscount().toString());
        if(!isValue){
            valueRBtn.toggle();
            percentageRBtn.toggle();
        }
        }
        user.setOrderId(orderId);
        itemCount = -1;
        for(SessionItem item : user.getUserItems()){
            itemCount+= Integer.parseInt(item.getQuantity());
        }
        if(user.getUserName() != null){
            addUserBtn.setImageDrawable(getResources().getDrawable(R.drawable.baseline_person_24));
        }else {
            addUserBtn.setImageDrawable(getResources().getDrawable(R.drawable.baseline_person_add_alt_24));
        }
        chargeBtn.setText("CHARGE\n" + formatter.format(getTotal(user.getUserItems())) + " $");
        discountTxt.setText(getDiscount(Double.valueOf(getTotal(user.getUserItems()).toString())).toString());
        showOrderDialog.cancel();
    }
}

