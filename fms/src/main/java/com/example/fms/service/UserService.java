package com.example.fms.service;

import com.example.fms.dto.UserProfileResponse;
import com.example.fms.dto.UserProfileUpdateRequest;
import com.example.fms.entity.User;
import com.example.fms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

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

    // Профиль считается заполненным, если указаны страна и дата прибытия
    response.setIsProfileComplete(
        user.getCountryOfArrival() != null &&
            user.getArrivalDate() != null);

    return response;
  }
}
