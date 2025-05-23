package com.dormitory.management.repository;

import com.dormitory.management.model.MomoTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MomoTransactionRepository extends JpaRepository<MomoTransaction, Long> {
} 