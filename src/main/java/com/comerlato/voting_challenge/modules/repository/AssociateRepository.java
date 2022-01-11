package com.comerlato.voting_challenge.modules.repository;

import com.comerlato.voting_challenge.modules.entity.Associate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssociateRepository extends JpaRepository<Associate, Long>, JpaSpecificationExecutor<Associate> {

    Optional<Associate> findByCpf(String cpf);
}
