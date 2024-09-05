package com.example.salesapp.Fragments;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.salesapp.MainActivity;
import com.example.salesapp.Models.User;
import com.example.salesapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddUserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "AddUserFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private User user;
    private HomeFragment homeFragment;
    private FragmentManager fragmentManager;
    private EditText firstName,lastName,gender,location;
    private Button clear,addUser;
    private View actionBar;
    private ImageButton imageButton;
    private View view;
    private ImageButton backArrow,actionBarAddUserBtn;
    private MainActivity mainActivity;
    private LinearLayout viewCartBtn;

    public AddUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddUserFragment newInstance(String param1, String param2) {
        AddUserFragment fragment = new AddUserFragment();
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
                mainActivity.setArrow(true);
                fragmentManager.beginTransaction()
                        .remove(new AddUserFragment())
                        .commit();
                fragmentManager.popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_user, container, false);
        homeFragment = (HomeFragment) getActivity().getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
        user = homeFragment.getUser();
        setupViews();
        return view;
    }
    private void setupViews() {
        fragmentManager = getParentFragmentManager();
        mainActivity = (MainActivity) getActivity();
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        actionBar = mainActivity.getSupportActionBar().getCustomView();
        viewCartBtn = actionBar.findViewById(R.id.viewCartBtn);
        viewCartBtn.setVisibility(View.GONE);
        firstName = view.findViewById(R.id.editText_firstName);
        lastName = view.findViewById(R.id.editText_lastName);
        gender = view.findViewById(R.id.editText_gender);
        location = view.findViewById(R.id.editText_address);
        actionBarAddUserBtn = actionBar.findViewById(R.id.action_bar_add_user);
        addUser = view.findViewById(R.id.addUser);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstName != null||lastName != null||gender != null|| location != null){
                    user.setUserName(firstName.getText().toString()+" "+lastName.getText().toString());
                    user.setGender(gender.getText().toString());
                    user.setLocation(location.getText().toString());
                    actionBarAddUserBtn.setImageDrawable(getResources().getDrawable(R.drawable.baseline_person_24));
                    mainActivity.setArrow(true);
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction()
                            .remove(new AddUserFragment())
                            .commit();
                }else{
                    Toast.makeText(getActivity(),"Fill all fields",Toast.LENGTH_LONG);
                }

            }
        });
        clear = view.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName.setText("");
                lastName.setText("");
                gender.setText("");
                location.setText("");
            }
        });
    }
    private void setupOnbackPressed(){
        viewCartBtn.setVisibility(View.VISIBLE);
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainActivity.setArrow(true);
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction()
                .remove(new AddUserFragment())
                .commit();
    }
}