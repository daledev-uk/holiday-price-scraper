package com.daledev.holidaypricescrapper.util;

import com.daledev.holidaypricescrapper.domain.PriceHistory;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author dale.ellis
 * @since 06/01/2019
 */
public class PriceHistoryJsonMapperTest {

    @Test
    @Ignore
    public void fromJson() {
        //Given
        String json = "{\"searchUUID\": \"53e5dbf8-00b5-4637-9a7c-be82f350d71f\",\"history\": \"[{\\\"firstRetrievedDate\\\":1546610555403,\\\"lastRetrievedDate\\\":1546610909664,\\\"cheapestQuotes\\\":[{\\\"airport\\\":{\\\"code\\\":\\\"STN\\\",\\\"uniqueId\\\":null},\\\"date\\\":1557273600000,\\\"price\\\":613.0}]}]\"}";

        // When
        PriceHistory priceHistory = PriceHistoryJsonMapper.fromJson(json);

        // Then
        assertNotNull(priceHistory);

    }
}