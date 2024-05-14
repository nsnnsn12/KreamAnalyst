package org.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.http.HttpExcuter;
import org.example.kream_api.KreamApi;
import org.example.kream_api.KreamApiV1;
import org.example.kream_api.KreamRequestCreator;
import org.example.kream_api.KreamTradeInfoMaker;
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        HttpExcuter httpExcuter = new HttpExcuter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        String bearerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmcmVzaCI6dHJ1ZSwiaWF0IjoxNzE1NjY5MDk2LCJqdGkiOiIyYmFiNjlmYS1jZTRiLTRhNWEtYjBjNi05ZWU5YWI5ZDk2NjIiLCJ0eXBlIjoiYWNjZXNzIiwiaWRlbnRpdHkiOjczODcwMzQsIm5iZiI6MTcxNTY2OTA5NiwiY3NyZiI6ImRhMGEyNDUyLWM3NzAtNGJhMy04MjE4LWE3ZWI2NGYyNWZmYyIsImV4cCI6MTcxNTY3NjI5NiwidWMiOnsic2FmZSI6dHJ1ZX19._1G5QhagLOC9tlqkdJhXr4uXs_ZEgcbQnbUQRbS8Ztg";
        KreamRequestCreator kreamRequestCreator = new KreamRequestCreator(bearerToken);
        KreamApi kreamApi = new KreamApiV1(mapper, kreamRequestCreator, httpExcuter);
        KreamTradeInfoMaker kreamTradeInfoMaker = new KreamTradeInfoMaker(kreamRequestCreator, httpExcuter, mapper);
        kreamTradeInfoMaker.getDistanceCount(12831L, "");
//        List<KreamItemDto> items = kreamApi.getItems(50);
//        ExcelMaker excelMaker = new ExcelMaker(items);
//        excelMaker.export();

    }
}