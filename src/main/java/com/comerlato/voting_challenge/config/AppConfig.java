package com.comerlato.voting_challenge.config;

import com.comerlato.voting_challenge.helper.MessageHelper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
//@EnableFeignClients(basePackages = {"com.comerlato.voting_challenge.modules.integration"})
public class AppConfig {

    @Bean
    MessageHelper messageHelper(MessageSource messageSource) {
        return new MessageHelper(messageSource);
    }

    @Bean
    MessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasenames("classpath:i18n/messages");
        source.setCacheSeconds(3600);
        source.setDefaultEncoding("UTF-8");
        return source;
    }

//    @Bean
//    @Primary
//    public ObjectMapper mapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        JavaTimeModule timeModule = new JavaTimeModule();
//        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)));
//        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMATTER)));
//        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)));
//        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_FORMATTER)));
//        mapper.setDateFormat(new SimpleDateFormat(DATE_TIME_FORMATTER));
//        mapper.setTimeZone(TimeZone.getTimeZone(ZoneId.of("America/Sao_Paulo")));
//        mapper.setDefaultPropertyInclusion(NON_ABSENT);
//        mapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
//        mapper.disable(WRITE_DATES_AS_TIMESTAMPS);
//        mapper.registerModule(timeModule).registerModule(new ParameterNamesModule()).registerModules(ObjectMapper.findModules());
//        return mapper;
//    }
}
