package org.example;

import org.example.excel.ExcelMaker;
import org.example.kream_api.KreamApi;
import org.example.kream_api.KreamApiV1;
import org.example.kream_api.dto.KreamItemDto;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        String bearerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmcmVzaCI6dHJ1ZSwiaWF0IjoxNzE1NjY5MDk2LCJqdGkiOiIyYmFiNjlmYS1jZTRiLTRhNWEtYjBjNi05ZWU5YWI5ZDk2NjIiLCJ0eXBlIjoiYWNjZXNzIiwiaWRlbnRpdHkiOjczODcwMzQsIm5iZiI6MTcxNTY2OTA5NiwiY3NyZiI6ImRhMGEyNDUyLWM3NzAtNGJhMy04MjE4LWE3ZWI2NGYyNWZmYyIsImV4cCI6MTcxNTY3NjI5NiwidWMiOnsic2FmZSI6dHJ1ZX19._1G5QhagLOC9tlqkdJhXr4uXs_ZEgcbQnbUQRbS8Ztg";
        KreamApi kreamApi = new KreamApiV1(bearerToken);
        List<KreamItemDto> items = kreamApi.getItems(50);
//        ExcelMaker excelMaker = new ExcelMaker(items);
//        excelMaker.export();

    }
}