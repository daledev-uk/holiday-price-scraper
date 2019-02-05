package com.daledev.holidaypricescrapper.domain;

import com.daledev.holidaypricescrapper.constants.PriceStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dale.ellis
 * @since 30/12/2018
 */
public class PriceHistory {
    private String searchUUID;
    private HolidayCriterion criterion = new HolidayCriterion();
    private List<PriceSnapshot> history = new ArrayList<>();
    private List<String> subscribers = new ArrayList<>();
    private boolean active = true;

    public String getSearchUUID() {
        return searchUUID;
    }

    public void setSearchUUID(String searchUUID) {
        this.searchUUID = searchUUID;
    }

    public HolidayCriterion getCriterion() {
        return criterion;
    }

    public void setCriterion(HolidayCriterion criterion) {
        this.criterion = criterion;
    }

    public List<PriceSnapshot> getHistory() {
        return history;
    }

    public void setHistory(List<PriceSnapshot> history) {
        this.history = history;
    }

    public List<String> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<String> subscribers) {
        this.subscribers = subscribers;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @JsonIgnore
    public PriceSnapshot getLastRecordedPrice() {
        return history == null || history.isEmpty() ? null : history.get(history.size() - 1);
    }

    /**
     * @param priceSnapshot
     * @return
     */
    public PriceStatus getPriceChangeStatus(PriceSnapshot priceSnapshot) {
        if (priceSnapshot.getCheapestQuotes().isEmpty()) {
            return PriceStatus.NO_PRICE;
        }
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

    @JsonIgnore
    public PriceSnapshot getPriceWhenCheapest() {
        PriceSnapshot cheapest = null;
        for (PriceSnapshot priceSnapshot : history) {
            if (cheapest == null || priceSnapshot.getPrice() < cheapest.getPrice()) {
                cheapest = priceSnapshot;
            }
        }
        return cheapest;
    }

    @JsonIgnore
    public PriceSnapshot getPriceWhenMostExpensive() {
        PriceSnapshot mostExpensive = null;
        for (PriceSnapshot priceSnapshot : history) {
            if (mostExpensive == null || priceSnapshot.getPrice() > mostExpensive.getPrice()) {
                mostExpensive = priceSnapshot;
            }
        }
        return mostExpensive;
    }

    public void updateCurrentPriceTimeFrame() {
        getLastRecordedPrice().setLastRetrievedDate(new Date());
    }

    @JsonIgnore
    public boolean isCurrentPriceCheapestCaptured() {
        return getLastRecordedPrice() == null || getLastRecordedPrice().getPrice() == getPriceWhenCheapest().getPrice();
    }
}
