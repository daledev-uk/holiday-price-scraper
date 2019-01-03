package com.daledev.holidaypricescrapper.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author dale.ellis
 * @since 30/12/2018
 */
public class PriceSnapshot implements Serializable {
    private Date firstRetrievedDate;
    private Date lastRetrievedDate;
    private List<HolidayQuote> cheapestQuotes;

    public PriceSnapshot() {
    }

    public PriceSnapshot(List<HolidayQuote> holidayQuotes) {
        this.firstRetrievedDate = new Date();
        this.lastRetrievedDate = firstRetrievedDate;
        this.cheapestQuotes = holidayQuotes;
    }

    public Date getFirstRetrievedDate() {
        return firstRetrievedDate;
    }

    public void setFirstRetrievedDate(Date firstRetrievedDate) {
        this.firstRetrievedDate = firstRetrievedDate;
    }

    public Date getRetrievedDate() {
        return firstRetrievedDate;
    }

    public Date getLastRetrievedDate() {
        return lastRetrievedDate;
    }

    public void setLastRetrievedDate(Date lastRetrievedDate) {
        this.lastRetrievedDate = lastRetrievedDate;
    }

    public List<HolidayQuote> getCheapestQuotes() {
        return cheapestQuotes;
    }

    public void setCheapestQuotes(List<HolidayQuote> cheapestQuotes) {
        this.cheapestQuotes = cheapestQuotes;
    }

    public double getPrice() {
        return getCheapestQuotes().get(0).getPrice();
    }

    @Override
    public String toString() {
        if (cheapestQuotes.size() == 1) {
            return cheapestQuotes.get(0).toString();
        } else {
            return cheapestQuotes.size() + " prices @ " + getPrice();
        }
    }
}
