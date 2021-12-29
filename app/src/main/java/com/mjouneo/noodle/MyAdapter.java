package com.mjouneo.noodle;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<ItemData> mData;
    private LayoutInflater mInflater;
    private OnItemClickListener onItemClickListener;
    private Handler handler = new Handler();

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
        int index = position;
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtID, txtSec;
        Button btnCancel;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            txtID = itemView.findViewById(R.id.tvID);
            txtSec = itemView.findViewById(R.id.tvSec);
            btnCancel = itemView.findViewById(R.id.btnCancel);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int positon = getAdapterPosition();
                        if (positon != RecyclerView.NO_POSITION){
                            listener.onCancelCLick(positon);
                        }
                    }
                }
            });
        }
    }
}
