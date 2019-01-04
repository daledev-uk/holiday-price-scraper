package com.daledev.holidaypricescrapper.dao.firstchoice;

import com.daledev.holidaypricescrapper.dao.HolidayQuoteDao;
import com.daledev.holidaypricescrapper.domain.Airport;
import com.daledev.holidaypricescrapper.domain.HolidayQuote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author dale.ellis
 * @since 02/01/2019
 */
@Repository
public class FirstChoiceHolidayQuoteDao implements HolidayQuoteDao {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final SimpleDateFormat URL_DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");

    // TODO : make this a builder
    private static final String SCRAPE_URL = "https://www.firstchoice.co.uk/holiday/packages" +
            "?airports[]={airports}" +
            "&units[]={accommodationRef}" +
            "&when={date}" +
            "&until=&flexibility=true&flexibleDays=3" +
            "&noOfAdults=2&noOfChildren=1&childrenAge=2" +
            "&duration={duration}" +
            "&searchRequestType=ins" +
            "&searchType=search" +
            "&sp=true" +
            "&multiSelect=true" +
            "&room=" +
            "&isVilla=false";

    private FirstChoicePriceScraper priceScraper;

    public FirstChoiceHolidayQuoteDao(FirstChoicePriceScraper firstChoicePriceScraper) {
        this.priceScraper = firstChoicePriceScraper;
    }

    @Override
    public List<HolidayQuote> getQuotes(Date startDate, Date endDate, Airport[] airports, int duration, String accommodationRef) {
        log.debug("Retrieving quotes from first choice between dates [{} - {}] and from airports {} for the duration of {} nights at accommodation : {}", startDate, endDate, airports, duration, accommodationRef);
        List<HolidayQuote> allPrices = new ArrayList<>();
        String airportCodes = Stream.of(airports).map(Airport::getCode).collect(Collectors.joining("|"));

        duration = 7114; // TODO : 7 days = 7114, 2 weeks = 1413, 10-11 nights = 1014

        for (String dateString : getStartOfMonthsInDateRange(startDate, endDate)) {
            RestTemplate restTemplate = new RestTemplate();
            String responseHtml = restTemplate.getForEntity(SCRAPE_URL, String.class, airportCodes, accommodationRef, dateString, duration).getBody();
            allPrices.addAll(priceScraper.extract(responseHtml));
        }

        return allPrices;
    }

    private List<String> getStartOfMonthsInDateRange(Date startDate, Date endDate) {
        return Arrays.asList("01-05-2019", "01-06-2019");
    }
}
