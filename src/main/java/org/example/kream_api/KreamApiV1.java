package org.example.kream_api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.net.URIBuilder;
import org.example.kream_api.dto.KreamItemDto;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
public class KreamApiV1 implements KreamApi{
    private final int PER_PAGE_SIZE = 50;
    private final String ROOT = "https://api.kream.co.kr/api/";
    private final String GETTING_ITEM = "p/tabs/all/";
    @Override
    public List<KreamItemDto> getItems(int wantedCount) {
        log.info("wantedCount: {}", wantedCount);
        validWantedCount(wantedCount);

        log.info("body");
        log.info(getPopularItems());
        return null;
    }

    private String getPopularItems(){
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest itemRequest = getPopularItemRequest("1");
        log.info("uri: {}", itemRequest.uri());
        log.info("headers: {}", itemRequest.headers());
        httpClient.sendAsync(itemRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(s -> log.info("body: {}", s))
                .join();
        return "";
    }

    private HttpRequest getPopularItemRequest(String cursor){
        try {
            URI uri = new URIBuilder(String.format("%s%s", ROOT, GETTING_ITEM))
                    .addParameter("sort", "popular_score")
                    .addParameter("cursor", cursor).build();
            log.info("build: {}", uri);
            return HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .headers("X-Kream-Web-Request-Secret", "kream-djscjsghdkd")
                    .headers("X-Kream-Device-Id", "web;f819c914-93a8-4148-a423-e52763fe173e")
                    .headers("X-Kream-Api-Version", "31")
                    .headers("X-Kream-Web-Build-Version", "5.5.1")
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("잘못된 url 입니다.");
        }
    }

    private void validWantedCount(int wantedCount){
        if(wantedCount % PER_PAGE_SIZE == 0) return;
        throw new IllegalArgumentException("50 단위로 아이템 갯수를 선택해주세요");
    }
}
