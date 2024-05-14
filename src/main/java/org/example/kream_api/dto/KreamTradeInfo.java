package org.example.kream_api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class KreamTradeInfo {
    private Long product_id;
    private String date_created;
}
