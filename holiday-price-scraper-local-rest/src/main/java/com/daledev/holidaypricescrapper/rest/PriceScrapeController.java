package com.daledev.holidaypricescrapper.rest;

import com.daledev.holidaypricescrapper.rest.dto.HolidayQuoteDto;
import com.daledev.holidaypricescrapper.rest.dto.PriceSnapshotDto;
import com.daledev.holidaypricescrapper.service.MappingService;
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

    private MappingService mappingService;
    private PriceRetrieverService priceRetrieverService;

    public PriceScrapeController(MappingService mappingService, PriceRetrieverService priceRetrieverService) {
        this.mappingService = mappingService;
        this.priceRetrieverService = priceRetrieverService;
    }

    /**
     * @return
     */
    @GetMapping(path = "/prices")
    public List<HolidayQuoteDto> getPrices() {
        log.debug("Request received to get prices");
        return mappingService.map(priceRetrieverService.getPrices(), HolidayQuoteDto.class);
    }

    /**
     * @return
     */
    @GetMapping(path = "/cheapest")
    public PriceSnapshotDto getCheapest() {
        log.debug("Request received to get cheapest price");
        return mappingService.map(priceRetrieverService.getCurrentCheapestPrice(), PriceSnapshotDto.class);
    }

    /**
     * @return
     */
    @GetMapping(path = "/check")
    public String priceCheck() {
        log.debug("Request received to check price");
        try {
            return priceRetrieverService.priceCheck().name();
        } catch (IOException e) {
            log.error("Failed to check prices", e);
            return "FAILED";
        }
    }

}
