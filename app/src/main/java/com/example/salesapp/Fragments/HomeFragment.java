package com.example.salesapp.Fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.salesapp.Adapters.DbItemsListAdapter;
import com.example.salesapp.Adapters.SessionCartListAdapter;
import com.example.salesapp.Database.DbHandler;
import com.example.salesapp.MainActivity;
import com.example.salesapp.Models.DBCartItem;
import com.example.salesapp.Models.SessionItem;
import com.example.salesapp.Models.User;
import com.example.salesapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements DbItemsListAdapter.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "HomeFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private ArrayList<DBCartItem> items = new ArrayList<>();
    private ArrayList<DBCartItem> searchItems = new ArrayList<>();
    private ArrayList<SessionItem> sessionItems = new ArrayList<>();
    private DbItemsListAdapter itemListAdapter, searchListAdapter;
    private SessionCartListAdapter cartListAdapter;
    private RecyclerView recyclerViewItems, recyclerViewSearch,recyclerViewCart;
    private LinearLayoutManager linearLayoutManagerItems,linearLayoutManagerCart;
    private DbHandler dbHandler;
    private User user;
    private Button chargeBtn;
    private FragmentManager fragmentManager;
    private ImageButton searchBtn,addUserBtn;
    private View actionBar;
    private FrameLayout searchBar;
    private EditText searchEditText,totalEditTxt;
    private TextView cartItemsNumber,totalTxt;
    private MainActivity mainActivity;
    private LinearLayout viewCartBtn,listsView,salesViews,paymentView;
    private ConstraintLayout cartViewsHolder;
    private Button paymentMethCardBtn,paymentMethCashBtn,paymentMethReditBtn;
    private int itemCount;
    private boolean isSearching = false, isCarting = false;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
