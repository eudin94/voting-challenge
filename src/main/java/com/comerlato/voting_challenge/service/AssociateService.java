package com.comerlato.voting_challenge.service;

import com.comerlato.voting_challenge.dto.AssociateDTO;
import com.comerlato.voting_challenge.dto.AssociateRequestDTO;
import com.comerlato.voting_challenge.entity.Associate;
import com.comerlato.voting_challenge.repository.AssociateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.comerlato.voting_challenge.util.mapper.MapperConstants.associateMapper;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
public class AssociateService {

    private final AssociateRepository repository;

    public AssociateDTO create(final AssociateRequestDTO request) {
        final var savedAssociate = repository.save(associateMapper.buildAssociate(request));
        return findDTOById(savedAssociate.getId());
    }

    public void delete(final Long id) {
        repository.deleteById(id);
    }

    public AssociateDTO findDTOById(final Long id) {
        return associateMapper.buildAssociateDTO(findById(id));
    }

    private Associate findById(final Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Associate not found" +
                " / Associado não encontrado"));
    }

}
