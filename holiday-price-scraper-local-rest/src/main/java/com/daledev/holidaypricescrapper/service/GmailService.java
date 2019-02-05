package com.daledev.holidaypricescrapper.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * @author dale.ellis
 * @since 01/01/2019
 */
@Service
public class GmailService implements MailSenderService {
    private JavaMailSender javaMailSender;

    public GmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String html) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setFrom("daledev.pricescraper@gmail.com", "Holiday Price Scraper");
            helper.setSubject(subject);
            helper.setText(html, true);
            helper.setValidateAddresses(false);

            javaMailSender.send(mimeMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