//        dbHandler.addItemtoItems("Apple", "1500", "3");
//        dbHandler.addItemtoItems("Orange", "1000", "2");
//        dbHandler.addItemtoItems("Mango", "1000", "3");
//        dbHandler.addItemtoItems("Coconut", "300", "5");
//        dbHandler.addItemtoItems("Chocolate", "100", "3");
//        dbHandler.addItemtoItems("Candles", "500", "2");
//        dbHandler.addItemtoItems("King coconut", "1500", "4");
//        dbHandler.addItemtoItems("Beef", "1000", "5");
//        dbHandler.addItemtoItems("Avocado", "500", "3");
//        dbHandler.addItemtoItems("Fruit juice", "400", "2");
//        dbHandler.addItemtoItems("Pineapple", "300", "3");
//        dbHandler.addItemtoItems("Soap", "500", "1");
        setupLogics();
        setupDatabase();
        setupItemsRecyclerView();
        setupViews();
        setupCartRecyclerView();
        return view;
    }
    private void setupLogics(){
        user = new User(null,null,null,sessionItems);
        itemCount = user.getUserCartItems().size();
    }
    private void setupViews(){
        mainActivity = (MainActivity) getActivity();
        fragmentManager = getParentFragmentManager();
        actionBar = mainActivity.getSupportActionBar().getCustomView();
        cartItemsNumber = actionBar.findViewById(R.id.cartItemNumber);
        cartItemsNumber.setText(String.valueOf(itemCount));
        paymentView = view.findViewById(R.id.paymentView);
        totalEditTxt = view.findViewById(R.id.editText_Total);
        totalTxt = view.findViewById(R.id.total);
        listsView = view.findViewById(R.id.listsView);
        searchBar = view.findViewById(R.id.searchbar);
        addUserBtn = actionBar.findViewById(R.id.action_bar_add_user);
        viewCartBtn = actionBar.findViewById(R.id.viewCartBtn);
        salesViews = view.findViewById(R.id.salesViews);
        chargeBtn = view.findViewById(R.id.charge);
        paymentMethCardBtn = view.findViewById(R.id.paymentMethodCardBtn);
        paymentMethCashBtn = view.findViewById(R.id.paymentMethodCashBtn);
        paymentMethReditBtn = view.findViewById(R.id.paymentMethodReditBtn);

        setupSearch();
        setupButtonClicks();
    }

    private void setupItemsRecyclerView(){
        itemListAdapter = new DbItemsListAdapter(items,this);
        linearLayoutManagerItems = new LinearLayoutManager(getActivity());
        recyclerViewItems = view.findViewById(R.id.recyclerViewItems);
        recyclerViewItems.setLayoutManager(linearLayoutManagerItems);
        recyclerViewItems.setAdapter(itemListAdapter);
    }
    private void setupSearch(){
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
                if(s.toString()!= ""){
                    searchItems.clear();
                    searchItems = dbHandler.searchItemByName(s.toString());
                    linearLayoutManagerCart = new LinearLayoutManager(getActivity());
                    recyclerViewSearch.setLayoutManager(linearLayoutManagerCart);
                    searchListAdapter = new DbItemsListAdapter(searchItems,HomeFragment.this);
                    recyclerViewSearch.setAdapter(searchListAdapter);
                    searchListAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void setupCartRecyclerView(){
        cartViewsHolder = view.findViewById(R.id.CartViewsHolder);
        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
    }

    private void setupButtonClicks(){

        chargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getPaymentMethod()!= null){
                    listsView.setVisibility(View.VISIBLE);
                    paymentView.setVisibility(View.GONE);
                    salesViews.setGravity(Gravity.TOP);
                    salesViews.setPadding(0,0,0,0);
                    fragmentManager.beginTransaction()
                            .add(R.id.fragment_container, ChargeFragment.class,null,ChargeFragment.TAG)
                            .setReorderingAllowed(true)
                            .addToBackStack("ChargeFragment")
                            .commit();
                }else{
                    mainActivity.setArrow();
                    listsView.setVisibility(View.GONE);
                    salesViews.setGravity(Gravity.BOTTOM);
                    salesViews.setPadding(0,0,0,30);
                    mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    viewCartBtn.setVisibility(View.GONE);
                    paymentView.setVisibility(View.VISIBLE);
                    String total = String.valueOf(getTotal(user.getUserCartItems()));
                    totalTxt.setText(total);
                    totalEditTxt.setText(total);

                }
            }
        });
        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.setArrow();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, AddUserFragment.class,null,AddUserFragment.TAG)
                        .setReorderingAllowed(true)
                        .addToBackStack("AddUserFragment")
                        .commit();
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSearching){
                    recyclerViewItems.setVisibility(View.VISIBLE);
                    recyclerViewSearch.setVisibility(View.GONE);
                    searchEditText.setText("");
                    searchEditText.setVisibility(View.GONE);
                    searchBtn.setImageResource(R.drawable.baseline_search_24);
                    isSearching = false;
                }
                else{
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
                if(!isCarting){
                    viewCartBtn.setVisibility(View.GONE);
                    searchBar.setVisibility(View.GONE);
                    recyclerViewItems.setVisibility(View.GONE);
                    recyclerViewSearch.setVisibility(View.GONE);
                    cartViewsHolder.setVisibility(View.VISIBLE);
                    recyclerViewCart.setLayoutManager(new LinearLayoutManager(getActivity()));
                    cartListAdapter = new SessionCartListAdapter(user.getUserCartItems());
                    recyclerViewCart.setAdapter(cartListAdapter);
                    TextView total = view.findViewById(R.id.textView_Total);
                    if(user.getUserCartItems().size()>0){
                        String totalString = String.valueOf(getTotal(user.getUserCartItems()));
                        total.setText(totalString);
                    }
                    mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    isCarting =true;
                }

            }
        });

        paymentMethCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setPaymentMethod("Card");
            }
        });
        paymentMethCashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setPaymentMethod("Cash");
            }
        });
        paymentMethReditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setPaymentMethod("Redit");
            }
        });
    }
    private void setupDatabase(){
        dbHandler = new DbHandler(getActivity());
        items = dbHandler.getItems();
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
        itemCount = user.getUserCartItems().size();
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        Double total = 0.00;
        for(SessionItem item :user.getUserCartItems()){
            total = total + Double.parseDouble(item.getPrice())*Integer.parseInt(item.getQuantity());
        }
        chargeBtn.setText("CHARGE\n"+df.format(total)+" $");
        cartItemsNumber.setText(String.valueOf(itemCount));
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

    public void setIsCarting(boolean is){
        isCarting = is;
    }
}