package com.comerlato.voting_challenge.util.mapper;

import com.comerlato.voting_challenge.dto.ScheduleResultsDTO;
import com.comerlato.voting_challenge.modules.entity.ScheduleResults;
import org.mapstruct.Mapper;

@Mapper
public interface ScheduleResultsMapper {

    ScheduleResultsDTO buildScheduleResultsDTO(ScheduleResults scheduleResults);
}
