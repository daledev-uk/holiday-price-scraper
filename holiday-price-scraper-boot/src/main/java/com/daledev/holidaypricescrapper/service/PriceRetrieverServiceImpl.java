package com.daledev.holidaypricescrapper.service;

import com.daledev.holidaypricescrapper.constants.PriceStatus;
import com.daledev.holidaypricescrapper.dao.HolidayQuoteDao;
import com.daledev.holidaypricescrapper.dao.PriceStoreDao;
import com.daledev.holidaypricescrapper.domain.Airport;
import com.daledev.holidaypricescrapper.domain.HolidayQuote;
import com.daledev.holidaypricescrapper.domain.PriceHistory;
import com.daledev.holidaypricescrapper.domain.PriceSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dale.ellis
 * @since 28/12/2018
 */
@Service
public class PriceRetrieverServiceImpl implements PriceRetrieverService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private HolidayQuoteDao holidayQuoteDao;
    private MailService mailService;
    private PriceStoreDao priceStoreDao;

    @Value("${searchUuid:defaultSearchUUI}")
    private String searchUuid;

    /**
     * @param holidayQuoteDao
     * @param mailService
     * @param priceStoreDao
     */
    public PriceRetrieverServiceImpl(HolidayQuoteDao holidayQuoteDao, MailService mailService, PriceStoreDao priceStoreDao) {
        this.holidayQuoteDao = holidayQuoteDao;
        this.mailService = mailService;
        this.priceStoreDao = priceStoreDao;
    }

    @Override
    public List<HolidayQuote> getPrices() {
        try {
            // TODO : Criteria should come from a request or saved request
            Date startDate = new SimpleDateFormat("dd-MM-yyyy").parse("01-05-2019");
            Date endDate = new SimpleDateFormat("dd-MM-yyyy").parse("30-06-2019");
            Airport[] airports = new Airport[]{new Airport("STN")};
            return holidayQuoteDao.getQuotes(startDate, endDate, airports, 7, "047406:HOTEL");
        } catch (ParseException e) {
            log.error("Failed to fetch quotes", e);
            return new ArrayList<>();
        }
    }

    @Override
    public PriceSnapshot getCurrentCheapestPrice() {
        List<HolidayQuote> cheapest = new ArrayList<>();
        List<HolidayQuote> prices = getPrices();

        for (HolidayQuote price : prices) {
            if (cheapest.isEmpty()) {
                cheapest.add(price);
            } else if (cheapest.get(0).getPrice() > price.getPrice()) {
                cheapest.clear();
                cheapest.add(price);
            } else if (cheapest.get(0).getPrice().equals(price.getPrice())) {
                cheapest.add(price);
            }

        }

        return new PriceSnapshot(cheapest);
    }

    @Override
    public PriceStatus priceCheck() throws IOException {
        PriceHistory priceHistory = priceStoreDao.getPriceHistory(searchUuid);
        PriceSnapshot currentPrice = getCurrentCheapestPrice();
        log.debug("History of prices retrieved, current cheapest price : {}", currentPrice);

        PriceStatus priceStatus = priceHistory.getPriceChangeStatus(currentPrice);
        log.debug("Price status : {}", priceStatus);

        updatePriceHistory(priceHistory, currentPrice);
        alertPriceStatus(priceHistory, priceStatus);

        return priceStatus;
    }

    private void updatePriceHistory(PriceHistory priceHistory, PriceSnapshot newPrice) throws IOException {
        PriceStatus priceStatus = priceHistory.getPriceChangeStatus(newPrice);

        if (priceStatus == PriceStatus.FIRST_PRICE) {
            priceHistory.addSnapshot(newPrice);
        } else if (priceStatus == PriceStatus.SAME) {
            priceHistory.updateCurrentPriceTimeFrame();
        } else {
            priceHistory.addSnapshot(newPrice);
        }

        priceStoreDao.storePrice(priceHistory);
    }

    private void alertPriceStatus(PriceHistory priceHistory, PriceStatus priceStatus) {
        if (priceStatus == PriceStatus.DECREASED) {
            alertPriceDrop(priceHistory);
        } else if (priceStatus == PriceStatus.INCREASED) {
            alertPriceIncrease(priceHistory);
        }
    }

    private void alertPriceDrop(PriceHistory priceHistory) {
        log.debug("Sending price dropped alert, cheapest recorded price : {}", priceHistory.isCurrentPriceCheapestCaptured());
        mailService.sendEmail("dale.ellis@netcall.com", "Price Drop", "<b>Price Dropped : &#163;" + priceHistory.getLastRecordedPrice().getPrice() + "</b>, is cheapest ever captured : " + priceHistory.isCurrentPriceCheapestCaptured());
    }

    private void alertPriceIncrease(PriceHistory priceHistory) {
        mailService.sendEmail("dale.ellis@netcall.com", "Price Increase", "Price has increased. £" + priceHistory.getLastRecordedPrice().getPrice());
    }
}
