package org.example;

import org.example.kream_api.KreamApi;
import org.example.kream_api.KreamApiV1;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        KreamApi kreamApi = new KreamApiV1();
        kreamApi.getItems(50);
    }
}