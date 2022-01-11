package com.comerlato.voting_challenge.util.mapper;

import com.comerlato.voting_challenge.dto.ScheduleDTO;
import com.comerlato.voting_challenge.dto.ScheduleRequestDTO;
import com.comerlato.voting_challenge.modules.entity.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ScheduleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "closed", ignore = true)
    Schedule buildSchedule(ScheduleRequestDTO request);

    ScheduleDTO buildScheduleDTO(Schedule schedule);
}
