package com.example.salesapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesapp.Models.Receipt;
import com.example.salesapp.R;

import java.util.ArrayList;
import java.util.List;

public class ReceiptsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
   private ArrayList<Receipt> receipts;
   private static String TAG = "ReceiptAdapter";
   private boolean hideDate = true;
    public ReceiptsAdapter(ArrayList<Receipt> receipts,boolean hideDate) {
        this.receipts = receipts;
        this.hideDate = hideDate;
    }
    private String[] dateTime = {"",""};

    @Override
    public int getItemViewType(int position) {
        if(receipts.get(position).isOnlyDate()){
            return 0;
        }else {
            return 1;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View layoutOne = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_receipt, parent, false);
            return new ReceiptViewHolder(layoutOne);
        } else if (viewType == 0) {
            View layoutTwo = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_timestamp, parent, false);
            return new TimestampViewHolder(layoutTwo);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReceiptViewHolder) {
            ((ReceiptViewHolder) holder).receipt.setText(receipts.get(holder.getAdapterPosition()).getReceiptId());
            ((ReceiptViewHolder) holder).price.setText(receipts.get(holder.getAdapterPosition()).getTotal_amount());
            if(!hideDate){
                ((ReceiptViewHolder) holder).date.setVisibility(View.VISIBLE);
                ((ReceiptViewHolder) holder).date.setText(receipts.get(holder.getAdapterPosition()).getTimestamp().split(" ")[0]);
            }else{
                ((ReceiptViewHolder) holder).date.setVisibility(View.GONE);
            }
            ((ReceiptViewHolder) holder).time.setText(receipts.get(holder.getAdapterPosition()).getTimestamp().split(" ")[1]);

        } else if (holder instanceof TimestampViewHolder) {
            TextView date = ((TimestampViewHolder) holder).date;
            date.setText(receipts.get(holder.getAdapterPosition()).getTimestamp().split(" ")[0]);
        }
    }

    @Override
    public int getItemCount() {

        return receipts.size();
    }

    public class ReceiptViewHolder extends RecyclerView.ViewHolder{
        private TextView receipt,time,price,date;
        public ReceiptViewHolder(@NonNull View itemView) {
            super(itemView);
            receipt = itemView.findViewById(R.id.receipt);
            time = itemView.findViewById(R.id.time);
            price = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.date);

        }
    }
    public class TimestampViewHolder extends RecyclerView.ViewHolder{
        private TextView date;
        public TimestampViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.timestamp_date);
        }
    }
}
