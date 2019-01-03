package com.daledev.holidaypricescrapper.dao;

import com.daledev.holidaypricescrapper.domain.PriceHistory;

import java.io.IOException;

/**
 * @author dale.ellis
 * @since 30/12/2018
 */
public interface PriceStoreDao {
    /**
     * @return
     */
    PriceHistory getPriceHistory() throws IOException;

    /**
     * @param priceHistory
     */
    void storePrice(PriceHistory priceHistory) throws IOException;
}
