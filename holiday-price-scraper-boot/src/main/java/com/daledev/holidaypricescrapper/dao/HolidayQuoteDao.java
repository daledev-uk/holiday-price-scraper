package com.daledev.holidaypricescrapper.dao;

import com.daledev.holidaypricescrapper.domain.HolidayCriterion;
import com.daledev.holidaypricescrapper.domain.HolidayQuote;

import java.util.List;

/**
 * @author dale.ellis
 * @since 02/01/2019
 */
public interface HolidayQuoteDao {

    /**
     * @param holidayCriterion
     * @return
     */
    List<HolidayQuote> getQuotes(HolidayCriterion holidayCriterion);
}
