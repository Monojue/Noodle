package com.mjouneo.noodle;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    RecyclerView recyclerView;
    Button btnAddDevice, btnGo;
    EditText editDeviceID, editTimer;
    TextView tvListOfDevice;

    List<ItemData> itemDataList = new ArrayList<>();
    List<String> IDList = new ArrayList<>();
    CommandBuilder commandBuilder = new CommandBuilder();
    int second = 0;
    String response;
    ItemData itemData;
    MyAdapter adapter;
    ProgressDialog progressDialog;
    int index;

    @SuppressLint({"NotifyDataSetChanged", "SetJavaScriptEnabled"})
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        recyclerView = findViewById(R.id.recyclerview);
        btnAddDevice = findViewById(R.id.btnAddDevice);
        btnGo = findViewById(R.id.btnsetTimer);
        editDeviceID = findViewById(R.id.edit_deviceID);
        editTimer = findViewById(R.id.edit_Second);
        tvListOfDevice = findViewById(R.id.tvlistofdevice);

        progressDialog = new ProgressDialog(this);

        // set up the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(this, itemDataList);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
//                itemDataList.get(position).countDownTimer.cancel();
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Are you sure want to Cancel!")
                    .setTitle("Cancel")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        response = "";
                        index = position;
                        //ToDo : get ID form cancel row
                        webView.loadUrl(commandBuilder.stopTimer(IDList).build());
                        adapter.notifyDataSetChanged();
                        progressDialog.setMessage("Canceling...");
                        progressDialog.show();
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel()).create().show();
        });

        btnAddDevice.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(editDeviceID.getText().toString()) && TextUtils.isDigitsOnly(editDeviceID.getText().toString())){
                String id = editDeviceID.getText().toString();
                if (IDList.contains(id)){
                    IDList.remove(id);
                }else {
                    IDList.add(id);
                }
                tvListOfDevice.setText(IDList.toString());
                editDeviceID.setText("");
            }
        });

        btnGo.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(editTimer.getText().toString()) && TextUtils.isDigitsOnly(editTimer.getText().toString())  && IDList.size() > 0){
                second = Integer.parseInt(editTimer.getText().toString());
                response = "";
                webView.loadUrl(commandBuilder.startTimer(IDList, second).build());
                itemData = new ItemData(IDList, second, adapter);
                progressDialog.setMessage("Sending...");
                progressDialog.show();
            }
        });

        progressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                new CountDownTimer(5000, 1000){
                    @Override
                    public void onTick(long l) {
                        if (!progressDialog.isShowing()) this.cancel();
                    }

                    @Override
                    public void onFinish() {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Connection Failed!", Toast.LENGTH_SHORT).show();
                    }
                }.start();
            }
        });

        progressDialog.setOnDismissListener(dialogInterface -> {
            adapter.notifyDataSetChanged();
            tvListOfDevice.setText(IDList.toString());
            if (IDList.isEmpty()) editTimer.setText("");
        });

        WebViewClient webViewClient = new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:HtmlViewer.showHTML" +
                        "('<body>'+document.getElementsByTagName('body')[0].innerHTML+'</body>');");
            }
        };
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(webViewClient);
        webView.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");
    }

    class MyJavaScriptInterface {

        private final Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String html) {
            response = html.substring(html.indexOf(':') + 1, html.lastIndexOf(':'));
            if("OK".equals(response)){
                itemData.countDownTimer.start();
                itemDataList.add(itemData);
                IDList.clear();
                progressDialog.dismiss();
            }else if ("STOP".equals(response)){
                itemDataList.get(index).countDownTimer.cancel();
                itemDataList.remove(index);
                Toast.makeText(ctx, "Timer Cancel Successfully!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }
}