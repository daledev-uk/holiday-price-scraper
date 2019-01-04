package com.daledev.holidaypricescrapper.util;

import com.daledev.holidaypricescrapper.domain.PriceHistory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author dale.ellis
 * @since 04/01/2019
 */
public class PriceHistoryJsonMapper {
    private static final Logger log = LoggerFactory.getLogger(PriceHistoryJsonMapper.class);
    private static ObjectMapper objectMapper;

    private PriceHistoryJsonMapper() {
    }

    static {
        objectMapper = new ObjectMapper();
        objectMapper.writer(new DefaultPrettyPrinter());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * @param priceHistory
     * @return
     */
    public static String toJson(PriceHistory priceHistory) {
        try {
            return objectMapper.writeValueAsString(priceHistory);
        } catch (JsonProcessingException e) {
            log.error("Failed to write object to json", e);
            return "{}";
        }
    }

    public static void writeToFile(PriceHistory priceHistory, File historyFile) throws IOException {
        objectMapper.writeValue(historyFile, priceHistory);
    }

    /**
     * @param historyFile
     * @return
     */
    public static PriceHistory fromJsonFile(File historyFile) throws IOException {
        return objectMapper.readValue(historyFile, PriceHistory.class);
    }

    /**
     * @param json
     * @return
     */
    public static PriceHistory fromJson(String json) {
        try {
            return objectMapper.readValue(json, PriceHistory.class);
        } catch (IOException e) {
            log.error("Failed to create object from json", e);
            return null;
        }
    }

}
