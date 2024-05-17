package org.example.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TradeInfo {
    int count1dayAgo = 0;
    int count2dayAgo = 0;
    //가격같은 경우는 mod를 이용해야 할 듯
    long count1weekAgo = 0;
    long count2weekAgo = 0;
    double totalPrice1dayAgo = 0;
    double totalPrice2dayAgo = 0;
    double totalPrice1weekAgo = 0;
    double totalPrice2weekAgo = 0;
}
