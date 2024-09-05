package com.example.salesapp.Fragments;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.salesapp.Adapters.DbItemsListAdapter;
import com.example.salesapp.Adapters.SessionItemsListAdapter;
import com.example.salesapp.MainActivity;
import com.example.salesapp.Models.DBItem;
import com.example.salesapp.Models.SessionItem;
import com.example.salesapp.Models.User;
import com.example.salesapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment implements DbItemsListAdapter.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "CartFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private RecyclerView recyclerViewCart;
    private ArrayList<DBItem> dbCartItems = new ArrayList<>();
    private ArrayList<SessionItem> sessionItems;
    private SessionItemsListAdapter cartListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private HomeFragment homeFragment;
    private FragmentManager fragmentManager;
    private ImageButton backArrow;
    private View actionBar;
    private MainActivity mainActivity;
    private DrawerLayout drawerLayout;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        fragmentManager = getParentFragmentManager();
        homeFragment =(HomeFragment) fragmentManager.findFragmentByTag(HomeFragment.TAG);

        User user = homeFragment.getUser();
        sessionItems = user.getUserItems();
        cartListAdapter = new SessionItemsListAdapter(sessionItems);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewCart.setLayoutManager(linearLayoutManager);
        recyclerViewCart.setAdapter(cartListAdapter);
        mainActivity = (MainActivity) getActivity();
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        actionBar = mainActivity.getSupportActionBar().getCustomView();
        backArrow.setVisibility(View.VISIBLE);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backArrow.setVisibility(View.GONE);
                mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction()
                        .remove(new AddUserFragment())
                        .commit();
            }
        });
        return view;
    }

    @Override
    public User onItemClick() {
        return null;
    }

    @Override
    public void afterItemClick() {

    }

    @Override
    public void productInfoClick(DBItem item) {

    }
}