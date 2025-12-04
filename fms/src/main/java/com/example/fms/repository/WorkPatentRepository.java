package com.example.fms.repository;

import com.example.fms.entity.WorkPatent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkPatentRepository extends JpaRepository<WorkPatent, Long> {
}

