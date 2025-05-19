package io.github.guoshiqiufeng.dify.examples.all;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author yanghq
 * @version 1.0
 * @since 2025/3/25 10:44
 */
@SpringBootApplication
public class ExamplesAllApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExamplesAllApplication.class, args);
    }

    /**
     * 日期时间 格式
     */
    String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期 格式
     */
    String PATTERN_DATE = "yyyy-MM-dd";
    /**
     * 时间格式
     */
    String PATTERN_TIME = "HH:mm";

    @Bean
    @ConditionalOnMissingBean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.simpleDateFormat(PATTERN_DATETIME);
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            builder.dateFormat(new SimpleDateFormat(PATTERN_DATETIME, Locale.CHINA));
            builder.featuresToEnable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
            builder.featuresToEnable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature());

            builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            builder.featuresToEnable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
            builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
            builder.serializerByType(Long.class, ToStringSerializer.instance);

            // LocalDateTime
            String dateTimePattern = PATTERN_DATETIME;
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(dateTimePattern);
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormat));
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormat));
            // LocalDate
            String datePattern = PATTERN_DATE;
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(datePattern);
            builder.serializerByType(LocalDate.class, new LocalDateSerializer(dateFormat));
            builder.deserializerByType(LocalDate.class, new LocalDateDeserializer(dateFormat));
            // LocalTime
            String timePattern = PATTERN_TIME;
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern(timePattern);
            builder.serializerByType(LocalTime.class, new LocalTimeSerializer(timeFormat));
            builder.deserializerByType(LocalTime.class, new LocalTimeDeserializer(timeFormat));

        };
    }

}
