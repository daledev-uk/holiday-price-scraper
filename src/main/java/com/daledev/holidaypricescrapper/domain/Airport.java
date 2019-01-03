package com.daledev.holidaypricescrapper.domain;

import java.io.Serializable;

/**
 * @author dale.ellis
 * @since 28/12/2018
 */
public class Airport implements Serializable {
    private String code;
    private String uniqueId;

    public Airport() {
    }

    public Airport(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public String toString() {
        return code;
    }
}
