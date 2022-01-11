package com.comerlato.voting_challenge.repository;

import com.comerlato.voting_challenge.entity.Vote;
import com.comerlato.voting_challenge.enums.VoteAnswerEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByScheduleIdAndAssociateId(Long scheduleId, Long associateId);

    Long countByAnswerAndScheduleId(VoteAnswerEnum answer, Long scheduleId);
}
