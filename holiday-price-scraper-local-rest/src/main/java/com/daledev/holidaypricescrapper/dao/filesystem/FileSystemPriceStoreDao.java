package com.daledev.holidaypricescrapper.dao.filesystem;

import com.daledev.holidaypricescrapper.dao.PriceStoreDao;
import com.daledev.holidaypricescrapper.domain.PriceHistory;
import com.daledev.holidaypricescrapper.util.PriceHistoryJsonMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

/**
 * @author dale.ellis
 * @since 30/12/2018
 */
@Repository
public class FileSystemPriceStoreDao implements PriceStoreDao {
    private File historyFile;

    @Override
    public PriceHistory getPriceHistory(String searchUuid) throws IOException {
        if (historyFile.exists()) {
            return PriceHistoryJsonMapper.fromJsonFile(historyFile);
        } else {
            return new PriceHistory();
        }
    }

    @Override
    public void storePrice(PriceHistory priceHistory) throws IOException {
        PriceHistoryJsonMapper.writeToFile(priceHistory, historyFile);
    }

    @PostConstruct
    private void createStorageFolder() {
        File tempDirectory = new File(System.getProperty("java.io.tmpdir"));
        File historyFolder = new File(tempDirectory, "price-scrape-history");
        if (!historyFolder.exists()) {
            historyFolder.mkdirs();
        }
        historyFile = new File(historyFile, "history.json");
    }
}
