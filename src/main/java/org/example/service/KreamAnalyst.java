package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.kream_api.KreamApi;
import org.example.kream_api.dto.*;
import org.example.service.dto.AnalyzedReport;
import org.example.service.dto.TradeInfo;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class KreamAnalyst {
    private final KreamApi kreamApi;

    private final LocalDate NOW = LocalDate.now();
    private final LocalDate ONE_DAY_AGO = NOW.minusDays(1);
    private final LocalDate TWO_DAYS_AGO = NOW.minusDays(2);
    private final LocalDate ONE_WEEK_AGO = NOW.minusDays(7);
    private final LocalDate TWO_WEEKS_AGO = NOW.minusDays(14);

    public List<AnalyzedReport> analyze(int wantedCount, int startRank){
        List<KreamItemDto> items = kreamApi.getItems(wantedCount, startRank);
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
            TradeInfo tradeInfo = getTradeInfo(item.getId());
            analyzedReport.setTradeCount1dayAgo(tradeInfo.getCount1dayAgo());
            analyzedReport.setTradeCount2dayAgo(tradeInfo.getCount2dayAgo());
            analyzedReport.setTradeCount1weekAgo(tradeInfo.getCount1weekAgo());
            analyzedReport.setTradeCount2weekAgo(tradeInfo.getCount2weekAgo());
            analyzedReport.setTradeAvgPrice1dayAgo(getAverage(tradeInfo.getTotalPrice1dayAgo(), tradeInfo.getCount1dayAgo()));
            analyzedReport.setTradeAvgPrice2dayAgo(getAverage(tradeInfo.getTotalPrice2dayAgo(), tradeInfo.getCount2dayAgo()));
            analyzedReport.setTradeAvgPrice1weekAgo(getAverage(tradeInfo.getTotalPrice1weekAgo(), tradeInfo.getCount1weekAgo()));
            analyzedReport.setTradeAvgPrice2weekAgo(getAverage(tradeInfo.getTotalPrice2weekAgo(), tradeInfo.getCount2weekAgo()));
            result.add(analyzedReport);
        }
        return result;
    }

    private double getAverage(double totalPrice, long totalCount){
        if(totalCount == 0) return 0;
        return totalPrice / totalCount;
    }

    private TradeInfo getTradeInfo(Long productId){
        TradeInfo tradeInfo = new TradeInfo();
        setTradeInfo(tradeInfo, productId, 1);
        return tradeInfo;
    }

    public void setTradeInfo(TradeInfo tradeInfo, Long productId, int cursor){
        KreamPage<KreamTradeItem> page = kreamApi.getKreamTradeItemPageByProdcutId(productId, cursor);

        List<KreamTradeItem> items = page.getItems();
        for (KreamTradeItem item : items) {
            LocalDate selected = getDate(item.getDate_created());
            if(selected.isEqual(NOW) || selected.isBefore(TWO_WEEKS_AGO)) continue;
            if(selected.isEqual(ONE_DAY_AGO)){
                tradeInfo.setCount1dayAgo(tradeInfo.getCount1dayAgo() + 1);
                tradeInfo.setTotalPrice1dayAgo(tradeInfo.getTotalPrice1dayAgo() + item.getPrice());
            }

            if(selected.isEqual(TWO_DAYS_AGO)){
                tradeInfo.setCount2dayAgo(tradeInfo.getCount2dayAgo() + 1);
                tradeInfo.setTotalPrice2dayAgo(tradeInfo.getTotalPrice2dayAgo() + item.getPrice());
            }

            if(selected.isEqual(ONE_WEEK_AGO) || selected.isAfter(ONE_WEEK_AGO)){
                tradeInfo.setCount1weekAgo(tradeInfo.getCount1weekAgo() + 1);
                tradeInfo.setTotalPrice1weekAgo(tradeInfo.getTotalPrice1weekAgo() + item.getPrice());
            }else{
                tradeInfo.setCount2weekAgo(tradeInfo.getCount2weekAgo() + 1);
                tradeInfo.setTotalPrice2weekAgo(tradeInfo.getTotalPrice2weekAgo()+ item.getPrice());
            }
        }

        if(canMoveOnNextPage(page)) setTradeInfo(tradeInfo, productId, page.getNextCursor());
    }

    private boolean canMoveOnNextPage(KreamPage<KreamTradeItem> page){
        KreamTradeItem lastItem = page.getItems().get(page.getItems().size()-1);
        LocalDate lastDate = getDate(lastItem.getDate_created());

        if(page.getNextCursor() == 0 || lastDate.isAfter(TWO_WEEKS_AGO)) return false;
        return true;
    }

    private LocalDate getDate(String date_created){
        return Instant.parse(date_created).atOffset(ZoneOffset.UTC).toLocalDate();
    }

    private String getProductURI(Long productId){
        return String.format("%s%s", "https://kream.co.kr/products/", productId);
    }
}
