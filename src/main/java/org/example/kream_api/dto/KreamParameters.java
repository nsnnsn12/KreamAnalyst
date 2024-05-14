package org.example.kream_api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KreamParameters {
    @Builder.Default
    private Integer perPage = 50;
    private String sort;
    @Builder.Default
    private Integer cursor = 1;
}
