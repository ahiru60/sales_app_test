package com.example.salesapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.salesapp.Adapters.UserListAdapter;
import com.example.salesapp.Database.DbHandler;
import com.example.salesapp.MainActivity;
import com.example.salesapp.Models.User;
import com.example.salesapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment implements UserListAdapter.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "UsersFragment";

    // TODO: Rename and change types of parameters
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

    public UsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsersFragment newInstance(String param1, String param2) {
        UsersFragment fragment = new UsersFragment();
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
        return view;
    }

    @Override
    public User addUserOnClick() {
        mainActivity.backFragments();
        actionBarAddUserBtn.setImageDrawable(getResources().getDrawable(R.drawable.baseline_person_24));
        mainActivity.setArrow(true);
        return homeFragment.getUser();

    }
}