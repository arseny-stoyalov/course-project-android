package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientActivity extends AppCompatActivity {

    private static final String TAG = "ClientActivity";

    private Button btn1;
    private Button btn2;

    private TextView txt1;
    private TextView txt2;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.btnServer1);
        btn2 = findViewById(R.id.btnServer2);
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        Log.i(TAG, "Process id: "+ android.os.Process.myPid());
        start();
    }

    void start(){
        Log.i(TAG, "Starting client");
        new Thread(new Client(txt1, txt2, objectMapper)).start();
        btn1.setOnClickListener(view -> {
            new Thread(() -> {
                RequestSender.sendToServer1(objectMapper, this);
            }).start();
        });
        btn2.setOnClickListener(view -> {
            new Thread(RequestSender::sendToServer2).start();
        });
    }

}