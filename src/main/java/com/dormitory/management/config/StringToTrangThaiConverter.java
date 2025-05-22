package com.dormitory.management.config;

import com.dormitory.management.model.HoaDon.TrangThai;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToTrangThaiConverter implements Converter<String, TrangThai> {
    @Override
    public TrangThai convert(String source) {
        try {
            return TrangThai.valueOf(source);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
} 