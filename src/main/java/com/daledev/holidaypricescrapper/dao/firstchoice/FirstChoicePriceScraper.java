package com.daledev.holidaypricescrapper.dao.firstchoice;

import com.daledev.holidaypricescrapper.domain.Airport;
import com.daledev.holidaypricescrapper.domain.HolidayQuote;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dale.ellis
 * @since 28/12/2018
 */
@Component
public class FirstChoicePriceScraper {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    /**
     * @param htmlContent
     * @return
     */
    public List<HolidayQuote> extract(String htmlContent) {
        String json = getJsonDataFromHtmlResponse(htmlContent);
        JSONArray priceJsonArray = getPriceJsonArray(json);
        return convertJsonArrayToPrices(priceJsonArray);
    }

    private String getJsonDataFromHtmlResponse(String htmlContent) {
        Document htmlDocument = Jsoup.parse(htmlContent);
        Element scriptContainingResults = htmlDocument.body().getElementsByTag("script").get(0);
        DataNode dataNode = (DataNode) scriptContainingResults.childNode(0);
        String scriptData = dataNode.getWholeData();
        return scriptData.substring(scriptData.indexOf('{'), scriptData.lastIndexOf("};") + 1);
    }

    private JSONArray getPriceJsonArray(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            // searchResult > holidays > dateSliderViewData
            JSONObject searchResultJsonObject = jsonObject.optJSONObject("searchResult");
            return searchResultJsonObject.optJSONArray("dateSliderViewData");

        } catch (Exception e) {
            return new JSONArray();
        }
    }

    private List<HolidayQuote> convertJsonArrayToPrices(JSONArray priceJsonArray) {
        List<HolidayQuote> prices = new ArrayList<>();
        for (int i = 0; i < priceJsonArray.length(); i++) {
            HolidayQuote price = getPrice(priceJsonArray.optJSONObject(i));
            if (price != null) {
                prices.add(price);
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
                e.printStackTrace();
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
                String airportCode = airportParamString.substring(airportParamString.indexOf("=") + 1, airportParamString.length() - 1);
                return new Airport(airportCode);
            }
        } catch (Exception e) {
            // Swallow
        }
        return null;
    }
}