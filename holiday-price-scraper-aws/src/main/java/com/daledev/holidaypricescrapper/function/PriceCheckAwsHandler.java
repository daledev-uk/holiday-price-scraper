package com.daledev.holidaypricescrapper.function;

import com.daledev.holidaypricescrapper.domain.PriceCheckResult;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

import java.util.List;

/**
 * @author dale.ellis
 * @since 03/01/2019
 */
public class PriceCheckAwsHandler extends SpringBootRequestHandler<String, List<PriceCheckResult>> {
}
