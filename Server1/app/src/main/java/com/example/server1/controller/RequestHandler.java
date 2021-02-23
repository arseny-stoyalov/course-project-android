package com.example.server1.controller;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.server1.Constants;
import com.example.server1.dto.ProcessEntity;
import com.example.server1.dto.Server1Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequestHandler {

    private static final String TAG = "RequestHandler";

    public static void sendServer2Request(ProcessEntity[] client, ObjectMapper objectMapper, Context context) {
        ProcessEntity[] thisProcess = {
                new ProcessEntity(
                        context.getApplicationInfo().processName,
                        android.os.Process.myPid(),
                        Thread.activeCount(),
                        getModules(android.os.Process.myPid()))
        };

        List<ProcessEntity> processList = new ArrayList<>(Arrays.asList(client));
        processList.addAll(Arrays.asList(thisProcess));
        Object[] objArray = processList.toArray();
        ProcessEntity[] processes = new ProcessEntity[objArray.length];
        for (int i = 0; i < objArray.length; i++) {
            processes[i] = (ProcessEntity) objArray[i];
        }

        Server1Message request = new Server1Message(Constants.SERVER2_REQUEST, processes);

        try (Socket server2 = new Socket(Constants.HOST, Constants.SERVER2_PORT)) {

            OutputStream out = server2.getOutputStream();

            out.write(objectMapper.writeValueAsBytes(request));

        } catch (IOException e) {
            Log.e(TAG, "Troubles requesting server2", e);
        }
    }

    public static void sendClientResponse(Server1Message server2Response, ObjectMapper objectMapper) {
        try (Socket client = new Socket(Constants.HOST, Constants.CLIENT_PORT)) {

            OutputStream out = client.getOutputStream();

            out.write(objectMapper.writeValueAsBytes(server2Response));

        } catch (IOException e) {
            Log.e(TAG, "Troubles requesting client", e);
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
