package com.daledev.holidaypricescrapper.service;

/**
 * @author dale.ellis
 * @since 01/01/2019
 */
public interface MailSenderService {
    /**
     *  @param to
     * @param subject
     * @param html
     */
    void sendEmail(String to, String subject, String html);
}
