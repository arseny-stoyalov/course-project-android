package com.example.server2;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.example.server2.dto.ProcessEntity;
import com.example.server2.dto.Server1Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequestHandler {

    private static final String TAG = "RequestHandler";

    public static void sendServer1Request(ProcessEntity[] processes, ObjectMapper objectMapper, Context context) {
        ProcessEntity[] thisProcess = {
                new ProcessEntity(
                        context.getApplicationInfo().processName,
                        android.os.Process.myPid(),
                        Thread.activeCount(),
                        getModules(android.os.Process.myPid()))
        };

        List<ProcessEntity> processList = new ArrayList<>(Arrays.asList(processes));
        processList.addAll(Arrays.asList(thisProcess));
        Object[] objArray = processList.toArray();
        ProcessEntity[] totalProcesses = new ProcessEntity[objArray.length];
        for (int i = 0; i < objArray.length; i++) {
            totalProcesses[i] = (ProcessEntity) objArray[i];
        }

        Server1Message request = new Server1Message(Constants.SERVER1_REQUEST, totalProcesses);

        try (Socket server1 = new Socket(Constants.HOST, Constants.SERVER1_PORT)) {

            OutputStream out = server1.getOutputStream();

            out.write(objectMapper.writeValueAsBytes(request));

        } catch (IOException e) {
            Log.e(TAG, "Troubles requesting server1", e);
        }
    }

    public static void sendClientResponse(Context context) {
        try (Socket client = new Socket(Constants.HOST, Constants.CLIENT_PORT)) {

            OutputStream out = client.getOutputStream();

            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            String resolution = width + "_" + height;

            out.write(resolution.getBytes());

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
