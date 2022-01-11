package com.comerlato.voting_challenge.util.mapper;

import com.comerlato.voting_challenge.dto.AssociateDTO;
import com.comerlato.voting_challenge.dto.AssociateRequestDTO;
import com.comerlato.voting_challenge.modules.entity.Associate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AssociateMapper {

    @Mapping(target = "id", ignore = true)
    Associate buildAssociate(AssociateRequestDTO request);

    AssociateDTO buildAssociateDTO(Associate associate);
}
