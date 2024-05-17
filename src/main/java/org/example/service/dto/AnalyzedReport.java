package org.example.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class AnalyzedReport {
    Long productId;
    String url;
    String productName;
    String modelNo;
    String originalPrice;
    String dateReleased;
    Long totalSellingCount;
    Long totalBuyingCount;
    int tradeCount1dayAgo;
    int tradeCount2dayAgo;
    long tradeCount1weekAgo;
    long tradeCount2weekAgo;
}
