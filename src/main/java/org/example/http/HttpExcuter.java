package org.example.http;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Log4j2
public class HttpExcuter {
    private final HttpClient httpClient;

    public HttpExcuter() {
        httpClient = HttpClient.newHttpClient();
    }
    public String executeHttp(HttpRequest request){
        try {
            HttpResponse<String> send = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("status: {}", send.statusCode());
            if(send.statusCode() >= 400) {
                log.info("error body: {}", send.body());
                throw new IllegalArgumentException("request error");
            }
            return send.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
