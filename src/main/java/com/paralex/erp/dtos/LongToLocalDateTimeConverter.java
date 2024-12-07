package com.paralex.erp.dtos;

import org.springframework.core.convert.converter.Converter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LongToLocalDateTimeConverter implements Converter<Long, LocalDateTime> {

    @Override
    public LocalDateTime convert(Long source) {
        if (source == null) {
            return null;
        }
        return LocalDateTime.ofEpochSecond(source / 1000, 0, ZoneOffset.UTC);
    }
}
