package com.comerlato.voting_challenge.resource;


import com.comerlato.voting_challenge.dto.AssociateDTO;
import com.comerlato.voting_challenge.dto.AssociateRequestDTO;
import com.comerlato.voting_challenge.service.AssociateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/associates")
@RequiredArgsConstructor
@Tag(name = "Associates")
public class AssociateResource {

    private final AssociateService service;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Create associate / Criar associado",
            responses = {@ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = AssociateDTO.class)))})
    public AssociateDTO create(@Valid @RequestBody AssociateRequestDTO request) {
        return service.create(request);
    }
}
