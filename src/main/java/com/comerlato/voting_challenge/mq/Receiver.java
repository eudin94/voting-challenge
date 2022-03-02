package com.comerlato.voting_challenge.mq;

import com.comerlato.voting_challenge.service.ScheduleService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class Receiver {

    private final ScheduleService scheduleService;
    private static final String QUEUE_NAME = "SCHEDULE_QUEUE";

    @PostConstruct
    public void receive() {
        try {

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            log.info("O sistema de mensageria está aguardando novas mensagens.");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                handleMessage(message);
            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });

        } catch (IOException | TimeoutException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void handleMessage(String message) {
        log.info("Mensagem recebida relativa à pauta de ID: [" + message + "]");
        Try.run(() -> {
            final var closeDTO = scheduleService.closeSchedule(Long.parseLong(message));
            log.info("\nPauta encerrada: \n" + closeDTO.toString());
        }).onFailure(throwable -> log.error(throwable.getMessage()));
    }
}
