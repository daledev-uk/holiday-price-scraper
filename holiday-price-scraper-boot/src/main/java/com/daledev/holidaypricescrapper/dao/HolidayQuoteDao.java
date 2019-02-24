package com.daledev.holidaypricescrapper.dao;

import com.daledev.holidaypricescrapper.domain.HolidayCriterion;
import com.daledev.holidaypricescrapper.domain.HolidayQuoteResults;

/**
 * @author dale.ellis
 * @since 02/01/2019
 */
public interface HolidayQuoteDao {

    /**
     * @param holidayCriterion
     * @return
     */
    HolidayQuoteResults getQuotes(HolidayCriterion holidayCriterion);
}
