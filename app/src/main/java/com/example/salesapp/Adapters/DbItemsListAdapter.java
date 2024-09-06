package com.example.salesapp.Adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesapp.Models.DBItem;
import com.example.salesapp.Models.SessionItem;
import com.example.salesapp.Models.User;
import com.example.salesapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DbItemsListAdapter extends RecyclerView.Adapter<DbItemsListAdapter.ViewHolder> {
    private ArrayList<DBItem> items;
    private OnClickListener onClickListener;
    public DbItemsListAdapter(ArrayList<DBItem> items, OnClickListener onClickListener) {
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
        String itemId = items.get(holder.getAdapterPosition()).getItemId();
        String itemName = items.get(holder.getAdapterPosition()).getItemName();
        String stock = items.get(holder.getAdapterPosition()).getStock();
        String rawPrice =items.get(holder.getAdapterPosition()).getSellingPrice();
        Bitmap imageBtmp = items.get(holder.getAdapterPosition()).getImageBtmp();

        if(imageBtmp != null){
            holder.image.setImageBitmap(imageBtmp);
        }else{
            holder.image.setImageResource(R.drawable.baseline_image_24);
        }
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
                ArrayList<SessionItem> userCartItems = user.getUserItems();
                if(userCartItems.size()!=0){
                    int index =-1;
                    boolean isFound = false;
                    for(SessionItem item : userCartItems){
                        index++;
                        if(item.getItemName().equals(itemName)){
                            int q = Integer.parseInt(userCartItems.get(index).getQuantity())+1;
                            userCartItems.get(index).setQuantity(String.valueOf(q));
                            isFound = true;
                        }
                    }
                    if(!isFound){
                        userCartItems.add(new SessionItem(itemId,imageBtmp,itemName,"1",rawPrice));
                        System.out.println("df");
                    }
                }else{
                    userCartItems.add(new SessionItem(itemId,imageBtmp,itemName,"1",rawPrice));
                    System.out.println("df");
                }
                onClickListener.afterItemClick();
            }
        });
        holder.productInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.productInfoClick(items.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private ImageButton productInfo;
        private TextView itemName,stock,price;
        private View itemRow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productInfo = itemView.findViewById(R.id.ImgBtn_info);
            itemRow = itemView.findViewById(R.id.item_row);
            image =itemView.findViewById(R.id.user_image);
            itemName = itemView.findViewById(R.id.textView_itemName);
            stock = itemView.findViewById(R.id.textView_stock);
            price = itemView.findViewById(R.id.textView_price);

        }
    }
    public interface OnClickListener{
        User onItemClick();
        void afterItemClick();
        void productInfoClick(DBItem item);
    }
}
