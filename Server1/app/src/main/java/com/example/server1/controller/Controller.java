package com.example.server1.controller;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.example.server1.Constants;
import com.example.server1.dto.Server1Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.ACTIVITY_SERVICE;

public class Controller implements Runnable {

    private static final String TAG = "Controller";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Context context;

    public Controller(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
       while(true)
           acceptMessages();
    }

    private void acceptMessages(){
        try (ServerSocket serverSocket = new ServerSocket(Constants.SERVER1_PORT)) {

            Socket client = serverSocket.accept();
            InputStream in = client.getInputStream();

            Server1Message msg = objectMapper.readValue(in, Server1Message.class);

            Log.i(TAG, "Got request from another process, request code: " + msg.getRequest());

            switch (msg.getRequest()){
                case Constants.CLIENT_REQUEST:
                    new Thread(() -> {
                        RequestHandler.sendServer2Request(msg.getEntities(), objectMapper, context);
                    }).start();
                    break;
                case Constants.SERVER2_REQUEST:
                    new Thread(() -> {
                        RequestHandler.sendClientResponse(msg, objectMapper);
                    }).start();
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
