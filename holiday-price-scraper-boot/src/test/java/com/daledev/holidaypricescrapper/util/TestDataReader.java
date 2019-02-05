package com.daledev.holidaypricescrapper.util;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author dale.ellis
 * @since 10/01/2019
 */
public class TestDataReader {
    private static final String TEST_CONTENT_PATH = "/test-content/";
    private static final String FIRST_CHOICE_PRICES_HTML = "first-choice-prices.html";
    private static final String FIRST_CHOICE_NO_PRICE_HTML = "first-choice-no-price.html";

    private TestDataReader() {
    }

    public static String getFirstChoicePricesHtml() {
        return getFileData(TEST_CONTENT_PATH + FIRST_CHOICE_PRICES_HTML);
    }

    public static String getFirstChoiceNoPriceHtml() {
        return getFileData(TEST_CONTENT_PATH + FIRST_CHOICE_NO_PRICE_HTML);
    }

    private static String getFileData(String filename) {
        try {
            URL fileResourceUrl = TestDataReader.class.getResource(filename);
            Path path = Paths.get(fileResourceUrl.toURI());
            return new String(java.nio.file.Files.readAllBytes(path), "UTF8");
        } catch (Exception ex) {
            return null;
        }
    }
}
