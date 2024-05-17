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
    String translated_name;
    String style_code;
    String original_price;
    String date_released;
}
