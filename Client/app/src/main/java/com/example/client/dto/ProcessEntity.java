package com.example.client.dto;


import androidx.annotation.NonNull;

public class ProcessEntity {

    private String processName;

    private int pId;

    private int threadCount;

    private int moduleCount;

    public ProcessEntity(String processName, int pId, int threadCount, int moduleCount) {
        this.processName = processName;
        this.pId = pId;
        this.threadCount = threadCount;
        this.moduleCount = moduleCount;
    }

    public ProcessEntity() {
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getPId() {
        return pId;
    }

    public void setPId(int pId) {
        this.pId = pId;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getModuleCount() {
        return moduleCount;
    }

    public void setModuleCount(int moduleCount) {
        this.moduleCount = moduleCount;
    }

    @NonNull
    @Override
    public String toString() {
        return processName +
                ": pid = " + pId +
                " threads = " + threadCount +
                " modules = " + moduleCount;
    }
}
