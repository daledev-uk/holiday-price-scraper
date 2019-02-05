package com.daledev.holidaypricescrapper.service;

import com.daledev.holidaypricescrapper.domain.PriceHistory;

/**
 * @author dale.ellis
 * @since 27/01/2019
 */
public interface TemplateService {

    /**
     *
     * @param priceHistory
     * @return
     */
    String getPriceIncreaseContent(PriceHistory priceHistory);

    /**
     *
     * @param priceHistory
     * @return
     */
    String getPriceDroppedContent(PriceHistory priceHistory);
}
