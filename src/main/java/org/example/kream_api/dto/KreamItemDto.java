package org.example.kream_api.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class KreamItemDto {
    Long id;
    String name;
    String url;
    String translated_name;
    String style_code;
    String original_price;
    String date_released;
    Long dDaySellingCount;
    Long dDayBuyingCount;
    int tradeCount1dayAgo;
    int tradeCount2dayAgo;
    long tradeCount1weekAgo;
    long tradeCount2weekAgo;
}
