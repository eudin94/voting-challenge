package com.comerlato.voting_challenge.repository;

import com.comerlato.voting_challenge.entity.Associate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociateRepository extends JpaRepository<Associate, Long> {
}
