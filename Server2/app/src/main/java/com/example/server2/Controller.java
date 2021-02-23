package com.example.server2;

import android.app.Activity;
import android.util.Log;

import com.example.server2.dto.Server1Message;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Controller implements Runnable{

    private static final String TAG = "Controller";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Activity activity;

    public Controller(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        while(true)
            acceptMessages();
    }

    private void acceptMessages(){
        try (ServerSocket serverSocket = new ServerSocket(Constants.SERVER2_PORT)) {

            Socket clientProcess = serverSocket.accept();
            InputStream in = clientProcess.getInputStream();

            String unsafeRequest = new String(IOUtils.toByteArray(in));

            Log.i(TAG, "Got request from another process");
            try {
                Server1Message msg = objectMapper.readValue(unsafeRequest, Server1Message.class);
                RequestHandler.sendServer1Request(msg.getEntities(), objectMapper, activity);
            } catch (Exception e){
                e.printStackTrace();
                if (unsafeRequest.equals(Constants.CLIENT_REQUEST))
                    RequestHandler.sendClientResponse(activity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
