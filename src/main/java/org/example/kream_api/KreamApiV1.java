package org.example.kream_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.http.HttpExcuter;
import org.example.kream_api.dto.KreamItemDto;
import org.example.kream_api.dto.KreamPage;
import org.example.kream_api.dto.KreamParameters;
import org.example.kream_api.dto.KreamTradeItem;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class KreamApiV1 implements KreamApi{
    private final int PER_PAGE_SIZE = 50;

    private final ObjectMapper mapper;
    private final KreamRequestCreator kreamRequestCreator;
    private final HttpExcuter httpExcuter;


    @Override
    public List<KreamItemDto> getItems(int wantedCount, int startRank) {
        List<KreamItemDto> result = new ArrayList<>();
        log.info("wantedCount: {}", wantedCount);
        validWantedCount(wantedCount);
        int count = wantedCount / PER_PAGE_SIZE;
        int startCursor = getCursor(startRank);
        for(int i = 0; i < count; i++){
            result.addAll(getPopularItems(startCursor + i));
        }
        return result;
    }

    private int getCursor(int startRank){
        startRank+= 49;
        validWantedCount(startRank);
        return startRank / PER_PAGE_SIZE;
    }

    @Override
    public Long getTotalSellingCount(Long productId){
        return getCounts(kreamRequestCreator.createRequestForProductDetail(productId, "asks", KreamParameters.builder().build()));
    }

    @Override
    public Long getTotalBuyingCount(Long productId){
        return getCounts(kreamRequestCreator.createRequestForProductDetail(productId, "bids", KreamParameters.builder().build()));
    }

    @Override
    public KreamPage<KreamTradeItem> getKreamTradeItemPageByProdcutId(Long productId, Integer cursor){
        HttpRequest request = kreamRequestCreator.createRequestForProductDetail(productId, "sales", KreamParameters.builder().cursor(cursor).sort("date_created[desc]").build());
        String responseJson = httpExcuter.executeHttp(request);
        KreamPage<KreamTradeItem> result = getKreamTradeItemPage(responseJson);
        log.info("page: {}", result);
        return result;
    }

    @Override
    public String login() {
        HttpRequest requestForLogin = kreamRequestCreator.createRequestForLogin();
        String responseJson = httpExcuter.executeHttp(requestForLogin);
        log.info("login responseJson: {}", responseJson);
        return getBearerToken(responseJson);
    }

    private String getBearerToken(String json){
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode accessToken = root.get("access_token");
            return accessToken.asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<KreamItemDto> getPopularItems(int cursor){
        String responseJson = httpExcuter.executeHttp(kreamRequestCreator.createRequestForGettingItemList(String.valueOf(cursor)));
        return toKreamItemDtos(responseJson);
    }

    private List<KreamItemDto> toKreamItemDtos(String json) {
        List<KreamItemDto> result = new ArrayList<>();
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode items = root.get("items");
            Iterator<JsonNode> elements = items.elements();
            while(elements.hasNext()){
                JsonNode next = elements.next();
                log.info("item");
                JsonNode product = next.get("product");
                if(product != null){
                    JsonNode release = product.get("release");
                    KreamItemDto kreamItemDto = mapper.treeToValue(release, KreamItemDto.class);
                    result.add(kreamItemDto);
                    log.info(kreamItemDto);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private KreamPage<KreamTradeItem> getKreamTradeItemPage(String json){
        KreamPage<KreamTradeItem> result = new KreamPage<>();
        try {
            JsonNode root = mapper.readTree(json);
            result.setCursor(root.get("cursor").asInt());
            result.setPerPage(root.get("per_page").asInt());
            result.setNextCursor(root.get("next_cursor").asInt());
            result.setPrevCursor(root.get("prev_cursor").asInt());
            result.setTotal(root.get("total").asLong());
            result.setItems(toKreamTradeItemDtos(root.get("items")));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    private List<KreamTradeItem> toKreamTradeItemDtos(JsonNode items) {
        List<KreamTradeItem> result = new ArrayList<>();
        try {
            Iterator<JsonNode> elements = items.elements();
            while(elements.hasNext()){
                JsonNode item = elements.next();
                if(item != null){
                    KreamTradeItem kreamItemDto = mapper.treeToValue(item, KreamTradeItem.class);
                    result.add(kreamItemDto);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    private Long getCounts(HttpRequest request){
        String responseJson = httpExcuter.executeHttp(request);
        return jsonToCounts(responseJson);
    }

    private Long jsonToCounts(String json){
        Long result;
        try {
            JsonNode root = mapper.readTree(json);
            result = root.get("total").asLong();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private void validWantedCount(int wantedCount){
        if(wantedCount % PER_PAGE_SIZE == 0) return;
        throw new IllegalArgumentException("50 단위로 아이템 갯수를 선택해주세요");
    }
}
