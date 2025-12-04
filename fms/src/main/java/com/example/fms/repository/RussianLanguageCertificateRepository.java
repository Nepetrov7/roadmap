package com.example.fms.repository;

import com.example.fms.entity.RussianLanguageCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RussianLanguageCertificateRepository extends JpaRepository<RussianLanguageCertificate, Long> {
}
