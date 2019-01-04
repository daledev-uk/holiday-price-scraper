package com.daledev.holidaypricescrapper.dao;

import com.daledev.holidaypricescrapper.domain.Airport;
import com.daledev.holidaypricescrapper.domain.HolidayQuote;

import java.util.Date;
import java.util.List;

/**
 * @author dale.ellis
 * @since 02/01/2019
 */
public interface HolidayQuoteDao {

    /**
     * @param startDate
     * @param endDate
     * @param airports
     * @param duration
     * @param accommodationRef
     * @return
     */
    List<HolidayQuote> getQuotes(Date startDate, Date endDate, Airport[] airports, int duration, String accommodationRef);
}
