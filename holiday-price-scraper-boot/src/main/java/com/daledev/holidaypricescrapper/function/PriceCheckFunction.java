package com.daledev.holidaypricescrapper.function;

import com.daledev.holidaypricescrapper.domain.PriceCheckResult;
import com.daledev.holidaypricescrapper.service.PriceRetrieverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author dale.ellis
 * @since 03/01/2019
 */
@Component
public class PriceCheckFunction implements Function<String, List<PriceCheckResult>> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private PriceRetrieverService priceRetrieverService;

    /**
     * @param priceRetrieverService
     */
    public PriceCheckFunction(PriceRetrieverService priceRetrieverService) {
        this.priceRetrieverService = priceRetrieverService;
    }

    @Override
    public List<PriceCheckResult> apply(String searchId) {
        try {
            return priceRetrieverService.priceChecks();
        } catch (IOException e) {
            log.error("Function failed to check price", e);
            return new ArrayList<>();
        }
    }
}
