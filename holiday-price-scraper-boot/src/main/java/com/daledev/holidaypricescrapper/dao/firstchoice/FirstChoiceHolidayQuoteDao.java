package com.daledev.holidaypricescrapper.dao.firstchoice;

import com.daledev.holidaypricescrapper.dao.HolidayQuoteDao;
import com.daledev.holidaypricescrapper.domain.HolidayCriterion;
import com.daledev.holidaypricescrapper.domain.HolidayQuoteResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author dale.ellis
 * @since 02/01/2019
 */
@Repository
public class FirstChoiceHolidayQuoteDao implements HolidayQuoteDao {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final SimpleDateFormat URL_DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");

    private FirstChoicePriceScraper priceScraper;

    public FirstChoiceHolidayQuoteDao(FirstChoicePriceScraper firstChoicePriceScraper) {
        this.priceScraper = firstChoicePriceScraper;
    }

    @Override
    public HolidayQuoteResults getQuotes(HolidayCriterion holidayCriterion) {
        log.debug("Retrieving quotes from first choice : {}", holidayCriterion);

        FirstChoiceFetchUrlBuilder builder = createQuoteUrl(holidayCriterion);
        return getHolidayQuoteResults(holidayCriterion, builder);
    }

    private FirstChoiceFetchUrlBuilder createQuoteUrl(HolidayCriterion holidayCriterion) {
        FirstChoiceFetchUrlBuilder builder = FirstChoiceFetchUrlBuilder.get();
        builder.withAccommodationRef(holidayCriterion.getAccommodationRef());
        builder.withAirports(holidayCriterion.getAirports());
        builder.withNumberOfAdults(holidayCriterion.getNumberOfAdults());
        builder.withChildrenAges(holidayCriterion.getChildrenAges());
        builder.withDuration(holidayCriterion.getDuration());
        builder.withFlexibleDays(3);
        return builder;
    }

    private HolidayQuoteResults getHolidayQuoteResults(HolidayCriterion holidayCriterion, FirstChoiceFetchUrlBuilder builder) {
        HolidayQuoteResults allPrices = new HolidayQuoteResults();

        for (String dateString : getStartOfMonthsInDateRange(holidayCriterion.getStartDate(), holidayCriterion.getEndDate())) {
            HolidayQuoteResults holidayQuoteResults = getHolidayQuoteResultsForMonth(builder, dateString);
            allPrices.accumulate(holidayQuoteResults);
        }

        return allPrices;
    }

    private HolidayQuoteResults getHolidayQuoteResultsForMonth(FirstChoiceFetchUrlBuilder builder, String dateString) {
        builder.withDate(dateString);

        RestTemplate restTemplate = new RestTemplate();
        String responseHtml = restTemplate.getForEntity(builder.build(), String.class).getBody();

        return priceScraper.extract(responseHtml);
    }

    private Collection<String> getStartOfMonthsInDateRange(Date startDate, Date endDate) {
        Set<String> startOfMonths = new LinkedHashSet<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        startOfMonths.add(getStartOfMonth(calendar));

        while (calendar.getTime().before(endDate)) {
            startOfMonths.add(getStartOfMonth(calendar));
            calendar.add(Calendar.HOUR, 24);
        }

        return startOfMonths;
    }

    private String getStartOfMonth(Calendar calendar) {
        Calendar startOfMonthCal = Calendar.getInstance();
        startOfMonthCal.setTime(calendar.getTime());

        startOfMonthCal.set(Calendar.DAY_OF_MONTH, 1);
        return URL_DATE_FORMATTER.format(startOfMonthCal.getTime());
    }
}
