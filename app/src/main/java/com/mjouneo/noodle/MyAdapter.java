package com.mjouneo.noodle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private final List<ItemData> mData;
    private final LayoutInflater mInflater;
    private OnItemClickListener onItemClickListener;

    MyAdapter(Context context, List<ItemData> data){
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row, parent, false);
        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        List<String> IDList = mData.get(position).ID;
        holder.txtID.setText(IDList.toString());
        holder.txtSec.setText(mData.get(position).timeLeft);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void removeFinish(ItemData itemData){
        mData.remove(itemData);
    }
    public interface OnItemClickListener{
        void onCancelCLick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtID, txtSec;
        Button btnCancel;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            txtID = itemView.findViewById(R.id.tvID);
            txtSec = itemView.findViewById(R.id.tvSec);
            btnCancel = itemView.findViewById(R.id.btnCancel);

            btnCancel.setOnClickListener(view -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onCancelCLick(position);
                    }
                }
            });
        }
    }
}
