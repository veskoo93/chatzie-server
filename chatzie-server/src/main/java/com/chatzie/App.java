package com.chatzie;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@SpringBootApplication()
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.chatzie.*")
)
@EnableWebSocketMessageBroker
public class App
{
    private static final Logger logger = getLogger(App.class);

    public static void main( String[] args )
    {
        SpringApplication.run(App.class);
    }

    @Bean
    public static WebSocketMessageBrokerConfigurer webSocketMessageBrokerConfigurer() {
        return new WebSocketConfig();
    }

    @Bean
    public static ChatController chatController() {
        return new ChatController();
    }
}
