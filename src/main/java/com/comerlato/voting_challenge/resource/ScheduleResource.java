package com.comerlato.voting_challenge.resource;

import com.comerlato.voting_challenge.dto.*;
import com.comerlato.voting_challenge.service.ScheduleService;
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

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedules")
public class ScheduleResource {

    private final ScheduleService service;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Criar pauta",
            responses = {@ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = ScheduleDTO.class)))})
    public ScheduleDTO create(@Valid @RequestBody ScheduleRequestDTO request) {
        return service.create(request);
    }

    @GetMapping
    @ResponseStatus(OK)
    @Operation(summary = "Listar pautas",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ScheduleDTO[].class)))})
    public Page<ScheduleDTO> findAll(@RequestParam(required = false) Optional<String> subject,
                                     @RequestParam(required = false) Optional<Boolean> closed,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(defaultValue = "id") String sort,
                                     @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        return service.findAll(subject, closed, PageRequest.of(page, size, Sort.by(direction, sort)));
    }

    @PostMapping("/vote")
    @ResponseStatus(CREATED)
    @Operation(summary = "Votar em pauta",
            responses = {@ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = VoteDTO.class)))})
    public VoteDTO voteForSchedule(@Valid @RequestBody VoteRequestDTO request) {
        return service.voteForSchedule(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @Operation(summary = "Encontrar pauta pelo id",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ScheduleDTO.class)))})
    public ScheduleDTO findDTOById(@Valid @PathVariable Long id) {
        return service.findDTOById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(OK)
    @Operation(summary = "Encerrar pauta pelo id",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ScheduleResultsDTO.class)))})
    public ScheduleResultsDTO closeSchedule(@Valid @PathVariable Long id) {
        return service.closeSchedule(id);
    }

    @GetMapping("/{id}/results")
    @ResponseStatus(OK)
    @Operation(summary = "Encontrar resultado de pauta encerrada pelo id",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ScheduleResultsDTO.class)))})
    public ScheduleResultsDTO showScheduleResults(@Valid @PathVariable Long id) {
        return service.findScheduleResultsDTOById(id);
    }
}
