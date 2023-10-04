package com.snezana.introtelecom.validations;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.snezana.introtelecom.exceptions.IllegalItemFieldException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.services.AdminCustomerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class JsonDateTimeDeserializer  extends JsonDeserializer <LocalDateTime> {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JsonDateTimeDeserializer.class);

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, IllegalItemFieldException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        String dateString = jsonParser.getText();
        try {
            return LocalDateTime.parse(dateString, formatter);
        } catch (Exception e) {
            log.info("print e message: {}", e.getMessage());
            LocalDateTime dateTime = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
            return dateTime;
        }
    }
}
