package org.example.bookservice.utils;

import lombok.experimental.UtilityClass;
import org.example.bookservice.dto.exception.BusinessException;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateUtils {

    public static String convertLocalDateTimeToFormat(LocalDateTime dateTime, String format) {
        if(!StringUtils.hasText(format)){
            throw new BusinessException("Tham số format không được để trống", "FORMAT_NOT_BLANK");
        }
        java.time.format.DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(formatter);
    }
}
