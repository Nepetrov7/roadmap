package com.example.fms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "work_patents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkPatent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "state_duty_paid", nullable = false)
    private Boolean stateDutyPaid = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_duty_id")
    private StateDuty stateDuty;
}

