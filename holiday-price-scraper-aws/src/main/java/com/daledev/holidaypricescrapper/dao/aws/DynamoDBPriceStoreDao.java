package com.daledev.holidaypricescrapper.dao.aws;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.daledev.holidaypricescrapper.dao.PriceStoreDao;
import com.daledev.holidaypricescrapper.domain.PriceHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.UUID;

/**
 * @author dale.ellis
 * @since 04/01/2019
 */
@Repository
public class DynamoDBPriceStoreDao implements PriceStoreDao {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public PriceHistory getPriceHistory(String searchUuid) throws IOException {
        log.debug("Attempting to retrieve PriceHistory with ID : {}", searchUuid);
        Item persistedHistory = getHistoryTable().getItem(PriceHistoryMapper.PRIMARY_KEY_NAME, searchUuid);

        if (persistedHistory != null) {
            log.debug("History found in DynamoDB. {}", persistedHistory.toJSONPretty());
            return PriceHistoryMapper.mapToPriceHistory(persistedHistory);
        }
        return new PriceHistory();
    }

    @Override
    public void storePrice(PriceHistory priceHistory) throws IOException {
        if (priceHistory.getSearchUUID() == null) {
            storeNewHistory(priceHistory);
        } else {
            updateStoredHistory(priceHistory);
        }
    }

    private void storeNewHistory(PriceHistory priceHistory) {
        String searchUUID = UUID.randomUUID().toString();
        log.debug("Storing new history with ID : {}", searchUUID);

        priceHistory.setSearchUUID(searchUUID);
        Item item = PriceHistoryMapper.mapFromPriceHistory(priceHistory);
        PutItemOutcome putItemOutcome = getHistoryTable().putItem(item);

        log.debug("Create response : {}", putItemOutcome);
    }

    private void updateStoredHistory(PriceHistory priceHistory) {
        log.debug("Updating history for ID : {}", priceHistory.getSearchUUID());
        Item item = PriceHistoryMapper.mapFromPriceHistory(priceHistory);

        AttributeUpdate attributeUpdate = new AttributeUpdate(PriceHistoryMapper.HISTORY_ATTRIBUTE_NAME);
        attributeUpdate.put(item.getJSON(PriceHistoryMapper.HISTORY_ATTRIBUTE_NAME));

        UpdateItemOutcome updateItemOutcome = getHistoryTable().updateItem(PriceHistoryMapper.PRIMARY_KEY_NAME, priceHistory.getSearchUUID(), attributeUpdate);
        log.debug("Update response : {}", updateItemOutcome);
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
