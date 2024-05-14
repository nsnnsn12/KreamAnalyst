package org.example.kream_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class KreamTradePage {
    private CursorInfo cursorInfo;
    private List<KreamTradeItem> kreamTradeItems;
}
