package com.comerlato.voting_challenge.modules.integration.config;


import com.comerlato.voting_challenge.helper.MessageHelper;
import com.comerlato.voting_challenge.modules.integration.exception.IntegrationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import feign.Request;
import feign.Response;
import feign.RetryableException;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.vavr.API;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.PageJacksonModule;
import org.springframework.cloud.openfeign.support.SortJacksonModule;
import org.springframework.context.annotation.Bean;
import org.springframework.data.util.Lazy;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static com.comerlato.voting_challenge.exception.ErrorCodeEnum.ERROR_INVALID_CPF;
import static feign.Request.HttpMethod.GET;
import static io.vavr.API.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RequiredArgsConstructor
public class FeignIntegrationConfig {

    private final MessageHelper messageHelper;

    @Bean
    ErrorDecoder errorDecoder() {
        return (s, response) -> {
            Optional<HttpStatus> responseStatus = Optional.of(HttpStatus.valueOf(response.status()));
            logHttpError(response, responseStatus);
            var errorMessage = Lazy.of(messageHelper.get(ERROR_INVALID_CPF));
            return Match(response).of(
                    Case($(res -> GET.equals(res.request().httpMethod()) && HttpStatus.valueOf(res.status()).is5xxServerError()),
                            new RetryableException(
                                    responseStatus.map(HttpStatus::value).orElseThrow(),
                                    errorMessage.orElse(response.reason()),
                                    response.request().httpMethod(),
                                    null,
                                    response.request())),
                    API.Case($(), new IntegrationException(errorMessage.orElse(response.reason()), responseStatus.orElseThrow())));
        };
    }

    private void logHttpError(Response response, Optional<HttpStatus> responseStatus) {
        responseStatus.ifPresent(status -> {
            Request request = response.request();
            String responseBody = Try.withResources(() -> response.body().asReader(UTF_8))
                    .of(IOUtils::toString)
                    .getOrElse(EMPTY);
            if (BAD_REQUEST.equals(status)) {
                log.warn(buildLogFullMessage(),
                        request.httpMethod() + " " + request.url(),
                        new String(ofNullable(request.body()).orElse(EMPTY.getBytes()), UTF_8),
                        responseBody);
            } else if (status.is5xxServerError()) {
                log.error(buildLogFullMessage(),
                        request.httpMethod() + " " + request.url(),
                        new String(ofNullable(request.body()).orElse(EMPTY.getBytes()), UTF_8),
                        responseBody);
            }
        });
    }

    private String buildLogFullMessage() {
        return System.lineSeparator() +
                "RequestUrl: {}" + System.lineSeparator() +
                "RequestBody: {}" + System.lineSeparator() +
                "Response: {}" + System.lineSeparator();
    }

    @Bean
    PageJacksonModule pageJacksonModule() {
        return new PageJacksonModule();
    }

    @Bean
    SortJacksonModule sortJacksonModule() {
        return new SortJacksonModule();
    }

    @Bean
    Jdk8Module jdk8Module() {
        return new Jdk8Module();
    }

    @Bean
    Encoder jacksonEncoder(PageJacksonModule pageModule, SortJacksonModule sortJacksonModule, Jdk8Module jdk8Module, ObjectMapper mapper) {
        mapper.registerModules(pageModule, sortJacksonModule, jdk8Module);
        return new JacksonEncoder(mapper);
    }

    @Bean
    Decoder jacksonDecoder(PageJacksonModule pageModule, SortJacksonModule sortJacksonModule, Jdk8Module jdk8Module, ObjectMapper mapper) {
        mapper.registerModules(pageModule, sortJacksonModule, jdk8Module);
        return new JacksonDecoder(mapper);
    }

    @Bean
    Retryer retryer(@Value("${feign.client.retryer.period:1000}") long period,
                    @Value("${feign.client.retryer.max-period:5000}") long maxPeriod,
                    @Value("${feign.client.retryer.max-attempts:3}") int maxAttempts) {
        return new CustomRetryer(period, maxPeriod, maxAttempts);
    }

}
