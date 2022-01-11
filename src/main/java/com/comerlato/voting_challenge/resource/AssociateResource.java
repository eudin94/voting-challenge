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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Optional;

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

    @GetMapping
    @ResponseStatus(OK)
    @Operation(summary = "Listar associados",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AssociateDTO[].class)))})
    public Page<AssociateDTO> findAll(@RequestParam(required = false)Optional<String> cpf,
                                      @RequestParam(required = false)Optional<String> name,
                                      @RequestParam(defaultValue = "0") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      @RequestParam(defaultValue = "id") String sort,
                                      @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        return service.findAll(cpf, name, PageRequest.of(page, size, Sort.by(direction, sort)));
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