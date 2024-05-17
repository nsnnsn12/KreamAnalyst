package org.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.http.HttpExcuter;
import org.example.kream_api.KreamApi;
import org.example.kream_api.KreamApiV1;
import org.example.kream_api.KreamRequestCreator;
import org.example.kream_api.KreamTradeInfoMaker;
import org.example.user.User;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        HttpExcuter httpExcuter = new HttpExcuter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        User user = new User("gkdlshtjdrb@naver.com", "rmaaud123!");

        KreamRequestCreator kreamRequestCreator = new KreamRequestCreator(user);
        KreamApi kreamApi = new KreamApiV1(mapper, kreamRequestCreator, httpExcuter);
        KreamTradeInfoMaker kreamTradeInfoMaker = new KreamTradeInfoMaker(kreamRequestCreator, httpExcuter, mapper);
        kreamTradeInfoMaker.getDistanceCount(12831L, "");
//        List<KreamItemDto> items = kreamApi.getItems(50);
//        ExcelMaker excelMaker = new ExcelMaker(items);
//        excelMaker.export();

    }
}