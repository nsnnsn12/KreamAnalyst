package org.example.kream_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.example.kream_api.dto.EndCursorInfo;
import org.example.kream_api.dto.KreamItemDto;
import org.example.kream_api.dto.KreamParameters;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Log4j2
public class KreamApiV1 implements KreamApi{
    private final int PER_PAGE_SIZE = 50;

    private final ObjectMapper mapper;
    private final KreamRequestCreator kreamRequestCreator;
    private final HttpClient httpClient;

    public KreamApiV1(String bearerToken) {
        httpClient = HttpClient.newHttpClient();
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        kreamRequestCreator = new KreamRequestCreator(bearerToken);
    }


    @Override
    public List<KreamItemDto> getItems(int wantedCount) {
        List<KreamItemDto> result = new ArrayList<>();
        log.info("wantedCount: {}", wantedCount);
        validWantedCount(wantedCount);
        int count = wantedCount / PER_PAGE_SIZE;
        for(int i = 0; i < count; i++){
            result.addAll(getPopularItems(i+1));
        }
        return result;
    }

    private List<KreamItemDto> getPopularItems(int count){
        List<KreamItemDto> result = new ArrayList<>();
        for(int i = 0; i < count; i++){
            String cursor = String.valueOf(i+1);
            String responseJson = executeHttp(kreamRequestCreator.createRequestForGettingItemList(cursor));
            result.addAll(toKreamItemDtos(responseJson));
        }

        return result;
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
                    kreamItemDto.setUrl(getProductURI(kreamItemDto.getId()));
                    kreamItemDto.setDDaySellingCount(getSellingCount(kreamItemDto));
                    kreamItemDto.setDDayBuyingCount(getBuyingCount(kreamItemDto));
                    result.add(kreamItemDto);
                    log.info(kreamItemDto);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private String getProductURI(Long productId){
        return String.format("%s%s", "https://kream.co.kr/products/", productId);
    }

    private Long getSellingCount(KreamItemDto kreamItemDto){
        return getCounts(kreamRequestCreator.createRequestForProductDetail(kreamItemDto.getId(), "asks", KreamParameters.builder().build()));
    }

    private Long getBuyingCount(KreamItemDto kreamItemDto){
        return getCounts(kreamRequestCreator.createRequestForProductDetail(kreamItemDto.getId(), "bids", KreamParameters.builder().build()));
    }


    private Long getCounts(HttpRequest request){
        String responseJson = executeHttp(request);
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

    private String executeHttp(HttpRequest request){
        try {
            HttpResponse<String> send = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(send.statusCode() >= 400) throw new IllegalArgumentException("request error");
            return send.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void validWantedCount(int wantedCount){
        if(wantedCount % PER_PAGE_SIZE == 0) return;
        throw new IllegalArgumentException("50 단위로 아이템 갯수를 선택해주세요");
    }
}
