package com.chatzie;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController
{
    private static final Logger logger = getLogger(ChatController.class);

    @MessageMapping("/input")
    @SendTo("/topic/messages")
    public String foo(String message)
    {
        logger.info("Got a message:{}", message);
        return "Received " + message;
    }
}
