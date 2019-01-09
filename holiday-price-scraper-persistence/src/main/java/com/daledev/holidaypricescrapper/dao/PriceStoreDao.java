package com.daledev.holidaypricescrapper.dao;

import com.daledev.holidaypricescrapper.domain.PriceHistory;

import java.io.IOException;
import java.util.List;

/**
 * @author dale.ellis
 * @since 30/12/2018
 */
public interface PriceStoreDao {
    /**
     * @return
     * @throws IOException
     */
    List<PriceHistory> getPriceHistories() throws IOException;

    /**
     * @param searchUuid
     * @return
     */
    PriceHistory getPriceHistory(String searchUuid) throws IOException;

    /**
     * @param priceHistory
     */
    void storePrice(PriceHistory priceHistory) throws IOException;
}
