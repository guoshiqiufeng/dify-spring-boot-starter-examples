package io.github.guoshiqiufeng.dify.examples.chat;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author yanghq
 * @version 1.0
 * @since 2025/3/25 10:44
 */
@SpringBootApplication
public class ExamplesChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExamplesChatApplication.class, args);
    }


}
