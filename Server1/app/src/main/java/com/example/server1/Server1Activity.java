package com.example.server1;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.server1.controller.Controller;

public class Server1Activity extends AppCompatActivity {

    private static final String TAG = "Server1Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "Process id: "+ android.os.Process.myPid());
        TextView txtMain = (TextView) findViewById(R.id.txtMain);
        startServer();
        txtMain.setText("Server started");
    }

    void startServer() {
        new Thread(new Controller(this)).start();
    }



}