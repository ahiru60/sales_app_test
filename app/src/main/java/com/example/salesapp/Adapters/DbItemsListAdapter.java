package com.example.salesapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesapp.Models.DBCartItem;
import com.example.salesapp.Models.SessionItem;
import com.example.salesapp.Models.User;
import com.example.salesapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DbItemsListAdapter extends RecyclerView.Adapter<DbItemsListAdapter.ViewHolder> {
    private ArrayList<DBCartItem> items;
    private OnClickListener onClickListener;
    public DbItemsListAdapter(ArrayList<DBCartItem> items, OnClickListener onClickListener) {
        this.items = items;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public DbItemsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DbItemsListAdapter.ViewHolder holder, int position) {
        String itemName = items.get(position).getItemName();
        String stock = items.get(position).getStock();
        String rawPrice =items.get(position).getPrice();

        holder.itemName.setText(itemName);
        holder.stock.setText(stock+" in Stock");
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        double unFormatedFloat = Float.parseFloat(rawPrice);
        String price = df.format(unFormatedFloat);
        holder.price.setText(price+"$");
        holder.itemRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = onClickListener.onItemClick();
                ArrayList<SessionItem> userCartItems = user.getUserCartItems();
                if(userCartItems.size()!=0){
                    int index =-1;
                    boolean isFound = false;
                    for(SessionItem item : userCartItems){
                        index++;
                        if(item.getItemName() == itemName){
                            int q = Integer.parseInt(userCartItems.get(index).getQuantity())+1;
                            userCartItems.get(index).setQuantity(String.valueOf(q));
                            isFound = true;
                        }
                    }
                    if(!isFound){
                        userCartItems.add(new SessionItem(itemName,"1",rawPrice));
                        System.out.println("df");
                    }
                }else{
                    userCartItems.add(new SessionItem(itemName,"1",rawPrice));
                    System.out.println("df");
                }

                onClickListener.afterItemClick();

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView itemName,stock,price;
        private View itemRow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemRow = itemView.findViewById(R.id.item_row);
            image =itemView.findViewById(R.id.imageView_image);
            itemName = itemView.findViewById(R.id.textView_itemName);
            stock = itemView.findViewById(R.id.textView_stock);
            price = itemView.findViewById(R.id.textView_price);

        }
    }
    public interface OnClickListener{
        User onItemClick();
        void afterItemClick();
    }
}
