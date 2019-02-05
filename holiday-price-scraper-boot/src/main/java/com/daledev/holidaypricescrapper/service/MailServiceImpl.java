package com.daledev.holidaypricescrapper.service;

import com.daledev.holidaypricescrapper.domain.PriceHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dale.ellis
 * @since 27/01/2019
 */
@Service
public class MailServiceImpl implements MailService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private MailSenderService mailSenderService;
    private TemplateService templateService;

    /**
     * @param mailSenderService
     * @param templateService
     */
    public MailServiceImpl(MailSenderService mailSenderService, TemplateService templateService) {
        this.mailSenderService = mailSenderService;
        this.templateService = templateService;
    }

    @Override
    public void sendPriceDropMail(PriceHistory priceHistory) {
        log.debug("Sending price dropped alert, cheapest recorded price : {}", priceHistory.isCurrentPriceCheapestCaptured());
        String htmlContent = templateService.getPriceDroppedContent(priceHistory);
        sendEmail("Price Drop", htmlContent, priceHistory.getSubscribers());
    }

    @Override
    public void sendPriceIncreaseMail(PriceHistory priceHistory) {
        log.debug("Sending price increased alert");
        String htmlContent = templateService.getPriceIncreaseContent(priceHistory);
        sendEmail("Price Increase", htmlContent, priceHistory.getSubscribers());
    }

    private void sendEmail(String subject, String content, List<String> emailAddresses) {
        log.debug("Calling mail service to perform send");
        emailAddresses.forEach(email -> mailSenderService.sendEmail(email, subject, content));
    }
}
