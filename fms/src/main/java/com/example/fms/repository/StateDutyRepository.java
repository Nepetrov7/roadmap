package com.example.fms.repository;

import com.example.fms.entity.StateDuty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateDutyRepository extends JpaRepository<StateDuty, Long> {
}

