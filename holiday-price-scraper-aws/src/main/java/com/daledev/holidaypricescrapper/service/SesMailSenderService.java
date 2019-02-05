package com.daledev.holidaypricescrapper.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author dale.ellis
 * @since 11/01/2019
 */
@Service
public class SesMailSenderService implements MailSenderService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private AmazonSimpleEmailService client;

    /**
     * @param client
     */
    public SesMailSenderService(AmazonSimpleEmailService client) {
        this.client = client;
    }

    @Override
    public void sendEmail(String to, String subject, String html) {
        log.debug("Attempting to send email via Amazon SES client : {}", client);

        SendEmailRequest request = new SendEmailRequest();
        request.setSource("Holiday Price Scraper <daledev.pricescraper@gmail.com>");
        request.setDestination(new Destination(Collections.singletonList(to)));
        request.setMessage(createEmailMessage(subject, html));
        log.debug("Send email request : {}", request);

        client.sendEmail(request);
    }

    private Message createEmailMessage(String subject, String html) {
        return new Message()
                .withSubject(new Content().withCharset("UTF-8").withData(subject))
                .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(html)));
    }
}
