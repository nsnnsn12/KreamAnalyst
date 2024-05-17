package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.kream_api.KreamApi;
import org.example.kream_api.dto.KreamItemDto;
import org.example.service.dto.AnalyzedReport;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class KreamAnalyst {
    private final KreamApi kreamApi;

    public List<AnalyzedReport> analyze(int wantedCount){
        List<KreamItemDto> items = kreamApi.getItems(wantedCount);
        log.info(items);
        List<AnalyzedReport> result = fromKreamItemDtos(items);
        log.info(result);
        return result;
    }

    private List<AnalyzedReport> fromKreamItemDtos(List<KreamItemDto> items){
        List<AnalyzedReport> result = new ArrayList<>();
        for (KreamItemDto item : items) {
            AnalyzedReport analyzedReport = AnalyzedReport.builder().productId(item.getId())
                    .productName(item.getName())
                    .url(getProductURI(item.getId()))
                    .modelNo(item.getStyle_code())
                    .originalPrice(item.getOriginal_price())
                    .dateReleased(item.getDate_released())
                    .totalBuyingCount(kreamApi.getTotalBuyingCount(item.getId()))
                    .totalSellingCount(kreamApi.getTotalSellingCount(item.getId()))
                    .build();

            result.add(analyzedReport);
        }
        return result;
    }

    private String getProductURI(Long productId){
        return String.format("%s%s", "https://kream.co.kr/products/", productId);
    }
}
