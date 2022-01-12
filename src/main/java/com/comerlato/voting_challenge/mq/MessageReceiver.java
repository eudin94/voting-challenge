package com.comerlato.voting_challenge.mq;

import com.comerlato.voting_challenge.service.ScheduleService;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageReceiver implements CommandLineRunner {

    private final ScheduleService scheduleService;

    @Value("${zeromq.port.receiver}")
    private String portReceiver;

    @Override
    public void run(String... args) {
        receive();
    }

    @Async
    public void receive() {
        try (var context = new ZContext()) {
            var socket = context.createSocket(SocketType.REP);
            socket.bind(portReceiver);
            while (!Thread.currentThread().isInterrupted()) {
                var incoming = new String(socket.recv(0), ZMQ.CHARSET);
                final var response = "\nMensagem recebida: [ " + incoming + " ]";
                handleMessage(incoming);
                socket.send(response.getBytes(ZMQ.CHARSET), 0);
            }
            socket.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void handleMessage(String message) {
        Try.run(() -> {
            final var closeDTO = scheduleService.closeSchedule(Long.parseLong(message));
            log.info("Pauta encerrada: \n" + closeDTO.toString());
        }).onFailure(throwable -> log.error(throwable.getMessage()));
    }
}
