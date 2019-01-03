package com.daledev.holidaypricescrapper.dao;

import com.daledev.holidaypricescrapper.domain.PriceHistory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * @author dale.ellis
 * @since 03/01/2019
 */
@Repository
@Profile("aws")
public class AwsPriceStoreDao implements PriceStoreDao {
    @Override
    public PriceHistory getPriceHistory() throws IOException {
        //TODO
        return new PriceHistory();
    }

    @Override
    public void storePrice(PriceHistory priceHistory) throws IOException {
        //TODO
    }
}
