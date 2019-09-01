package ru.markstudio.animals.data;

import java.util.ArrayList;

public class QueryResponse {

    private String message;
    private ArrayList<Animal> data;

    public String getMessage() {
        return message;
    }

    public ArrayList<Animal> getData() {
        return data;
    }
}
