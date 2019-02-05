package com.daledev.holidaypricescrapper.config;

import com.daledev.imgur4j.apiv3.ImageApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dale.ellis
 * @since 30/01/2019
 */
@Configuration
public class ImgurConfig {

    @Bean
    public ImageApiClient imageApiClient(@Value("${imgurClientId}") String clientId, @Value("${imgurClientSecret}") String clientSecret) {
        return new ImageApiClient(clientId, clientSecret);
    }

}
