package com.comerlato.voting_challenge.creator;

import com.comerlato.voting_challenge.dto.ScheduleDTO;
import com.comerlato.voting_challenge.dto.ScheduleRequestDTO;
import com.comerlato.voting_challenge.dto.ScheduleResultsDTO;
import com.comerlato.voting_challenge.enums.VoteResultEnum;
import com.comerlato.voting_challenge.modules.entity.Schedule;
import com.comerlato.voting_challenge.modules.entity.ScheduleResults;

import static com.comerlato.voting_challenge.enums.VoteResultEnum.NO;
import static com.comerlato.voting_challenge.enums.VoteResultEnum.YES;
import static com.comerlato.voting_challenge.util.mapper.MapperConstants.scheduleMapper;
import static com.comerlato.voting_challenge.util.mapper.MapperConstants.scheduleResultsMapper;

public class ScheduleCreator {

    public static final ScheduleRequestDTO scheduleRequestDTO = createScheduleRequest();
    public static final Schedule schedule = scheduleMapper.buildSchedule(scheduleRequestDTO).withId(1L).withClosed(false);
    public static final ScheduleDTO scheduleDTO = scheduleMapper.buildScheduleDTO(schedule);
    public static final ScheduleResults resultsYes = createScheduleResults(YES);
    public static final ScheduleResults resultsNo = createScheduleResults(NO);
    public static final ScheduleResultsDTO resultsDTOYes = scheduleResultsMapper.buildScheduleResultsDTO(resultsYes);
    public static final ScheduleResultsDTO resultsDTONo = scheduleResultsMapper.buildScheduleResultsDTO(resultsNo);

    private static ScheduleRequestDTO createScheduleRequest() {
        return ScheduleRequestDTO.builder()
                .subject("Assunto")
                .durationInSeconds(60)
                .build();
    }

    private static ScheduleResults createScheduleResults(VoteResultEnum result) {
        return ScheduleResults.builder()
                .scheduleId(schedule.getId())
                .votedYes(YES.equals(result) ? 1L : 0L)
                .votedNo(NO.equals(result) ? 1L : 0L)
                .result(result)
                .build();
    }
}
