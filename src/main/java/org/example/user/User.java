package org.example.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class User {
    private final String email;
    private final String password;
    private int callCount;
    private String bearerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmcmVzaCI6dHJ1ZSwiaWF0IjoxNzE1OTA5MjI5LCJqdGkiOiJkOGU4YWFlOS0zODhhLTQ1MjYtYjRmZi02ZDA3NzZkMzIxYmIiLCJ0eXBlIjoiYWNjZXNzIiwiaWRlbnRpdHkiOjczODcwMzQsIm5iZiI6MTcxNTkwOTIyOSwiY3NyZiI6IjZmODMxMGM2LThmYTUtNGJmMC05NTM1LTA2NzMzMTgyY2MxNCIsImV4cCI6MTcxNTkxNjQyOSwidWMiOnsic2FmZSI6dHJ1ZX19.T4N10JtHCghX9_ffWtkLMJhXT1z-klzW0cIIq8xlb6g";

    public String login(){
        return this.bearerToken;
    }
}
