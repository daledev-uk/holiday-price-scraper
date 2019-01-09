package com.daledev.holidaypricescrapper.domain;

import com.daledev.holidaypricescrapper.constants.PriceStatus;

/**
 * @author dale.ellis
 * @since 07/01/2019
 */
public class PriceCheckResult {
    private String searchUUID;
    private String criterionDescription;
    private PriceStatus priceStatus;
    private Double price;

    public PriceCheckResult() {
    }

    public PriceCheckResult(String searchUUID, String criterionDecription, PriceStatus priceStatus, Double price) {
        this.searchUUID = searchUUID;
        this.criterionDescription = criterionDecription;
        this.priceStatus = priceStatus;
        this.price = price;
    }

    public String getSearchUUID() {
        return searchUUID;
    }

    public void setSearchUUID(String searchUUID) {
        this.searchUUID = searchUUID;
    }

    public String getCriterionDescription() {
        return criterionDescription;
    }

    public void setCriterionDescription(String criterionDescription) {
        this.criterionDescription = criterionDescription;
    }

    public PriceStatus getPriceStatus() {
        return priceStatus;
    }

    public void setPriceStatus(PriceStatus priceStatus) {
        this.priceStatus = priceStatus;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
