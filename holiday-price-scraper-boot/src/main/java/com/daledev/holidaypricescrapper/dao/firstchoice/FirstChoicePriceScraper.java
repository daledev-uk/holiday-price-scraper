package com.daledev.holidaypricescrapper.dao.firstchoice;

import com.daledev.holidaypricescrapper.constants.QuoteResultStatus;
import com.daledev.holidaypricescrapper.domain.Airport;
import com.daledev.holidaypricescrapper.domain.HolidayQuote;
import com.daledev.holidaypricescrapper.domain.HolidayQuoteResults;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dale.ellis
 * @since 28/12/2018
 */
@Component
public class FirstChoicePriceScraper {
    private static final Logger log = LoggerFactory.getLogger(FirstChoicePriceScraper.class);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private static final String HOLIDAYS_ALL_GONE_ELEMENT_ID = "allGoneBanner";

    /**
     * @param htmlContent
     * @return
     */
    public HolidayQuoteResults extract(String htmlContent) {
        Document htmlDocument = Jsoup.parse(htmlContent);
        String json = getJsonDataFromHtmlResponse(htmlDocument);
        JSONArray priceJsonArray = getPriceJsonArray(json);
        if (priceJsonArray.length() == 0) {
            return discoverNoPriceReason(htmlDocument);
        } else {
            return convertJsonArrayToPrices(priceJsonArray);
        }
    }

    private String getJsonDataFromHtmlResponse(Document htmlDocument) {
        Element scriptContainingResults = htmlDocument.body().getElementsByTag("script").get(0);
        DataNode dataNode = (DataNode) scriptContainingResults.childNode(0);
        String scriptData = dataNode.getWholeData();
        return scriptData.substring(scriptData.indexOf('{'), scriptData.lastIndexOf("};") + 1);
    }

    private JSONArray getPriceJsonArray(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            // searchResult > holidays > dateSliderViewData
            if (jsonObject.has("searchResult")) {
                JSONObject searchResultJsonObject = jsonObject.optJSONObject("searchResult");
                return searchResultJsonObject.optJSONArray("dateSliderViewData");
            } else {
                log.debug("Holiday result JSON contains no search results");
                return new JSONArray();
            }

        } catch (Exception e) {
            log.error("Could not extract price from search result JSON", e);
            return new JSONArray();
        }
    }

    private HolidayQuoteResults convertJsonArrayToPrices(JSONArray priceJsonArray) {
        HolidayQuoteResults prices = new HolidayQuoteResults();
        for (int i = 0; i < priceJsonArray.length(); i++) {
            HolidayQuote price = getPrice(priceJsonArray.optJSONObject(i));
            if (price != null) {
                prices.getResults().add(price);
            }
        }
        return prices;
    }

    private HolidayQuote getPrice(JSONObject jsonObject) {
        String priceInSterling = jsonObject.optString("price");
        if (priceInSterling != null && !priceInSterling.equals("null")) {
            String date = jsonObject.optString("date");
            try {
                Date holidayDate = DATE_FORMAT.parse(date);
                Double price = new Double(priceInSterling.substring(1));
                return new HolidayQuote(holidayDate, price, getAirport(jsonObject));
            } catch (ParseException e) {
                log.error("Failed to parse data", e);
            }
        }
        return null;
    }

    private Airport getAirport(JSONObject jsonObject) {
        String accomUrl = jsonObject.optString("accomUrl");
        try {
            String accomUrlDecoded = java.net.URLDecoder.decode(accomUrl, "UTF-8");
            Pattern pattern = Pattern.compile("&airports\\[\\]=\\S+?&");

            Matcher matcher = pattern.matcher(accomUrlDecoded);
            if (matcher.find()) {
                String airportParamString = matcher.group(0);
                String airportCode = airportParamString.substring(airportParamString.indexOf('=') + 1, airportParamString.length() - 1);
                return new Airport(airportCode);
            }
        } catch (Exception e) {
            // Swallow
        }
        return null;
    }

    private HolidayQuoteResults discoverNoPriceReason(Document htmlDocument) {
        HolidayQuoteResults results = new HolidayQuoteResults();
        Element holidayRemoveBannerElement = htmlDocument.getElementById(HOLIDAYS_ALL_GONE_ELEMENT_ID);
        if (holidayRemoveBannerElement != null) {
            log.debug("Holiday all gone element found : {}", holidayRemoveBannerElement.text());
            results.setStatus(QuoteResultStatus.ALL_GONE);
        }
        return results;
    }
}