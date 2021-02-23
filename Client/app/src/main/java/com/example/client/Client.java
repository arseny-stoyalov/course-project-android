package com.example.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.example.client.dto.Server1Message;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Client implements Runnable {

    private static final String TAG = "Client";

    private final TextView txt1;

    private final TextView txt2;

    private final ObjectMapper objectMapper;

    public Client(TextView txt1, TextView txt2, ObjectMapper objectMapper) {
        this.txt1 = txt1;
        this.txt2 = txt2;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run() {
        while(true)
            acceptMessages();
    }

    private void acceptMessages(){
        try (ServerSocket serverSocket = new ServerSocket(Constants.CLIENT_PORT)) {

            StringBuilder builder = new StringBuilder();
            Socket client = serverSocket.accept();
            InputStream in = client.getInputStream();

            String unsafeRequest = new String(IOUtils.toByteArray(in));;
            Server1Message s1Msg;

            Log.i(TAG, "Got response from another process");

            try {
                s1Msg = objectMapper.readValue(unsafeRequest, Server1Message.class);
                builder.append("Process info: \n")
                        .append(Arrays.toString(s1Msg.getEntities()));
                txt1.setText(builder.toString());
            } catch (Exception e) {
                String[] resolution = unsafeRequest.split("_");
                if (resolution.length < 2) return;
                builder.append("Resolution: ")
                        .append(resolution[0])
                        .append("x")
                        .append(resolution[1]);
                txt2.setText(builder.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
