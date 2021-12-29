package com.mjouneo.noodle;

import android.os.Build;
import android.os.CountDownTimer;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ItemData {
    List<String> ID = new ArrayList<>();
    int Second;
    CountDownTimer countDownTimer;
    String timeLeft = "null : null";
    ItemData itemData = this;
    MyAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.S)
    public ItemData(List<String> ID, int second, MyAdapter adapter){
        this.ID = List.copyOf(ID);
        this.Second = second * 1000;
        this.adapter = adapter;
        countDownTimer = new CountDownTimer(Second, 1000) {
            @Override
            public void onTick(long l) {
                int miniutes = (int) (l / 1000) / 60;
                int second = (int) (l / 1000) % 60;
                timeLeft = String.format(Locale.getDefault(), "%02d : %02d", miniutes, second);
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
