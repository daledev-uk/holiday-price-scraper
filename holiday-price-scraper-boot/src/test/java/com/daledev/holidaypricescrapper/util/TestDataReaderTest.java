package com.daledev.holidaypricescrapper.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author dale.ellis
 * @since 10/01/2019
 */
public class TestDataReaderTest {

    @Test
    public void getFirstChoicePricesHtml() {
        // When
        String html = TestDataReader.getFirstChoicePricesHtml();

        // Then
        assertNotNull(html);
    }

    @Test
    public void getFirstChoiceNoPriceHtml() {
        // When
        String html = TestDataReader.getFirstChoiceNoPriceHtml();

        // Then
        assertNotNull(html);
    }
}