package com.daledev.holidaypricescrapper.service;

import com.daledev.holidaypricescrapper.constants.PriceStatus;
import com.daledev.holidaypricescrapper.dao.HolidayQuoteDao;
import com.daledev.holidaypricescrapper.dao.PriceStoreDao;
import com.daledev.holidaypricescrapper.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author dale.ellis
 * @since 28/12/2018
 */
@Service
public class PriceRetrieverServiceImpl implements PriceRetrieverService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private MailService mailService;
    private HolidayQuoteDao holidayQuoteDao;
    private PriceStoreDao priceStoreDao;

    /**
     * @param mailService
     * @param holidayQuoteDao
     * @param priceStoreDao
     */
    public PriceRetrieverServiceImpl(MailService mailService, HolidayQuoteDao holidayQuoteDao, PriceStoreDao priceStoreDao) {
        this.mailService = mailService;
        this.holidayQuoteDao = holidayQuoteDao;
        this.priceStoreDao = priceStoreDao;
    }

    @Override
    public List<PriceCheckResult> priceChecks() throws IOException {
        List<PriceCheckResult> priceStatus = new ArrayList<>();
        for (PriceHistory priceHistory : priceStoreDao.getPriceHistories()) {
            if (priceHistory.getSearchUUID() == null) {
                priceHistory.setSearchUUID(UUID.randomUUID().toString());
                log.debug("New search registered with UUID : {}", priceHistory.getSearchUUID());
            }
            if (priceHistory.isActive()) {
                priceStatus.add(priceCheck(priceHistory));
            }
        }


        return priceStatus;
    }

    private PriceCheckResult priceCheck(PriceHistory priceHistory) throws IOException {
        PriceSnapshot currentPrice = getCurrentCheapestPrice(priceHistory);
        log.debug("History of prices retrieved, current cheapest price : {}", currentPrice);

        PriceStatus priceStatus = priceHistory.getPriceChangeStatus(currentPrice);
        log.debug("Price status : {}", priceStatus);

        updatePriceHistory(priceHistory, currentPrice);
        alertPriceStatus(priceHistory, priceStatus);

        return new PriceCheckResult(priceHistory.getSearchUUID(), priceHistory.getCriterion().getDescription(), priceStatus, currentPrice.getPrice());
    }

    private PriceSnapshot getCurrentCheapestPrice(PriceHistory priceHistory) {
        List<HolidayQuote> cheapest = new ArrayList<>();
        List<HolidayQuote> prices = getPrices(priceHistory.getCriterion());

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

    private List<HolidayQuote> getPrices(HolidayCriterion holidayCriterion) {
        return holidayQuoteDao.getQuotes(holidayCriterion);
    }

    private void updatePriceHistory(PriceHistory priceHistory, PriceSnapshot newPrice) throws IOException {
        PriceStatus priceStatus = priceHistory.getPriceChangeStatus(newPrice);

        if (priceStatus == PriceStatus.NO_PRICE) {
            log.debug("No price could be captured, maybe holiday package has been removed?");
            return;
        } else if (priceStatus == PriceStatus.FIRST_PRICE) {
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
            mailService.sendPriceDropMail(priceHistory);
        } else if (priceStatus == PriceStatus.INCREASED) {
            mailService.sendPriceIncreaseMail(priceHistory);
        }
    }
}
