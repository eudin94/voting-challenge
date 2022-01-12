package com.comerlato.voting_challenge.mq;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import zmq.ZMQ;

import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageSender {

    @Value("${zeromq.port.sender}")
    private String portSender;

    @Async
    public void sendMessage(final String message, final Integer sleepDurationInSeconds) {
        try (var context = new ZContext()) {
            Thread.sleep(TimeUnit.SECONDS.toMillis(sleepDurationInSeconds));
            log.info("Enviando mensagem para encerrar a pauta de id: [ " + message + " ]");
            var socket = context.createSocket(SocketType.REQ);
            socket.connect(portSender);
            while (!Thread.currentThread().isInterrupted()) {
                log.info("Enviando id: [ " + message + " ]");
                socket.send(message.getBytes(ZMQ.CHARSET), 0);
                var reply = new String(socket.recv(0), ZMQ.CHARSET);
                log.info(reply);
                break;
            }
            socket.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
