package com.example.salesapp.Adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesapp.Models.SessionItem;
import com.example.salesapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SessionCartListAdapter extends RecyclerView.Adapter<SessionCartListAdapter.ViewHolder> {
    private ArrayList<SessionItem> items;
    public SessionCartListAdapter(ArrayList<SessionItem> items ) {
        this.items = items;
    }

    @NonNull
    @Override
    public SessionCartListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionCartListAdapter.ViewHolder holder, int position) {
        String itemName = items.get(position).getItemName();
        String quantity = items.get(position).getQuantity();
        String rawPrice =items.get(position).getPrice();
        Bitmap imageBtmp = items.get(position).getImage();

//        if(imageBtmp != null){
//            holder.image.setImageBitmap(imageBtmp);
//        }
        holder.itemName.setText(itemName);
        holder.quantity.setText(quantity);
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        double unFormatedFloat = Float.parseFloat(rawPrice);
        String price = df.format(unFormatedFloat);
        holder.price.setText(price+"$");
        holder.itemRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView itemName,quantity,price;
        private View itemRow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemRow = itemView.findViewById(R.id.item_row);
            image =itemView.findViewById(R.id.imageView_image);
            itemName = itemView.findViewById(R.id.textView_itemName);
            quantity = itemView.findViewById(R.id.textView_stock);
            price = itemView.findViewById(R.id.textView_price);

        }
    }
}
