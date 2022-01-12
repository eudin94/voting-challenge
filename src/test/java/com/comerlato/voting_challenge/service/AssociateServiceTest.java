package com.comerlato.voting_challenge.service;

import com.comerlato.voting_challenge.helper.MessageHelper;
import com.comerlato.voting_challenge.modules.entity.Associate;
import com.comerlato.voting_challenge.modules.repository.AssociateRepository;
import com.comerlato.voting_challenge.modules.repository.specification.AssociateSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.comerlato.voting_challenge.creator.AssociateCreator.*;
import static com.comerlato.voting_challenge.util.mapper.MapperConstants.associateMapper;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(SpringExtension.class)
public class AssociateServiceTest {

    @InjectMocks
    private AssociateService service;
    @Mock
    private AssociateRepository repository;
    @Mock
    private MessageHelper messageHelper;

    @Test
    void create_returnsAssociateDTO_whenSuccessful() {
        when(repository.findByCpf(associate.getCpf())).thenReturn(empty());
        when(repository.save(associate.withId(null))).thenReturn(associate);
        when(repository.findById(associate.getId())).thenReturn(Optional.of(associate));
        assertEquals(associateDTO, service.create(associateRequestDTO));
    }

    @Test
    void create_returns400_whenCPFAlreadyUsed() {
        when(repository.findByCpf(associate.getCpf())).thenReturn(Optional.of(associate));
        final var status = assertThrows(ResponseStatusException.class,
                () -> service.create(associateRequestDTO)).getStatus();
        assertEquals(BAD_REQUEST, status);
    }

    @Test
    void findAll_returnsPageOfDTOs_whenSuccessful() {
        final var page = new PageImpl<Associate>(List.of(associate));
        final var assertion = page.map(associateMapper::buildAssociateDTO);
        final var pageable = PageRequest.of(0, 10, Sort.by(ASC, "id"));
        when(repository.findAll(any(AssociateSpecification.class), any(Pageable.class))).thenReturn(page);
        assertEquals(assertion, service.findAll(empty(), empty(), pageable));
    }

    @Test
    void findDTOById_throws404_whenAssociateNotFound() {
        when(repository.findById(associate.getId())).thenReturn(empty());
        final var status = assertThrows(ResponseStatusException.class,
                () -> service.findDTOById(associate.getId())).getStatus();
        assertEquals(NOT_FOUND, status);
    }

    @Test
    void delete_removesEntity_whenSuccessful() {
        when(repository.findById(associate.getId())).thenReturn(Optional.of(associate));
        service.delete(associate.getId());
        verify(repository, times(1)).delete(associate);
    }

}
