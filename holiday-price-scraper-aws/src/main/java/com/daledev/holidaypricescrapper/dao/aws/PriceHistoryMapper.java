package com.daledev.holidaypricescrapper.dao.aws;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.daledev.holidaypricescrapper.domain.PriceHistory;
import com.daledev.holidaypricescrapper.util.PriceHistoryJsonMapper;

/**
 * @author dale.ellis
 * @since 04/01/2019
 */
class PriceHistoryMapper {
    static final String TABLE_NAME = "PriceHistory";
    static final String PRIMARY_KEY_NAME = "searchUUID";
    static final String HISTORY_ATTRIBUTE_NAME = "history";

    private PriceHistoryMapper() {
    }

    /**
     *
     * @param persistedHistory
     * @return
     */
    static PriceHistory mapToPriceHistory(Item persistedHistory) {
        String json = persistedHistory.toJSON();
        return PriceHistoryJsonMapper.fromJson(json);
    }

    /**
     *
     * @param persistedHistory
     * @return
     */
    static Item mapFromPriceHistory(PriceHistory persistedHistory) {
        String historyAsJson = PriceHistoryJsonMapper.toJson(persistedHistory);
        return Item.fromJSON(historyAsJson);
    }
}
