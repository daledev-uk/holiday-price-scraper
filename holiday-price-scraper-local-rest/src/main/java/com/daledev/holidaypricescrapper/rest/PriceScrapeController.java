package com.daledev.holidaypricescrapper.rest;

import com.daledev.holidaypricescrapper.domain.PriceCheckResult;
import com.daledev.holidaypricescrapper.service.PriceRetrieverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author dale.ellis
 * @since 28/12/2018
 */
@RestController
public class PriceScrapeController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private PriceRetrieverService priceRetrieverService;

    public PriceScrapeController(PriceRetrieverService priceRetrieverService) {
        this.priceRetrieverService = priceRetrieverService;
    }

    /**
     * @return
     */
    @GetMapping(path = "/check")
    public List<PriceCheckResult> priceCheck() throws IOException {
        log.debug("Request received to check prices");
        return priceRetrieverService.priceChecks();
    }

}
