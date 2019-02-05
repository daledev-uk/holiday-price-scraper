package com.daledev.holidaypricescrapper.dao.aws;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.daledev.holidaypricescrapper.dao.PriceStoreDao;
import com.daledev.holidaypricescrapper.domain.PriceHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author dale.ellis
 * @since 04/01/2019
 */
@Repository
public class DynamoDBPriceStoreDao implements PriceStoreDao {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<PriceHistory> getPriceHistories() {
        List<PriceHistory> histories = new ArrayList<>();

        log.debug("Scanning For all PriceHistory items");
        ItemCollection<ScanOutcome> scanResults = getHistoryTable().scan();
        for (Item item : scanResults) {
            log.debug("Item {} from results returned in results", item);
            PriceHistory priceHistory = PriceHistoryMapper.mapToPriceHistory(item);
            if (priceHistory != null) {
                histories.add(priceHistory);
            }
        }

        log.debug("Returning {} price histories", histories.size());
        return histories;
    }

    @Override
    public PriceHistory getPriceHistory(String searchUuid) {
        log.debug("Attempting to retrieve PriceHistory with ID : {}", searchUuid);
        Item persistedHistory = getHistoryTable().getItem(PriceHistoryMapper.PRIMARY_KEY_NAME, searchUuid);

        if (persistedHistory != null) {
            log.debug("History found in DynamoDB. {}", persistedHistory.toJSONPretty());
            return PriceHistoryMapper.mapToPriceHistory(persistedHistory);
        }
        return new PriceHistory();
    }

    @Override
    public void storePrice(PriceHistory priceHistory) {
        log.debug("Storing history with ID : {}", priceHistory.getSearchUUID());

        Item item = PriceHistoryMapper.mapFromPriceHistory(priceHistory);
        PutItemOutcome putItemOutcome = getHistoryTable().putItem(item);

        log.debug("Create response : {}", putItemOutcome);
    }

    private Table getHistoryTable() {
        log.debug("Creating Table API object for table : {}", PriceHistoryMapper.TABLE_NAME);
        DynamoDB dynamoDB = getDynamoDB();
        return dynamoDB.getTable(PriceHistoryMapper.TABLE_NAME);
    }

    private DynamoDB getDynamoDB() {
        log.debug("Creating AmazonDynamoDB client");
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        log.debug("AmazonDynamoDB client : {}", client);
        return new DynamoDB(client);
    }
}
