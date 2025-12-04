package com.example.fms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String middleName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "country_of_arrival")
    private String countryOfArrival;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "russian_language_certificate_id")
    private RussianLanguageCertificate russianLanguageCertificate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_patent_id")
    private WorkPatent workPatent;
}

