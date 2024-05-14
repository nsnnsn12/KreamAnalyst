package org.example.kream_api;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.poi.util.StringUtil;
import org.example.kream_api.dto.KreamParameters;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

@RequiredArgsConstructor
public class KreamRequestCreator {
    private final String ROOT_API_URI = "https://api.kream.co.kr/api/";
    private final String ALL_ITEM_URI = "p/tabs/all/";
    private final String PRODUCT_URI = "p/products/";

    private final String bearerToken;

    public HttpRequest createRequestForGettingItemList(String cursor){
        try {
            URI uri = new URIBuilder(String.format("%s%s", ROOT_API_URI, ALL_ITEM_URI))
                    .addParameter("sort", "popular_score")
                    .addParameter("cursor", cursor).build();
            return createGetRequestForKreamApi(uri);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("잘못된 url 입니다.");
        }
    }

    public HttpRequest createRequestForProductDetail(Long id, String type, KreamParameters parameters){
        try {
            URIBuilder uriBuilder = new URIBuilder(String.format("%s%s%s/%s", ROOT_API_URI, PRODUCT_URI, id.toString(), type))
                    .addParameter("per_page", parameters.getPerPage().toString())
                    .addParameter("sort", "50")
                    .addParameter("cursor", parameters.getCursor().toString());
            if(StringUtil.isNotBlank(parameters.getSort())){
                uriBuilder.addParameter("sort", parameters.getSort());
            }
            return createGetRequestForKreamApi(uriBuilder.build());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("잘못된 url 입니다.");
        }
    }

    private HttpRequest createGetRequestForKreamApi(URI uri){
        return HttpRequest
                .newBuilder()
                .uri(uri)
                .header("X-Kream-Web-Request-Secret", "kream-djscjsghdkd")
                .header("X-Kream-Device-Id", "web;f819c914-93a8-4148-a423-e52763fe173e")
                .header("X-Kream-Api-Version", "31")
                .header("X-Kream-Web-Build-Version", "5.5.1")
                .header("X-Kream-Client-Datetime", "20240514094912+0900")
                .header("Authorization", String.format("Bearer %s", bearerToken))
                .GET()
                .build();
    }
}
