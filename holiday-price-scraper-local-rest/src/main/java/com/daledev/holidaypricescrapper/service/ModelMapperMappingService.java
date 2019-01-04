package com.daledev.holidaypricescrapper.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dale.ellis
 * @since 30/09/2018
 */
@Service
public class ModelMapperMappingService implements MappingService {
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public <T> T map(Object source, Class<T> destinationClass) {
        return modelMapper.map(source, destinationClass);
    }

    @Override
    public <T> List<T> map(Iterable<?> source, Class<T> destinationClass) {
        List<T> mappedObjects = new ArrayList<>();
        for (Object inObject : source) {
            mappedObjects.add(map(inObject, destinationClass));
        }
        return mappedObjects;
    }
}
