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

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/associates")
@RequiredArgsConstructor
@Tag(name = "Associates")
public class AssociateResource {

    private final AssociateService service;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Criar associado",
            responses = {@ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = AssociateDTO.class)))})
    public AssociateDTO create(@Valid @RequestBody AssociateRequestDTO request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @Operation(summary = "Encontrar associado pelo id",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AssociateDTO.class)))})
    public AssociateDTO findDTOById(@Valid @PathVariable Long id) {
        return service.findDTOById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Deletar associado pelo id",
            responses = {@ApiResponse(responseCode = "204", content = @Content(schema = @Schema(implementation = AssociateDTO.class)))})
    public void delete(@Valid @PathVariable Long id) {
        service.delete(id);
    }
}