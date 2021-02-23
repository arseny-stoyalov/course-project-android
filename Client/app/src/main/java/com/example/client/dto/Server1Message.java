package com.example.client.dto;

public class Server1Message {

    private int request;

    private ProcessEntity[] entities;

    public Server1Message(int request, ProcessEntity[] entities) {
        this.request = request;
        this.entities = entities;
    }

    public Server1Message() {
    }

    public int getRequest() {
        return request;
    }

    public void setRequest(int request) {
        this.request = request;
    }

    public ProcessEntity[] getEntities() {
        return entities;
    }

    public void setEntities(ProcessEntity[] entities) {
        this.entities = entities;
    }

}
