package org.example.kream_api;

import org.example.kream_api.dto.KreamItemDto;
import org.example.kream_api.dto.KreamPage;
import org.example.kream_api.dto.KreamTradeItem;

import java.util.List;

/**
 * kream api와 연동하는 부분은 모두 여기에서 처리할 수 있도록
 */
public interface KreamApi {
    List<KreamItemDto> getItems(int wantedCount, int startRank);

    String login();

    Long getTotalSellingCount(Long productId);

    Long getTotalBuyingCount(Long productId);

    KreamPage<KreamTradeItem> getKreamTradeItemPageByProdcutId(Long productId, Integer cursor);
}
