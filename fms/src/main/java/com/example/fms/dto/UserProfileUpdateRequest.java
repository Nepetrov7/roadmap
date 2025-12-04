package com.example.fms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateRequest {
  @NotBlank(message = "Имя обязательно")
  private String firstName;

  @NotBlank(message = "Фамилия обязательна")
  private String lastName;

  @NotBlank(message = "Отчество обязательно")
  private String middleName;

  @NotBlank(message = "Страна прибытия обязательна")
  private String countryOfArrival;

  @NotNull(message = "Дата прибытия обязательна")
  private LocalDate arrivalDate;

  private Boolean hasRussianLanguageCertificate;
  private Boolean hasWorkPatent;
  private Boolean hasPaidStateDuty;
}
