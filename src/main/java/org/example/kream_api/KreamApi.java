package org.example.kream_api;

import org.example.kream_api.dto.KreamItemDto;

import java.util.List;

public interface KreamApi {
    List<KreamItemDto> getItems(int wantedCount);
}
