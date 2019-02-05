package com.daledev.holidaypricescrapper.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
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
        this(new ArrayList<>());
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

    @JsonIgnore // Required for DTO mapping
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

    @JsonIgnore
    public Double getPrice() {
        return cheapestQuotes.isEmpty() ? null : cheapestQuotes.get(0).getPrice();
    }

    @JsonIgnore
    public Date getDepartDate() {
        return cheapestQuotes.isEmpty() ? null : cheapestQuotes.get(0).getDate();
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
