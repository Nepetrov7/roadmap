package com.example.fms.service;

import com.example.fms.dto.UserProfileResponse;
import com.example.fms.dto.UserProfileUpdateRequest;
import com.example.fms.entity.RussianLanguageCertificate;
import com.example.fms.entity.StateDuty;
import com.example.fms.entity.User;
import com.example.fms.entity.WorkPatent;
import com.example.fms.repository.RussianLanguageCertificateRepository;
import com.example.fms.repository.StateDutyRepository;
import com.example.fms.repository.UserRepository;
import com.example.fms.repository.WorkPatentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final RussianLanguageCertificateRepository certificateRepository;
  private final WorkPatentRepository workPatentRepository;
  private final StateDutyRepository stateDutyRepository;

  public User getOrCreateUser() {
    return userRepository.findAll().stream()
        .findFirst()
        .orElseGet(() -> {
          User newUser = new User();
          newUser.setFirstName("");
          newUser.setLastName("");
          newUser.setMiddleName("");
          newUser.setUsername("user");
          newUser.setPassword("");
          return userRepository.save(newUser);
        });
  }

  public UserProfileResponse getProfile() {
    User user = getOrCreateUser();
    return mapToProfileResponse(user);
  }

  @Transactional
  public UserProfileResponse updateProfile(UserProfileUpdateRequest request) {
    User user = getOrCreateUser();

    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setMiddleName(request.getMiddleName());
    user.setCountryOfArrival(request.getCountryOfArrival());
    user.setArrivalDate(request.getArrivalDate());

    // Обработка сертификата владения русским языком
    if (Boolean.TRUE.equals(request.getHasRussianLanguageCertificate())) {
      if (user.getRussianLanguageCertificate() == null) {
        RussianLanguageCertificate certificate = new RussianLanguageCertificate();
        certificate.setIssueDate(LocalDate.now());
        certificate.setExpiryDate(LocalDate.now().plusYears(5));
        certificate = certificateRepository.save(certificate);
        user.setRussianLanguageCertificate(certificate);
      }
    } else {
      if (user.getRussianLanguageCertificate() != null) {
        certificateRepository.delete(user.getRussianLanguageCertificate());
        user.setRussianLanguageCertificate(null);
      }
    }

    // Обработка патента на работу
    if (Boolean.TRUE.equals(request.getHasWorkPatent())) {
      if (user.getWorkPatent() == null) {
        WorkPatent patent = new WorkPatent();
        patent.setIssueDate(LocalDate.now());
        patent.setExpiryDate(LocalDate.now().plusYears(1));
        patent.setStateDutyPaid(Boolean.TRUE.equals(request.getHasPaidStateDuty()));
        patent = workPatentRepository.save(patent);
        user.setWorkPatent(patent);
      } else {
        user.getWorkPatent().setStateDutyPaid(Boolean.TRUE.equals(request.getHasPaidStateDuty()));
      }

      // Обработка госпошлины
      if (Boolean.TRUE.equals(request.getHasPaidStateDuty())) {
        WorkPatent patent = user.getWorkPatent();
        if (patent.getStateDuty() == null) {
          StateDuty duty = new StateDuty();
          duty.setPaymentDate(LocalDate.now());
          duty.setPaymentAmount(new BigDecimal("5000"));
          duty.setUser(user);
          duty = stateDutyRepository.save(duty);
          patent.setStateDuty(duty);
        }
      }
    } else {
      if (user.getWorkPatent() != null) {
        if (user.getWorkPatent().getStateDuty() != null) {
          stateDutyRepository.delete(user.getWorkPatent().getStateDuty());
        }
        workPatentRepository.delete(user.getWorkPatent());
        user.setWorkPatent(null);
      }
    }

    user = userRepository.save(user);
    return mapToProfileResponse(user);
  }

  private UserProfileResponse mapToProfileResponse(User user) {
    UserProfileResponse response = new UserProfileResponse();
    response.setId(user.getId());
    response.setFirstName(user.getFirstName());
    response.setLastName(user.getLastName());
    response.setMiddleName(user.getMiddleName());
    response.setUsername(user.getUsername());
    response.setCountryOfArrival(user.getCountryOfArrival());
    response.setArrivalDate(user.getArrivalDate());
    response.setHasRussianLanguageCertificate(user.getRussianLanguageCertificate() != null);
    response.setHasWorkPatent(user.getWorkPatent() != null);
    response.setHasPaidStateDuty(
        user.getWorkPatent() != null &&
            Boolean.TRUE.equals(user.getWorkPatent().getStateDutyPaid()));

    // Профиль считается заполненным, если указаны страна и дата прибытия
    response.setIsProfileComplete(
        user.getCountryOfArrival() != null &&
            user.getArrivalDate() != null);

    return response;
  }
}
