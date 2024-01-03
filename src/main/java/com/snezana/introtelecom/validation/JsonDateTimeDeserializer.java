package com.snezana.introtelecom.validation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.snezana.introtelecom.exception.IllegalItemFieldException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Month;

/* used for LocalDateTime input data validation */
public class JsonDateTimeDeserializer  extends JsonDeserializer <LocalDateTime> {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, IllegalItemFieldException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        String dateString = jsonParser.getText();
        try {
            return LocalDateTime.parse(dateString, formatter);
        } catch (Exception e) {
            LocalDateTime dateTime = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
            return dateTime;
        }
    }
}
