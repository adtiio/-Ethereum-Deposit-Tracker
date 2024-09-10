package com.ethreumTracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ethreumTracker.model.Deposit;

public interface DepositRepository extends JpaRepository<Deposit,Long>{
    Optional<Deposit> findByHash(String txHash);
    boolean existsByBlockNumber(Long blockNumber);
}
