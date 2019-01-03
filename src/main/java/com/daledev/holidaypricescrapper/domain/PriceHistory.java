package com.daledev.holidaypricescrapper.domain;

import com.daledev.holidaypricescrapper.constants.PriceStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dale.ellis
 * @since 30/12/2018
 */
public class PriceHistory {
    private List<PriceSnapshot> history = new ArrayList<>();

    public List<PriceSnapshot> getHistory() {
        return history;
    }

    public void setHistory(List<PriceSnapshot> history) {
        this.history = history;
    }

    public PriceSnapshot getLastRecordedPrice() {
        return history.get(history.size() - 1);
    }

    /**
     * @param priceSnapshot
     * @return
     */
    public PriceStatus getPriceChangeStatus(PriceSnapshot priceSnapshot) {
        HolidayQuote currentPrice = priceSnapshot.getCheapestQuotes().get(0);

        if (history.isEmpty()) {
            return PriceStatus.FIRST_PRICE;
        } else {
            PriceSnapshot lastRecordedPrice = getLastRecordedPrice();
            double lastPriceValue = lastRecordedPrice.getPrice();
            if (lastPriceValue > currentPrice.getPrice()) {
                return PriceStatus.DECREASED;
            } else if (lastPriceValue < currentPrice.getPrice()) {
                return PriceStatus.INCREASED;
            } else {
                return PriceStatus.SAME;
            }
        }
    }

    public void addSnapshot(PriceSnapshot newPrice) {
        history.add(newPrice);
    }

    public PriceSnapshot getPriceWhenCheapest() {
        PriceSnapshot cheapest = null;
        for (PriceSnapshot priceSnapshot : history) {
            if (cheapest == null || priceSnapshot.getPrice() < cheapest.getPrice()) {
                cheapest = priceSnapshot;
            }
        }
        return cheapest;
    }

    public void updateCurrentPriceTimeFrame() {
        getLastRecordedPrice().setLastRetrievedDate(new Date());
    }

    public boolean isCurrentPriceCheapestCaptured() {
        return getLastRecordedPrice().getPrice() == getPriceWhenCheapest().getPrice();
    }
}
