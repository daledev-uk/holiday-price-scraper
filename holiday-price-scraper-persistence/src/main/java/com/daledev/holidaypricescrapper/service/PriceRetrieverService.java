package com.daledev.holidaypricescrapper.service;

import com.daledev.holidaypricescrapper.domain.PriceCheckResult;

import java.io.IOException;
import java.util.List;

/**
 * @author dale.ellis
 * @since 28/12/2018
 */
public interface PriceRetrieverService {

    /**
     *
     */
    List<PriceCheckResult> priceChecks() throws IOException;
}
