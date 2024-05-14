package org.example.kream_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.http.HttpExcuter;
import org.example.kream_api.dto.KreamParameters;
import org.example.kream_api.dto.KreamTradeInfo;
import java.net.http.HttpRequest;
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
        Long result = 0L;
        HttpRequest request = kreamRequestCreator.createRequestForProductDetail(productId, "sales", KreamParameters.builder().cursor(1).sort("date_created[desc]").build());
        String responseJson = httpExcuter.executeHttp(request);
        log.info("KreamTradeInfoMaker");
        log.info(responseJson);
        toKreamTradeInfos(responseJson);
        return result;
    }

    private List<KreamTradeInfo> toKreamTradeInfos(String json) {
        List<KreamTradeInfo> result = new ArrayList<>();
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode items = root.get("items");
            Iterator<JsonNode> elements = items.elements();
            while(elements.hasNext()){
                JsonNode item = elements.next();
                KreamTradeInfo kreamItemDto = mapper.treeToValue(item, KreamTradeInfo.class);
                log.info(kreamItemDto);
                result.add(kreamItemDto);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
