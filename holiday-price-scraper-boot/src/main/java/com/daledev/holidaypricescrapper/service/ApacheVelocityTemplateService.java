package com.daledev.holidaypricescrapper.service;

import com.daledev.holidaypricescrapper.domain.PriceHistory;
import com.daledev.imgur4j.apiv3.ImageApiClient;
import com.daledev.imgur4j.apiv3.model.ImageUpload;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.tools.generic.DateTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.StringWriter;

/**
 * @author dale.ellis
 * @since 27/01/2019
 */
@Service
public class ApacheVelocityTemplateService implements TemplateService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String INCREASED_TEMPLATE = "static/price-increase.vm";
    private static final String DROPPED_TEMPLATE = "static/price-drop.vm";

    private PriceHistoryChartService priceHistoryChartService;
    private ImageApiClient imgurImageClient;
    private VelocityEngine velocityEngine;

    /**
     * @param priceHistoryChartService
     * @param imgurImageClient
     */
    public ApacheVelocityTemplateService(PriceHistoryChartService priceHistoryChartService, ImageApiClient imgurImageClient) {
        this.priceHistoryChartService = priceHistoryChartService;
        this.imgurImageClient = imgurImageClient;
    }

    @PostConstruct
    private void init() {
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
    }

    @Override
    public String getPriceIncreaseContent(PriceHistory priceHistory) {
        return getProcessedContent(priceHistory, INCREASED_TEMPLATE);
    }

    @Override
    public String getPriceDroppedContent(PriceHistory priceHistory) {
        return getProcessedContent(priceHistory, DROPPED_TEMPLATE);
    }

    private String getProcessedContent(PriceHistory priceHistory, String templateName) {
        log.debug("Fetching template : {}", templateName);
        Template template = velocityEngine.getTemplate(templateName);
        VelocityContext context = createVelocityContext(priceHistory);

        log.debug("Merging template content");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        return writer.toString();
    }

    private VelocityContext createVelocityContext(PriceHistory priceHistory) {
        VelocityContext context = new VelocityContext();
        context.put("data", priceHistory);
        context.put("date", new DateTool());
        context.put("reportImageSrc", getPriceGraphImageSrc(priceHistory));
        return context;
    }

    private String getPriceGraphImageSrc(PriceHistory priceHistory) {
        // Upload img to imgur, maybe create project
        log.debug("Creating line chart from historic data");
        byte[] content = priceHistoryChartService.getLineChart(priceHistory);

        log.debug("Uploading image to Imgur");
        ImageUpload imgurImage = imgurImageClient.uploadImage(content);

        log.debug("Image in Imgur at URL : {}", imgurImage.getData().getLink());
        return imgurImage.getData().getLink();
    }
}
