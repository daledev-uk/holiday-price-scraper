package com.daledev.holidaypricescrapper.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dale.ellis
 * @since 28/12/2018
 */
public class HolidayQuote implements Serializable {
    private Airport airport;
    private Date date;
    private Double price;

    public HolidayQuote() {
    }

    public HolidayQuote(Date date, Double price, Airport airport) {
        this.date = date;
        this.price = price;
        this.airport = airport;
    }

    public Airport getAirport() {
        return airport;
    }

    public void setAirport(Airport airport) {
        this.airport = airport;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "HolidayQuote{" +
                "airport=" + airport +
                ", date=" + date +
                ", price=" + price +
                '}';
    }
}
