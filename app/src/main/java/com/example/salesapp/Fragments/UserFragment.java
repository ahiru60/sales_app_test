package com.example.salesapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.salesapp.Database.DbHandler;
import com.example.salesapp.Models.User;
import com.example.salesapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "UserFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private User user;
    private FragmentManager fragmentManager;
    private View view;
    private TextView userName,gender,location;
    private ImageView userImage;
    private Button removeUserBtn;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        DbHandler dbHandler = new DbHandler(getContext());
        view = inflater.inflate(R.layout.fragment_user, container, false);
        fragmentManager = getParentFragmentManager();
        HomeFragment homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(HomeFragment.TAG);
        user = homeFragment.getUser();
        userImage = view.findViewById(R.id.user_Image);
        userName = view.findViewById(R.id.username);
        gender = view.findViewById(R.id.gender);
        location = view.findViewById(R.id.location);
        removeUserBtn = view.findViewById(R.id.remove_user);
        userImage.setImageBitmap(user.getImage());
        userName.setText(user.getUserName());
        gender.setText(user.getGender());
        location.setText(user.getLocation());
        removeUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                user.setUserId(null);
//                user.setLocation(null);
//                user.setGender(null);
//                user.setImage(null);
//                user.setUserName(null);
                homeFragment.removeUser();

            }
        });
        return view;
    }
}