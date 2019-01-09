package com.daledev.holidaypricescrapper.domain;

import java.util.Arrays;
import java.util.Date;

/**
 * @author dale.ellis
 * @since 06/01/2019
 */
public class HolidayCriterion {
    private String description;
    private Date startDate;
    private Date endDate;
    private Airport[] airports;
    private int duration;
    private String accommodationRef;
    private int numberOfAdults = 0;
    private int[] childrenAges = new int[0];

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Airport[] getAirports() {
        return airports;
    }

    public void setAirports(Airport[] airports) {
        this.airports = airports;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAccommodationRef() {
        return accommodationRef;
    }

    public void setAccommodationRef(String accommodationRef) {
        this.accommodationRef = accommodationRef;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int[] getChildrenAges() {
        return childrenAges;
    }

    public void setChildrenAges(int[] childrenAges) {
        this.childrenAges = childrenAges;
    }

    @Override
    public String toString() {
        return "HolidayCriterion{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", airports=" + Arrays.toString(airports) +
                ", duration=" + duration +
                ", accommodationRef='" + accommodationRef + '\'' +
                ", numberOfAdults=" + numberOfAdults +
                ", childrenAges=" + Arrays.toString(childrenAges) +
                '}';
    }
}
