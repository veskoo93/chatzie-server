package com.chatzie;

import static org.junit.jupiter.api.Assertions.*;
import static org.slf4j.LoggerFactory.getLogger;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private static final Logger logger = getLogger(AppTest.class);

    @Test
    public void shouldAnswerWithTrue() throws InterruptedException, ExecutionException
    {
        WebSocketClient client = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new StringMessageConverter());
        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter()
        {
            @Override
            public void handleFrame(StompHeaders headers, Object payload)
            {
                super.handleFrame(headers, payload);
                logger.info("Received frame:{}", payload);
            }

            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders)
            {
                logger.info("Connected!");
                super.afterConnected(session, connectedHeaders);

                logger.info("Subscribing for responses!");
                session.subscribe("/topic/messages", new StompFrameHandler()
                {
                    @Override
                    public Type getPayloadType(StompHeaders headers)
                    {
                        return String.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload)
                    {
                        logger.info("Received message:{}", payload);
                    }
                });
                logger.info("Subscribed for responses!");
                session.send("/app/input", "foo");
                logger.info("Sent!");
            }
        };
        ListenableFuture<StompSession> connectFuture = stompClient.connect("ws://localhost:8080/application", sessionHandler);
        connectFuture.addCallback(new ListenableFutureCallback<StompSession>()
        {
            @Override
            public void onFailure(Throwable throwable)
            {
                logger.error("Failed to connect!", throwable);
            }

            @Override
            public void onSuccess(StompSession stompSession)
            {
                logger.info("Connection future done!");
                stompSession.send("/app/input", "from outside");
            }
        });

        Thread.sleep(333_000);
    }
}
