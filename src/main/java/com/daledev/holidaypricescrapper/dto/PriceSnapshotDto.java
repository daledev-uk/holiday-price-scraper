package com.daledev.holidaypricescrapper.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author dale.ellis
 * @since 30/12/2018
 */
public class PriceSnapshotDto implements Serializable {
    private Date retrievedDate;
    private List<HolidayQuoteDto> cheapestQuotes;

    public PriceSnapshotDto() {
    }

    public PriceSnapshotDto(List<HolidayQuoteDto> cheapest) {
        this.retrievedDate = new Date();
        this.cheapestQuotes = cheapest;
    }

    public Date getRetrievedDate() {
        return retrievedDate;
    }

    public void setRetrievedDate(Date retrievedDate) {
        this.retrievedDate = retrievedDate;
    }

    public List<HolidayQuoteDto> getCheapestQuotes() {
        return cheapestQuotes;
    }

    public void setCheapestQuotes(List<HolidayQuoteDto> cheapestQuotes) {
        this.cheapestQuotes = cheapestQuotes;
    }
}
