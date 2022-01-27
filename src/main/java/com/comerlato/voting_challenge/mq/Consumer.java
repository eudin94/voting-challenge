package com.comerlato.voting_challenge.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(value = "example.kafka.consumer-enabled", havingValue = "true")
public class Consumer {

    @KafkaListener(topics = {"INPUT_DATA"})
    public void consume(final @Payload String message,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long ts,
                        final Acknowledgment acknowledgment) {
        log.info(String.format(
                "#### -> Consumed message -> TIMESTAMP: %d" +
                "\n%s" +
                "\noffset: %d" +
                "\nkey: %s" +
                "\npartition: %d" +
                "\ntopic: %s", ts, message, offset, key, partition, topic));
    }
}
