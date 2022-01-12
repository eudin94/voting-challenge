package com.comerlato.voting_challenge.modules.integration.cpf;

import com.comerlato.voting_challenge.modules.integration.config.FeignIntegrationConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.LinkedHashMap;


@FeignClient(name = "CPFIntegration",
        url = "${api.cpf.uri}",
        configuration = {FeignIntegrationConfig.class})
public interface CPFIntegration {

    @GetMapping("/{cpf}")
    LinkedHashMap<String, String> validateCPF(@PathVariable String cpf);
}
