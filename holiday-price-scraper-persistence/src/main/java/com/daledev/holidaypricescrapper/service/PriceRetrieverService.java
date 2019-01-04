package com.daledev.holidaypricescrapper.service;

import com.daledev.holidaypricescrapper.constants.PriceStatus;
import com.daledev.holidaypricescrapper.domain.HolidayQuote;
import com.daledev.holidaypricescrapper.domain.PriceSnapshot;

import java.io.IOException;
import java.util.List;

/**
 * @author dale.ellis
 * @since 28/12/2018
 */
public interface PriceRetrieverService {

    /**
     * @return
     */
    List<HolidayQuote> getPrices();

    /**
     * @return
     */
    PriceSnapshot getCurrentCheapestPrice();

    /**
     *
     */
    PriceStatus priceCheck() throws IOException;
}
