package com.daledev.holidaypricescrapper.dao.aws;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.daledev.holidaypricescrapper.domain.Airport;
import com.daledev.holidaypricescrapper.domain.HolidayQuote;
import com.daledev.holidaypricescrapper.domain.PriceHistory;
import com.daledev.holidaypricescrapper.domain.PriceSnapshot;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author dale.ellis
 * @since 04/01/2019
 */
@RunWith(Enclosed.class)
@Ignore
public class PriceHistoryMapperTest {

    public static class MapToPriceHistory {
        @Test
        public void mapToPriceHistory() {
            // Given
            PriceHistory history = new PriceHistory();
            history.setSearchUUID("test");
            history.addSnapshot(createPriceSnapshotWithQuote(new Date(100), new Date(100000), 250.99d, "LGW"));
            Item item = PriceHistoryMapper.mapFromPriceHistory(history);

            // When
            PriceHistory mappedHistory = PriceHistoryMapper.mapToPriceHistory(item);

            // Then
            assertEquals(history.getSearchUUID(), mappedHistory.getSearchUUID());
        }
    }

    public static class MapFromPriceHistory {
        @Test
        public void givenNoHistory() {
            // Given
            PriceHistory history = new PriceHistory();
            history.setSearchUUID("test");

            // When
            Item item = PriceHistoryMapper.mapFromPriceHistory(history);

            // Then
            assertEquals("{\"searchUUID\":\"test\",\"history\":[]}", item.toJSON());
        }

        @Test
        public void given1History() {
            // Given
            PriceHistory history = new PriceHistory();
            history.setSearchUUID("test");
            history.addSnapshot(createPriceSnapshot(new Date(100)));

            // When
            Item item = PriceHistoryMapper.mapFromPriceHistory(history);

            // Then
            assertEquals("{\"searchUUID\":\"test\",\"history\":[{\"firstRetrievedDate\":100,\"lastRetrievedDate\":100,\"cheapestQuotes\":[]}]}", item.toJSON());
        }

        @Test
        public void given2History() {
            // Given
            PriceHistory history = new PriceHistory();
            history.setSearchUUID("test");
            history.addSnapshot(createPriceSnapshot(new Date(100)));
            history.addSnapshot(createPriceSnapshot(new Date(255)));

            // When
            Item item = PriceHistoryMapper.mapFromPriceHistory(history);

            // Then
            assertEquals("{\"searchUUID\":\"test\",\"history\":[{\"firstRetrievedDate\":100,\"lastRetrievedDate\":100,\"cheapestQuotes\":[]},{\"firstRetrievedDate\":255,\"lastRetrievedDate\":255,\"cheapestQuotes\":[]}]}", item.toJSON());
        }

        @Test
        public void given1HistoryWithPrice() {
            // Given
            PriceHistory history = new PriceHistory();
            history.setSearchUUID("test");
            history.addSnapshot(createPriceSnapshotWithQuote(new Date(100), new Date(100000), 250.99d, "LGW"));

            // When
            Item item = PriceHistoryMapper.mapFromPriceHistory(history);

            // Then
            assertEquals("{\"searchUUID\":\"test\",\"history\":[{\"firstRetrievedDate\":100,\"lastRetrievedDate\":100,\"cheapestQuotes\":[{\"airport\":{\"code\":\"LGW\",\"uniqueId\":null},\"date\":100000,\"price\":250.99}]}]}", item.toJSON());
        }
    }

    private static PriceSnapshot createPriceSnapshot(Date aDate) {
        PriceSnapshot priceSnapshot = new PriceSnapshot();
        priceSnapshot.setFirstRetrievedDate(aDate);
        priceSnapshot.setLastRetrievedDate(aDate);
        return priceSnapshot;
    }

    private static PriceSnapshot createPriceSnapshotWithQuote(Date aDate, Date holidayDate, double price, String airportCode) {
        PriceSnapshot priceSnapshot = createPriceSnapshot(aDate);
        priceSnapshot.getCheapestQuotes().add(new HolidayQuote(holidayDate, price, new Airport(airportCode)));
        return priceSnapshot;
    }
}