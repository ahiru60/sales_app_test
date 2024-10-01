package com.example.salesapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.salesapp.Adapters.UserListAdapter;
import com.example.salesapp.Database.DbHandler;
import com.example.salesapp.MainActivity;
import com.example.salesapp.Models.User;
import com.example.salesapp.R;
public class UsersFragment extends Fragment implements UserListAdapter.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "UsersFragment";
    private String mParam1;
    private String mParam2;

    private View view;
    private RecyclerView recyclerView;
    private HomeFragment homeFragment;
    private DbHandler dbHandler;
    private UserListAdapter userListAdapter;
    private Button addNewUserBtn;
    private FragmentManager fragmentManager;
    private MainActivity mainActivity;
    private View actionBar;
    private ImageButton actionBarAddUserBtn;
    private EditText searchUsers;

    public UsersFragment() {
        // Required empty public constructor
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
        view = inflater.inflate(R.layout.fragment_users, container, false);
        dbHandler = new DbHandler(getContext());
        fragmentManager = getParentFragmentManager();
        mainActivity = (MainActivity) getActivity();
        actionBar = mainActivity.getSupportActionBar().getCustomView();
        actionBarAddUserBtn = actionBar.findViewById(R.id.action_bar_add_user);
        homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(HomeFragment.TAG);
        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        userListAdapter = new UserListAdapter(this,dbHandler.getUsers());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(userListAdapter);
        searchUsers = view.findViewById(R.id.search_user);
        addNewUserBtn = view.findViewById(R.id.add_new_userBtn);
        addNewUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    fragmentManager.beginTransaction()
                            .add(R.id.fragment_container, AddUserFragment.class, null, AddUserFragment.TAG)
                            .setReorderingAllowed(true)
                            .addToBackStack("AddUserFragment")
                            .commit();
            }
        });
        searchUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userListAdapter = new UserListAdapter(UsersFragment.this,dbHandler.searchUserByName(s.toString()));
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(userListAdapter);
            }
        });
        return view;
    }

    @Override
    public User addUserOnClick() {
        mainActivity.backFragments();
        actionBarAddUserBtn.setImageDrawable(getResources().getDrawable(R.drawable.baseline_person_24));
        mainActivity.homeSetActions(true);
        return homeFragment.getUser();

    }
}