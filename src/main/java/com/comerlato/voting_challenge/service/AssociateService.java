package com.comerlato.voting_challenge.service;

import com.comerlato.voting_challenge.dto.AssociateDTO;
import com.comerlato.voting_challenge.dto.AssociateRequestDTO;
import com.comerlato.voting_challenge.helper.MessageHelper;
import com.comerlato.voting_challenge.modules.entity.Associate;
import com.comerlato.voting_challenge.modules.integration.cpf.CPFIntegration;
import com.comerlato.voting_challenge.modules.repository.AssociateRepository;
import com.comerlato.voting_challenge.modules.repository.specification.AssociateSpecification;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.comerlato.voting_challenge.exception.ErrorCodeEnum.*;
import static com.comerlato.voting_challenge.util.mapper.MapperConstants.associateMapper;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
@Slf4j
public class AssociateService {

    private final AssociateRepository repository;
    private final MessageHelper messageHelper;
    private final CPFIntegration cpfIntegration;

    public AssociateDTO create(final AssociateRequestDTO request) {
        validateCPF(request.getCpf());
        final var savedAssociate = repository.save(associateMapper.buildAssociate(request));
        return findDTOById(savedAssociate.getId());
    }

    public Page<AssociateDTO> findAll(final Optional<String> cpf, final Optional<String> name, final Pageable pageable) {
        return repository.findAll(AssociateSpecification.builder()
                .cpf(cpf)
                .name(name)
                .build(), pageable).map(associateMapper::buildAssociateDTO);
    }

    public AssociateDTO findDTOById(final Long id) {
        return associateMapper.buildAssociateDTO(findById(id));
    }

    public void delete(final Long id) {
        final var associate = findById(id);
        repository.delete(associate);
    }

    private void validateCPF(final String cpf) {
        Try.run(() -> cpfIntegration.validateCPF(cpf)).onFailure(throwable -> {
            log.error(throwable.getMessage());
            throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_INVALID_CPF));
        });
        final var existingAssociate = repository.findByCpf(cpf);
        if (existingAssociate.isPresent()) {
            log.error(messageHelper.get(ERROR_CPF_ALREADY_EXISTS));
            throw new ResponseStatusException(BAD_REQUEST, messageHelper.get(ERROR_CPF_ALREADY_EXISTS));
        }
    }

    private Associate findById(final Long id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error(messageHelper.get(ERROR_ASSOCIATE_NOT_FOUND));
            throw new ResponseStatusException(NOT_FOUND, messageHelper.get(ERROR_ASSOCIATE_NOT_FOUND));
        });
    }

}
