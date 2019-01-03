package com.daledev.holidaypricescrapper.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dale.ellis
 * @since 28/12/2018
 */
public class HolidayQuoteDto implements Serializable {
    private AirportDto airport;
    private Date date;
    private Double price;

    public HolidayQuoteDto() {
    }

    public HolidayQuoteDto(Date date, Double price) {
        this.date = date;
        this.price = price;
    }

    public AirportDto getAirport() {
        return airport;
    }

    public void setAirport(AirportDto airport) {
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
}
