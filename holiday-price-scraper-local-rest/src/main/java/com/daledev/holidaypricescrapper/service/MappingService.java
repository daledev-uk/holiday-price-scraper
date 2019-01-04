package com.daledev.holidaypricescrapper.service;

import java.util.List;

/**
 * @author dale.ellis
 * @since 30/09/2018
 */
public interface MappingService {

    /**
     * @param source
     * @param destinationClass
     * @param <T>
     * @return
     */
    <T> T map(Object source, Class<T> destinationClass);

    /**
     * @param source
     * @param destinationClass
     * @param <T>
     * @return
     */
    <T> List<T> map(Iterable<?> source, Class<T> destinationClass);
}
