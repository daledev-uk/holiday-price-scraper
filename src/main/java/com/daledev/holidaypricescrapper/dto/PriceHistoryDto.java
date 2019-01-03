package com.daledev.holidaypricescrapper.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dale.ellis
 * @since 30/12/2018
 */
public class PriceHistoryDto {
    private Date dateWhenCheapest;
    private List<HolidayQuoteDto> pricesWhenCheapest;
    private List<PriceSnapshotDto> history = new ArrayList<>();

    public Date getDateWhenCheapest() {
        return dateWhenCheapest;
    }

    public void setDateWhenCheapest(Date dateWhenCheapest) {
        this.dateWhenCheapest = dateWhenCheapest;
    }

    public List<HolidayQuoteDto> getPricesWhenCheapest() {
        return pricesWhenCheapest;
    }

    public void setPricesWhenCheapest(List<HolidayQuoteDto> pricesWhenCheapest) {
        this.pricesWhenCheapest = pricesWhenCheapest;
    }

    public List<PriceSnapshotDto> getHistory() {
        return history;
    }

    public void setHistory(List<PriceSnapshotDto> history) {
        this.history = history;
    }
}
