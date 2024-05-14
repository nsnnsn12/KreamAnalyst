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
        String bearerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmcmVzaCI6dHJ1ZSwiaWF0IjoxNzE1Njc2NTM1LCJqdGkiOiI2N2QzMWVjZC1jMzkxLTQyMWItOGU1Ni1mZTVkMDMzMDkzMmYiLCJ0eXBlIjoiYWNjZXNzIiwiaWRlbnRpdHkiOjczODcwMzQsIm5iZiI6MTcxNTY3NjUzNSwiY3NyZiI6ImE5YjFjMzlhLTg2OTItNDBiNS04MmVkLTIyOGVjM2M5NTZkOCIsImV4cCI6MTcxNTY4MzczNSwidWMiOnsic2FmZSI6dHJ1ZX19.RVkeuFFiioJif5C-cGLjKw7GbOA5qxHkxE-6g2N3MxA";
        KreamRequestCreator kreamRequestCreator = new KreamRequestCreator(bearerToken);
        KreamApi kreamApi = new KreamApiV1(mapper, kreamRequestCreator, httpExcuter);
        KreamTradeInfoMaker kreamTradeInfoMaker = new KreamTradeInfoMaker(kreamRequestCreator, httpExcuter, mapper);
        kreamTradeInfoMaker.getDistanceCount(12831L, "");
//        List<KreamItemDto> items = kreamApi.getItems(50);
//        ExcelMaker excelMaker = new ExcelMaker(items);
//        excelMaker.export();

    }
}