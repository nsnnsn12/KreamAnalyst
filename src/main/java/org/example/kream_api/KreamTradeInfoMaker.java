package org.example.kream_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.http.HttpExcuter;
import org.example.kream_api.dto.CursorInfo;
import org.example.kream_api.dto.KreamParameters;
import org.example.kream_api.dto.KreamTradeItem;
import org.example.kream_api.dto.KreamTradePage;

import java.net.http.HttpRequest;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
public class KreamTradeInfoMaker {
    private final KreamRequestCreator kreamRequestCreator;
    private final HttpExcuter httpExcuter;
    private final ObjectMapper mapper;

    public Long getDistanceCount(Long productId, String date){
        LocalDate now = LocalDate.now();
        LocalDate targetDate = now.minusDays(14);
        log.info("now: {}", now);
        log.info("localDate: {}", targetDate);
        Long result = 0L;
        int cursor = 1;
        boolean isFind = false;
        while(!isFind){
            HttpRequest request = kreamRequestCreator.createRequestForProductDetail(productId, "sales", KreamParameters.builder().cursor(cursor++).sort("date_created[desc]").build());
            String responseJson = httpExcuter.executeHttp(request);
            log.info("KreamTradeInfoMaker");
            KreamTradePage kreamTradePage = toKreamTradeInfos(responseJson);
            isFind = isFind(kreamTradePage.getKreamTradeItems(), targetDate);
        }
        return result;
    }

    //계속 반복하면서
    //앞뒤의 값을 검사한다.
    //total
    //현재 cursor

    private boolean isFind(List<KreamTradeItem> kreamTradeItems, LocalDate targetDate){
        for (KreamTradeItem kreamTradeItem : kreamTradeItems) {
            LocalDate selectedDate = getDate(kreamTradeItem);
            if(targetDate.isAfter(selectedDate)) return true;
        }
        return false;
    }

    private LocalDate getDate(KreamTradeItem kreamTradeItem){
        OffsetDateTime offsetDateTime = Instant.parse(kreamTradeItem.getDate_created()).atOffset(ZoneOffset.UTC);
        return offsetDateTime.toLocalDate();
    }

    private KreamTradePage toKreamTradeInfos(String json) {
        KreamTradePage result = new KreamTradePage();
        try {
            List<KreamTradeItem> kreamTradeItems = new ArrayList<>();
            JsonNode root = mapper.readTree(json);
            CursorInfo cursorInfo = mapper.treeToValue(root, CursorInfo.class);
            log.info(cursorInfo);
            JsonNode items = root.get("items");
            Iterator<JsonNode> elements = items.elements();
            while(elements.hasNext()){
                JsonNode item = elements.next();
                KreamTradeItem kreamItemDto = mapper.treeToValue(item, KreamTradeItem.class);
                log.info(kreamItemDto);
                kreamTradeItems.add(kreamItemDto);
            }
            result.setCursorInfo(cursorInfo);
            result.setKreamTradeItems(kreamTradeItems);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
