package com.daledev.holidaypricescrapper.service;

import com.daledev.holidaypricescrapper.domain.PriceHistory;

/**
 * @author dale.ellis
 * @since 13/01/2019
 */
public interface PriceHistoryChartService {

    /**
     * @param priceHistory
     * @return
     */
    byte[] getLineChart(PriceHistory priceHistory);
}
