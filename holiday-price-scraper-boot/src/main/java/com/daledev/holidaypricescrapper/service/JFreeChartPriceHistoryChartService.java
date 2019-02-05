package com.daledev.holidaypricescrapper.service;

import com.daledev.holidaypricescrapper.domain.PriceHistory;
import com.daledev.holidaypricescrapper.domain.PriceSnapshot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.XYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author dale.ellis
 * @since 13/01/2019
 */
@Service
public class JFreeChartPriceHistoryChartService implements PriceHistoryChartService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public byte[] getLineChart(PriceHistory priceHistory) {
        log.debug("Generating line chart");
        JFreeChart lineChart = ChartFactory.createLineChart(
                priceHistory.getCriterion().getDescription(),
                "Date", "Price",
                createDataSet(priceHistory),
                PlotOrientation.VERTICAL,
                false, true, false);

        setYAxisRange(priceHistory, lineChart);

        JFreeChart timeSeriesChart = ChartFactory.createTimeSeriesChart(
                priceHistory.getCriterion().getDescription(),
                "Date", "Price",
                createTimeSeriesDataSet(priceHistory),
                false, true, false);

        try {
            try (ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream()) {
                try {
                    ChartUtils.writeChartAsJPEG(byteOutputStream, lineChart, 800, 300);
                    return byteOutputStream.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (IOException e) {
            return new byte[0];
        }
    }

    private XYDataset createTimeSeriesDataSet(PriceHistory priceHistory) {
        log.debug("Creating data-set for line chart");
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        for (PriceSnapshot priceSnapshot : priceHistory.getHistory()) {
            //new TimeSeries("Holiday").add(new TimeSeriesDataItem(new ));
            //dataSet.addSeries(new TimeSeries("Holiday"));
            //dataSet.addValue(priceSnapshot.getPrice(), "Holiday", priceSnapshot.getFirstRetrievedDate());
            //dataSet.addValue(priceSnapshot.getPrice(), "Holiday", priceSnapshot.getLastRetrievedDate());
        }

        return dataSet;
    }

    private DefaultCategoryDataset createDataSet(PriceHistory priceHistory) {
        log.debug("Creating data-set for line chart");
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        for (PriceSnapshot priceSnapshot : priceHistory.getHistory()) {
            dataSet.addValue(priceSnapshot.getPrice(), "Holiday", priceSnapshot.getFirstRetrievedDate());
            dataSet.addValue(priceSnapshot.getPrice(), "Holiday", priceSnapshot.getLastRetrievedDate());
        }

        return dataSet;
    }

    private void setYAxisRange(PriceHistory priceHistory, JFreeChart lineChart) {
        PriceSnapshot cheapestRecorded = priceHistory.getPriceWhenCheapest();
        PriceSnapshot mostExpensiveRecorded = priceHistory.getPriceWhenMostExpensive();
        log.debug("cheapestRecorded : ({}), mostExpensiveRecorded : ({})", cheapestRecorded, mostExpensiveRecorded);

        double minYValue = cheapestRecorded == null ? 0d : cheapestRecorded.getPrice();
        double maxYValue = mostExpensiveRecorded == null ? 10000d : mostExpensiveRecorded.getPrice();
        log.debug("minYValue : ({}), maxYValue : ({})", minYValue, maxYValue);

        Range yRange = new Range(minYValue - 2, maxYValue + 2);
        log.debug("yRange : {}", yRange);

        CategoryPlot plot = lineChart.getCategoryPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(yRange);
    }
}
