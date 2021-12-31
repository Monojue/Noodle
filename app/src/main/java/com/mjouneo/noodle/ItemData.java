package com.mjouneo.noodle;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.CountDownTimer;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.Locale;

public class ItemData {
    List<String> ID;
    int Second;
    CountDownTimer countDownTimer;
    String timeLeft = "null : null";
    ItemData itemData = this;
    MyAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint("NotifyDataSetChanged")
    public ItemData(List<String> ID, int second, MyAdapter adapter){
        this.ID = List.copyOf(ID);
        this.Second = second * 1000;
        this.adapter = adapter;
        countDownTimer = new CountDownTimer(Second, 1000) {

            @Override
            public void onTick(long l) {
                int minutes = (int) (l / 1000) / 60;
                int second = (int) (l / 1000) % 60;
                timeLeft = String.format(Locale.getDefault(), "%02d : %02d", minutes, second);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                adapter.removeFinish(itemData);
                adapter.notifyDataSetChanged();
            }
        };
    }
}
