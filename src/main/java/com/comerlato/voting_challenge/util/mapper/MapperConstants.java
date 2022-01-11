package com.comerlato.voting_challenge.util.mapper;

import org.mapstruct.factory.Mappers;

public class MapperConstants {

    private MapperConstants() {
    }

    public static final AssociateMapper associateMapper = Mappers.getMapper(AssociateMapper.class);
    public static final ScheduleMapper scheduleMapper = Mappers.getMapper(ScheduleMapper.class);
    public static final VoteMapper voteMapper = Mappers.getMapper(VoteMapper.class);
}
