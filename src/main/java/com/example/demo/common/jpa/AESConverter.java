package com.example.demo.common.jpa;

import com.example.demo.utils.AES256;
import lombok.RequiredArgsConstructor;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@RequiredArgsConstructor
@Converter
public class AESConverter implements AttributeConverter<String, String> {
    private final AES256 aes256;
    @Override
    public String convertToDatabaseColumn(String attribute) {
        return aes256.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return aes256.decrypt(dbData);
    }
}
