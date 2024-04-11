package com.snezana.introtelecom.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JsonDateTimeDeserializerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testDeserialize_valid_dateTime() throws IOException {
        LocalDateTime dateTime = LocalDateTime.of(2024, Month.JANUARY, 17, 15, 41, 23, 0);
        String jsonStr = "\"2024-01-17T15:41:23.000Z\""; // JSON string of the LocalDateTime field value
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new JsonDateTimeDeserializer());
        mapper.registerModule(module);
        LocalDateTime readValue = mapper.readValue(jsonStr, LocalDateTime.class);
        assertThat(readValue).isEqualTo(dateTime);
    }

    @Test
    void testDeserialize_notValid_dateTime() throws IOException {
        LocalDateTime dateTime = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
        String jsonStr = "\"2024-01-1715:41:23.000Z\""; // JSON string of the LocalDateTime field value
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new JsonDateTimeDeserializer());
        mapper.registerModule(module);
        LocalDateTime readValue = mapper.readValue(jsonStr, LocalDateTime.class);
        assertThat(readValue).isEqualTo(dateTime);
    }

    @Test
    void testDeserialize_notValid_dateTime2() throws IOException {
        LocalDateTime dateTime = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
        String jsonStr = "\"2024-01-35T15:41:23.000Z\""; // JSON string of the LocalDateTime field value
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new JsonDateTimeDeserializer());
        mapper.registerModule(module);
        LocalDateTime readValue = mapper.readValue(jsonStr, LocalDateTime.class);
        assertThat(readValue).isEqualTo(dateTime);
    }

}