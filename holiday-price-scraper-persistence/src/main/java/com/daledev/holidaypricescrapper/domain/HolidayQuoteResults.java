package com.daledev.holidaypricescrapper.domain;

import com.daledev.holidaypricescrapper.constants.QuoteResultStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dale.ellis
 * @since 10/02/2019
 */
public class HolidayQuoteResults {
    private QuoteResultStatus status = QuoteResultStatus.OK;
    private List<HolidayQuote> results = new ArrayList<>();

    public QuoteResultStatus getStatus() {
        return status;
    }

    public void setStatus(QuoteResultStatus status) {
        this.status = status;
    }

    public List<HolidayQuote> getResults() {
        return results;
    }

    public void setResults(List<HolidayQuote> results) {
        this.results = results;
    }

    /**
     * @param holidayQuoteResults
     */
    public void accumulate(HolidayQuoteResults holidayQuoteResults) {
        results.addAll(holidayQuoteResults.getResults());
        status = holidayQuoteResults.getStatus();
    }
}
