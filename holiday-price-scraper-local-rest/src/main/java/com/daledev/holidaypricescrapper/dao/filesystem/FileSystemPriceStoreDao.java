package com.daledev.holidaypricescrapper.dao.filesystem;

import com.daledev.holidaypricescrapper.dao.PriceStoreDao;
import com.daledev.holidaypricescrapper.domain.PriceHistory;
import com.daledev.holidaypricescrapper.util.PriceHistoryJsonMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dale.ellis
 * @since 30/12/2018
 */
@Repository
public class FileSystemPriceStoreDao implements PriceStoreDao {
    private static final String JSON_EXTENSION = ".json";
    private File historyFolder;

    @Value("${local.storage.folder.path:#{null}}")
    private String storageFolderPath;


    @Override
    public List<PriceHistory> getPriceHistories() throws IOException {
        List<PriceHistory> histories = new ArrayList<>();

        for (File file : getFiles()) {
            PriceHistory priceHistory = getPriceHistory(file);
            if (priceHistory != null) {
                histories.add(priceHistory);
            }
        }

        return histories;
    }

    @Override
    public PriceHistory getPriceHistory(String searchUuid) throws IOException {
        File historyFile = new File(historyFolder, searchUuid + JSON_EXTENSION);
        PriceHistory priceHistory = getPriceHistory(historyFile);
        if (priceHistory == null) {
            priceHistory = new PriceHistory();
            priceHistory.setSearchUUID(searchUuid);
        }
        return priceHistory;
    }

    @Override
    public void storePrice(PriceHistory priceHistory) throws IOException {
        File historyFile = new File(historyFolder, priceHistory.getSearchUUID() + JSON_EXTENSION);
        PriceHistoryJsonMapper.writeToFile(priceHistory, historyFile);
    }

    private PriceHistory getPriceHistory(File historyFile) throws IOException {
        if (historyFile.exists()) {
            return PriceHistoryJsonMapper.fromJsonFile(historyFile);
        } else {
            return null;
        }
    }

    @PostConstruct
    private void createStorageFolder() {
        if (storageFolderPath == null) {
            File tempDirectory = new File(System.getProperty("java.io.tmpdir"));
            historyFolder = new File(tempDirectory, "price-scrape-history");
        } else {
            historyFolder = new File(storageFolderPath);
        }

        if (!historyFolder.exists()) {
            historyFolder.mkdirs();
        }
    }

    private File[] getFiles() {
        return historyFolder.listFiles((dir, name) -> name.endsWith(JSON_EXTENSION));
    }
}
