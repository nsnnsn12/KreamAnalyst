package org.example.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.kream_api.KreamRequestCreator;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpExcuter {
    private final HttpClient httpClient;

    public HttpExcuter() {
        httpClient = HttpClient.newHttpClient();
    }
    public String executeHttp(HttpRequest request){
        try {
            HttpResponse<String> send = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(send.statusCode() >= 400) throw new IllegalArgumentException("request error");
            return send.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
