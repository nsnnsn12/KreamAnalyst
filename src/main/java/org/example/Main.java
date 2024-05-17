package org.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.example.http.HttpExcuter;
import org.example.kream_api.KreamApi;
import org.example.kream_api.KreamApiV1;
import org.example.kream_api.KreamRequestCreator;
import org.example.kream_api.dto.KreamPage;
import org.example.kream_api.dto.KreamTradeItem;
import org.example.service.KreamAnalyst;
import org.example.service.dto.TradeInfo;
import org.example.user.User;

@Log4j2
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        HttpExcuter httpExcuter = new HttpExcuter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        User user = new User("gkdlshtjdrb@naver.com", "rmaaud123!");

        KreamRequestCreator kreamRequestCreator = new KreamRequestCreator(user);
        KreamApi kreamApi = new KreamApiV1(mapper, kreamRequestCreator, httpExcuter);
        user.login(kreamApi);
//        KreamPage<KreamTradeItem> page = kreamApi.getKreamTradeItemPageByProdcutId(285138L, 1);
        KreamAnalyst kreamAnalyst = new KreamAnalyst(kreamApi);
//        kreamAnalyst.analyze(50, 101);
        TradeInfo tradeInfo = new TradeInfo();
        kreamAnalyst.setTradeInfo(tradeInfo, 134094L, 1);
        log.info("tradeInfo.getTotalPrice1dayAgo():{}",tradeInfo.getTotalPrice1dayAgo() / tradeInfo.getCount1dayAgo());
        log.info("tradeInfo.getTotalPrice2dayAgo():{}",tradeInfo.getTotalPrice2dayAgo() / tradeInfo.getCount2dayAgo());
        log.info("tradeInfo.getTotalPrice1weekAgo():{}",tradeInfo.getTotalPrice1weekAgo() / tradeInfo.getCount1weekAgo());
        log.info("tradeInfo.getTotalPrice2weekAgo():{}",tradeInfo.getTotalPrice2weekAgo() / tradeInfo.getCount2weekAgo());
        log.info("tradeInfo: {}", tradeInfo);
//        KreamTradeInfoMaker kreamTradeInfoMaker = new KreamTradeInfoMaker(kreamRequestCreator, httpExcuter, mapper);
//        kreamTradeInfoMaker.getDistanceCount(12831L, "");
//        List<KreamItemDto> items = kreamApi.getItems(50);
//        ExcelMaker excelMaker = new ExcelMaker(items);
//        excelMaker.export();

    }
}