package io.github.guoshiqiufeng.dify.examples.dataset;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigInteger;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfiguration implements Jackson2ObjectMapperBuilderCustomizer, Ordered {
    @Override
    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        jacksonObjectMapperBuilder
                .timeZone("Asia/Shanghai")
                .serializers(
                        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(PlatformJacksonConstant.PATTERN_DATE)),
                        new LocalDateSerializer(DateTimeFormatter.ofPattern(PlatformJacksonConstant.PATTERN_DATE)),
                        new LocalTimeSerializer(DateTimeFormatter.ofPattern(PlatformJacksonConstant.PATTERN_DATETIME))
                )
                .deserializers(
                        new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(PlatformJacksonConstant.PATTERN_DATE)),
                        new LocalDateDeserializer(DateTimeFormatter.ofPattern(PlatformJacksonConstant.PATTERN_DATE)),
                        new LocalTimeDeserializer(DateTimeFormatter.ofPattern(PlatformJacksonConstant.PATTERN_DATETIME))
                )
                //Long 在前台超过 18 位 丢失精度 修改为只针对id
                .serializerByType(Long.TYPE, ToStringSerializer.instance)
                .serializerByType(Long.class, ToStringSerializer.instance)
                .serializerByType(BigInteger.class, ToStringSerializer.instance);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
