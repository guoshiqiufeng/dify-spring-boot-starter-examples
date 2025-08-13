package io.github.guoshiqiufeng.dify.examples.date.config;

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
import io.github.guoshiqiufeng.dify.examples.date.PlatformJacksonConstant;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
 * @since 2021-11-08 16:38
 */
// @Configuration
public class JacksonConfiguration {


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.simpleDateFormat(PlatformJacksonConstant.PATTERN_DATETIME);

            // 设置为中国上海时区
            builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            // 序列化时，日期的统一格式
            builder.dateFormat(new SimpleDateFormat(PlatformJacksonConstant.PATTERN_DATETIME, Locale.CHINA));
            // 序列化处理
            builder.featuresToEnable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
            builder.featuresToEnable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature());

            // 失败处理
            builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            // 单引号处理
            builder.featuresToEnable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
            builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            // 序列化、反序列化设置

            // LocalDateTime
            String dateTimePattern = PlatformJacksonConstant.PATTERN_DATETIME;
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(dateTimePattern);
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormat));
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormat));
            // LocalDate
            String datePattern = PlatformJacksonConstant.PATTERN_DATE;
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(datePattern);
            builder.serializerByType(LocalDate.class, new LocalDateSerializer(dateFormat));
            builder.deserializerByType(LocalDate.class, new LocalDateDeserializer(dateFormat));
            // LocalTime
            String timePattern = PlatformJacksonConstant.PATTERN_TIME;
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern(timePattern);
            builder.serializerByType(LocalTime.class, new LocalTimeSerializer(timeFormat));
            builder.deserializerByType(LocalTime.class, new LocalTimeDeserializer(timeFormat));
        };
    }
}
