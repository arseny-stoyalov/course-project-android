package com.example.server2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowMetrics;
import android.widget.TextView;

public class Server2Activity extends AppCompatActivity {

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