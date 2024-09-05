package com.example.salesapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesapp.Models.Order;
import com.example.salesapp.Models.SessionItem;
import com.example.salesapp.R;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    private ArrayList<Order> items;
    private OnClickListner onClickListener;
    private DbItemsListAdapter.OnClickListener dbOnClickListener;
    public OrderListAdapter(OnClickListner onClickListener,DbItemsListAdapter.OnClickListener dbOnClickListener, ArrayList<Order> items) {
        this.onClickListener = onClickListener;
        this.dbOnClickListener = dbOnClickListener;
        this.items =items;
    }

    @NonNull
    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row,parent,false);
        return new OrderListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.ViewHolder holder, int position) {
        Order item = items.get(holder.getAdapterPosition());
        holder.orderId.setText(item.getOrderId());
        holder.timeAgo.setText(item.getTimeStamp());
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.orderItemClick(item.getOrderId());
                dbOnClickListener.afterItemClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       private TextView orderId,timeAgo;
       private ImageButton editBtn,closeBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.TxtOrderId);
            timeAgo = itemView.findViewById(R.id.TxtAgo);
            editBtn = itemView.findViewById(R.id.ImgBtnEdit);
            closeBtn = itemView.findViewById(R.id.closeBtn);
        }
    }
    public interface OnClickListner {
        public void orderItemClick(String orderId);

    }
}
