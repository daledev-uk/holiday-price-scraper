package com.daledev.holidaypricescrapper.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dale.ellis
 * @since 11/01/2019
 */
@Configuration
public class AwsSesConfig {

    @Bean
    public AmazonSimpleEmailService client() {
        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(Regions.EU_WEST_1).build();
    }

}
