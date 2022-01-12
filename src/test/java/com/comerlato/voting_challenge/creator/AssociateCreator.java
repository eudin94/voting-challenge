package com.comerlato.voting_challenge.creator;

import com.comerlato.voting_challenge.dto.AssociateDTO;
import com.comerlato.voting_challenge.dto.AssociateRequestDTO;
import com.comerlato.voting_challenge.modules.entity.Associate;

import static com.comerlato.voting_challenge.util.mapper.MapperConstants.associateMapper;

public class AssociateCreator {

    public static final AssociateRequestDTO associateRequestDTO = createAssociateRequest();
    public static final Associate associate = associateMapper.buildAssociate(associateRequestDTO).withId(1L);
    public static final AssociateDTO associateDTO = associateMapper.buildAssociateDTO(associate);

    private static AssociateRequestDTO createAssociateRequest() {
        return AssociateRequestDTO.builder()
                .cpf("10493883053")
                .name("Associado da Silva")
                .build();
    }
}
