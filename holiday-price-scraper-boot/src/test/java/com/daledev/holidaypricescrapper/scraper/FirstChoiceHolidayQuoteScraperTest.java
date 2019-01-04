package com.daledev.holidaypricescrapper.scraper;

import com.daledev.holidaypricescrapper.dao.firstchoice.FirstChoicePriceScraper;
import com.daledev.holidaypricescrapper.domain.HolidayQuote;
import org.junit.Test;

import java.util.List;

/**
 * @author dale.ellis
 * @since 28/12/2018
 */
public class FirstChoiceHolidayQuoteScraperTest {
    private FirstChoicePriceScraper priceScraper = new FirstChoicePriceScraper();

    @Test
    public void extract() {
        // Given
        String html = "";

        // When
        //List<HolidayQuote> results = priceScraper.extract(html);

        // Then

    }
}