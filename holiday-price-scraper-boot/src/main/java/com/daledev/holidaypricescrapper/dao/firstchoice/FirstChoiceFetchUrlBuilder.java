package com.daledev.holidaypricescrapper.dao.firstchoice;

import com.daledev.holidaypricescrapper.domain.Airport;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author dale.ellis
 * @since 10/02/2019
 */
public class FirstChoiceFetchUrlBuilder {
    private static final String SCRAPE_URL = "https://www.firstchoice.co.uk/holiday/packages";

    private String airportCodes;
    private String accommodationRef;
    private String date;
    private String until;
    private int flexibleDays = 0;
    private int numberOfAdults = 0;
    private int[] childrenAges = new int[0];
    private int durationCode;
    private String searchRequestType = "ins";
    private String searchType = "search";
    private boolean sp = true;
    private boolean multiSelect = true;
    private String room;
    private boolean isVilla = false;

    private FirstChoiceFetchUrlBuilder() {
    }

    public static FirstChoiceFetchUrlBuilder get() {
        return new FirstChoiceFetchUrlBuilder();
    }

    /**
     * @return
     */
    public String build() {
        StringBuilder url = new StringBuilder(SCRAPE_URL);

        url.append("?airports[]=").append(airportCodes);
        url.append("&units[]=").append(accommodationRef);
        url.append("&when=").append(date);
        url.append("&until=").append(blankStringNulls(until));
        url.append("&flexibility=").append(flexibleDays > 0);
        url.append("&flexibleDays=").append(flexibleDays);
        url.append("&noOfAdults=").append(numberOfAdults);
        url.append("&noOfChildren=").append(childrenAges.length);
        url.append("&childrenAge=").append(IntStream.of(childrenAges).mapToObj(String::valueOf).collect(Collectors.joining("|")));
        url.append("&duration=").append(durationCode);
        url.append("&searchRequestType=").append(searchRequestType);
        url.append("&searchType=").append(searchType);
        url.append("&sp=").append(sp);
        url.append("&multiSelect=").append(multiSelect);
        url.append("&room=").append(blankStringNulls(room));
        url.append("&isVilla=").append(isVilla);

        return url.toString();
    }

    public FirstChoiceFetchUrlBuilder withAirports(Airport[] airports) {
        this.airportCodes = Stream.of(airports).map(Airport::getCode).collect(Collectors.joining("|"));
        return this;
    }

    public FirstChoiceFetchUrlBuilder withAccommodationRef(String accommodationRef) {
        this.accommodationRef = accommodationRef;
        return this;
    }

    public FirstChoiceFetchUrlBuilder withDate(String date) {
        this.date = date;
        return this;
    }

    public FirstChoiceFetchUrlBuilder withUntil(String until) {
        this.until = until;
        return this;
    }

    public FirstChoiceFetchUrlBuilder withFlexibleDays(int flexibleDays) {
        this.flexibleDays = flexibleDays;
        return this;
    }

    public FirstChoiceFetchUrlBuilder withNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
        return this;
    }

    public FirstChoiceFetchUrlBuilder withChildrenAges(int[] childrenAges) {
        this.childrenAges = childrenAges;
        return this;
    }

    public FirstChoiceFetchUrlBuilder withDuration(int durationInDays) {
        if (durationInDays == 14) {
            this.durationCode = 1413;
        } else if (durationInDays == 10 || durationInDays == 11) {
            this.durationCode = 1014;
        } else {
            this.durationCode = 7114; // 7 days
        }
        return this;
    }

    public FirstChoiceFetchUrlBuilder withSearchRequestType(String searchRequestType) {
        this.searchRequestType = searchRequestType;
        return this;
    }

    public FirstChoiceFetchUrlBuilder withSearchType(String searchType) {
        this.searchType = searchType;
        return this;
    }

    public FirstChoiceFetchUrlBuilder withSp(boolean sp) {
        this.sp = sp;
        return this;
    }

    public FirstChoiceFetchUrlBuilder withMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
        return this;
    }

    public FirstChoiceFetchUrlBuilder withRoom(String room) {
        this.room = room;
        return this;
    }

    public FirstChoiceFetchUrlBuilder withVilla(boolean villa) {
        isVilla = villa;
        return this;
    }

    private String blankStringNulls(Object value) {
        return value == null ? "" : value.toString();
    }


}
