package com.example.client;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.client.dto.ProcessEntity;
import com.example.client.dto.Server1Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequestSender {

    private static final String TAG = "RequestSender";

    public static void sendToServer1(ObjectMapper objectMapper, Context context){
        ProcessEntity[] process = {
                new ProcessEntity(
                        context.getApplicationInfo().processName,
                        android.os.Process.myPid(),
                        Thread.activeCount(),
                        getModules(android.os.Process.myPid()))
        };

        Server1Message request = new Server1Message(Constants.CLIENT_REQUEST, process);

        try (Socket server1 = new Socket(Constants.HOST, Constants.SERVER1_PORT)) {

            OutputStream out = server1.getOutputStream();

            out.write(objectMapper.writeValueAsBytes(request));

        } catch (IOException e) {
            Log.e(TAG, "Troubles requesting server1", e);
        }
    }

    public static void sendToServer2(){

        try (Socket server2 = new Socket(Constants.HOST, Constants.SERVER2_PORT)) {

            OutputStream out = server2.getOutputStream();

            out.write(Constants.SERVER2_REQUEST.getBytes());

        } catch (IOException e) {
            Log.e(TAG, "Troubles requesting server2", e);
        }
    }

    private static int getModules(int pId) {
        Set<String> libs = new HashSet<>();
        String mapsFile = "/proc/" + pId + "/maps";
        try (BufferedReader reader = new BufferedReader(new FileReader(mapsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith(".so")) {
                    int n = line.lastIndexOf(" ");
                    libs.add(line.substring(n + 1));
                }
            }
            Log.d(TAG, libs.size() + " libraries:");
            for (String lib : libs) {
                Log.d(TAG, lib);
            }
            return libs.size();
        } catch (Exception e) {
            Log.e(TAG, "Got: ", e);
        }
        return -1;
    }

}
