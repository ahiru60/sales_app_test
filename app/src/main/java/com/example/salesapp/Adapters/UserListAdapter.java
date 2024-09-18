package com.example.salesapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesapp.Fragments.AddUserFragment;
import com.example.salesapp.Fragments.UsersFragment;
import com.example.salesapp.MainActivity;
import com.example.salesapp.Models.User;
import com.example.salesapp.R;
import com.example.salesapp.Tools.BitmapQualityReducer;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>{
    private ArrayList<User> users;
    private OnClickListener onClickListener;
    public UserListAdapter(OnClickListener onClickListener,ArrayList<User> users) {
        this.onClickListener = onClickListener;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row,parent,false);
        return new UserListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(holder.getAdapterPosition());
        if(users != null){
        holder.userName.setText(user.getUserName());
        holder.profileImage.setImageBitmap(user.getImage());
        holder.AddUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               User currentUser = onClickListener.addUserOnClick();
               currentUser.setUserName(user.getUserName());
               currentUser.setUserId(user.getUserId());
               currentUser.setImage(user.getImage());
               currentUser.setLocation(user.getLocation());
               currentUser.setGender(user.getGender());
               currentUser.setUserId(user.getUserId());

            }
        });
        }
    }

    @Override
    public int getItemCount() {
        if(users != null){
            return users.size();
        }else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView profileImage;
        private TextView userName;
        private ImageButton AddUserBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);
            AddUserBtn = itemView.findViewById(R.id.add_userBtn);
        }
    }

    public interface OnClickListener{
        public User addUserOnClick();
    }
}
