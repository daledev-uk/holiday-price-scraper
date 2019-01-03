package com.daledev.holidaypricescrapper.dao;

import com.daledev.holidaypricescrapper.domain.PriceHistory;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

/**
 * @author dale.ellis
 * @since 30/12/2018
 */
@Repository
@Profile("boot")
public class FileSystemPriceStoreDao implements PriceStoreDao {
    private File historyFile;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PriceHistory getPriceHistory() throws IOException {
        if (historyFile.exists()) {
            return objectMapper.readValue(historyFile, PriceHistory.class);
        } else {
            return new PriceHistory();
        }
    }

    @Override
    public void storePrice(PriceHistory priceHistory) throws IOException {
        objectMapper.writeValue(historyFile, priceHistory);
    }

    @PostConstruct
    private void createStorageFolder() {
        File tempDirectory = new File(System.getProperty("java.io.tmpdir"));
        File historyFolder = new File(tempDirectory, "price-scrape-history");
        if (!historyFolder.exists()) {
            historyFolder.mkdirs();
        }
        historyFile = new File(historyFile, "history.json");

        objectMapper.writer(new DefaultPrettyPrinter());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
