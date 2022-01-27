package com.comerlato.voting_challenge.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class SendMessageTask {

    private final Producer producer;

    public SendMessageTask(Producer producer) {
        this.producer = producer;
    }

    @Scheduled(fixedRateString = "10000")
    public void send() throws ExecutionException, InterruptedException {
        final var listenableFuture = this.producer.sendMessage("INPUT_DATA",
                "IN_KEY", LocalDate.now().toString());
        final var result = listenableFuture.get();
        log.info(String.format(
                "Produced:" +
                        "\ntopic: %s" +
                        "\noffset: %d" +
                        "\npartition: %d" +
                        "\nvalue size: %d",
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().offset(),
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().serializedValueSize()));
    }
}
