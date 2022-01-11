package com.comerlato.voting_challenge.modules.entity;

import com.comerlato.voting_challenge.enums.VoteAnswerEnum;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Entity
@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vote")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long scheduleId;
    private Long associateId;
    @Enumerated(STRING)
    private VoteAnswerEnum answer;
}
