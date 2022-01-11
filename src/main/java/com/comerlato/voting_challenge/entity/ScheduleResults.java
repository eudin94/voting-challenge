package com.comerlato.voting_challenge.entity;

import com.comerlato.voting_challenge.enums.VoteResultEnum;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.EnumType.STRING;

@Entity
@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "voting_results")
public class ScheduleResults {

    @Id
    private Long scheduleId;
    private Long votedYes;
    private Long votedNo;
    @Enumerated(STRING)
    private VoteResultEnum result;
}
