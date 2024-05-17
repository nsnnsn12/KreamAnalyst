package org.example.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.kream_api.KreamApi;

@Getter
@Log4j2
@RequiredArgsConstructor
public class User {
    private final String email;
    private final String password;
    private int callCount;
    private String bearerToken;

    public void login(KreamApi kreamApi){
        bearerToken = kreamApi.login();
        log.info("setting bearerToken: {}", bearerToken);
    }
}
