package org.example.kream_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.poi.util.StringUtil;
import org.example.kream_api.dto.KreamParameters;
import org.example.user.User;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class KreamRequestCreator {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String ROOT_API_URI = "https://api.kream.co.kr/api/";
    private final String ALL_ITEM_URI = "p/tabs/all/";
    private final String PRODUCT_URI = "p/products/";

    private final String LOGIN = "auth/login/";

    private final User user;

    private final String[] DEFAULT_HEADERS = {"X-Kream-Web-Request-Secret", "kream-djscjsghdkd"
            ,"X-Kream-Device-Id", "web;f819c914-93a8-4148-a423-e52763fe173e"
            ,"X-Kream-Api-Version", "31"
            ,"X-Kream-Web-Build-Version", "5.5.1"
            ,"X-Kream-Client-Datetime", "20240514094912+0900"};

    private final String AUTHORIZATION_HEADER = "Authorization";

    public HttpRequest createRequestForGettingItemList(String cursor){
        try {
            URI uri = new URIBuilder(String.format("%s%s", ROOT_API_URI, ALL_ITEM_URI))
                    .addParameter("sort", "popular_score")
                    .addParameter("cursor", cursor).build();

            String[] headers = Stream.of(DEFAULT_HEADERS, getAuthorizationHeader()).flatMap(Stream::of).toArray(String[]::new);
            return createGetRequestForKreamApi(uri, headers);
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
            String[] headers = Stream.of(DEFAULT_HEADERS, getAuthorizationHeader()).flatMap(Stream::of).toArray(String[]::new);
            return createGetRequestForKreamApi(uriBuilder.build(), headers);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("잘못된 url 입니다.");
        }
    }

    public HttpRequest createRequestForLogin(){
        try {
            URI uri = new URIBuilder(String.format("%s%s", ROOT_API_URI, LOGIN)).build();
            String[] headers = Stream.of(DEFAULT_HEADERS, new String[]{"Content-Type", "application/json"}).flatMap(Stream::of).toArray(String[]::new);

            Map<String, String> loginInfo = new HashMap<>();
            loginInfo.put("email", user.getEmail());
            loginInfo.put("password", user.getPassword());

            String requestBody = jsonBody(loginInfo);
            return createPostRequestForKreamApi(uri, headers, requestBody);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("잘못된 url 입니다.");
        }
    }

    private String jsonBody(Map<String, String> map){
        try {
            return objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private String[] getAuthorizationHeader(){
        return new String[] {AUTHORIZATION_HEADER, String.format("Bearer %s", user.getBearerToken())};
    }

    private HttpRequest createGetRequestForKreamApi(URI uri, String[] headers){
        return HttpRequest
                .newBuilder()
                .uri(uri)
                .headers(headers)
                .GET()
                .build();
    }

    private HttpRequest createPostRequestForKreamApi(URI uri, String[] headers, String requestBody){
        return HttpRequest
                .newBuilder()
                .uri(uri)
                .headers(headers)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }
}
