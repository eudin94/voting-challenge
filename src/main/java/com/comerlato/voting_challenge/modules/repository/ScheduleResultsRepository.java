package com.comerlato.voting_challenge.modules.repository;

import com.comerlato.voting_challenge.modules.entity.ScheduleResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleResultsRepository extends JpaRepository<ScheduleResults, Long> {

}
