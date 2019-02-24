package com.daledev.holidaypricescrapper.scraper;

import com.daledev.holidaypricescrapper.dao.firstchoice.FirstChoicePriceScraper;
import com.daledev.holidaypricescrapper.domain.HolidayQuote;
import com.daledev.holidaypricescrapper.domain.HolidayQuoteResults;
import com.daledev.holidaypricescrapper.util.TestDataReader;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author dale.ellis
 * @since 28/12/2018
 */
public class FirstChoiceHolidayQuoteScraperTest {
    private FirstChoicePriceScraper priceScraper = new FirstChoicePriceScraper();

    @Test
    public void extractForPricesFormContent() {
        // Given
        String html = TestDataReader.getFirstChoicePricesHtml();

        // When
        HolidayQuoteResults results = priceScraper.extract(html);

        // Then
        assertEquals(4, results.getResults().size());
    }

    @Test
    public void extractForNoPriceFormContent() {
        // Given
        String html = TestDataReader.getFirstChoiceNoPriceHtml();

        // When
        HolidayQuoteResults results = priceScraper.extract(html);

        // Then
        assertEquals(0, results.getResults().size());
    }
}