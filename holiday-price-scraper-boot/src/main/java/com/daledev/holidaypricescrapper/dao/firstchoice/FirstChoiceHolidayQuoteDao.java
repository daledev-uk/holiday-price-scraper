package com.daledev.holidaypricescrapper.dao.firstchoice;

import com.daledev.holidaypricescrapper.dao.HolidayQuoteDao;
import com.daledev.holidaypricescrapper.domain.Airport;
import com.daledev.holidaypricescrapper.domain.HolidayCriterion;
import com.daledev.holidaypricescrapper.domain.HolidayQuote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
            "&noOfAdults={adults}&noOfChildren={children}&childrenAge={childrenAges}" +
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
    public List<HolidayQuote> getQuotes(HolidayCriterion holidayCriterion) {
        log.debug("Retrieving quotes from first choice : {}", holidayCriterion);
        List<HolidayQuote> allPrices = new ArrayList<>();
        String airportCodes = Stream.of(holidayCriterion.getAirports()).map(Airport::getCode)
                .collect(Collectors.joining("|"));
        int adults = holidayCriterion.getNumberOfAdults();
        int children = holidayCriterion.getChildrenAges().length;
        String childrenAges = IntStream.of(holidayCriterion.getChildrenAges()).mapToObj(String::valueOf)
                .collect(Collectors.joining("|"));
        int duration = getDurationCode(holidayCriterion.getDuration());

        for (String dateString : getStartOfMonthsInDateRange(holidayCriterion.getStartDate(), holidayCriterion.getEndDate())) {
            RestTemplate restTemplate = new RestTemplate();
            String responseHtml = restTemplate.getForEntity(SCRAPE_URL, String.class, airportCodes, holidayCriterion.getAccommodationRef(), dateString, adults, children, childrenAges, duration).getBody();
            allPrices.addAll(priceScraper.extract(responseHtml));
        }

        return allPrices;
    }

    private int getDurationCode(int days) {
        if (days == 14) {
            return 1413;
        } else if (days == 10 || days == 11) {
            return 1014;
        } else {
            return 7114; // 7 days
        }

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
