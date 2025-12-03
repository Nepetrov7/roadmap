export interface UserProfileResponse {
  id: number;
  firstName: string;
  lastName: string;
  middleName: string;
  username: string;
  countryOfArrival: string | null;
  arrivalDate: string | null;
  hasRussianLanguageCertificate: boolean;
  hasWorkPatent: boolean;
  hasPaidStateDuty: boolean;
  isProfileComplete: boolean;
}

export interface UserProfileUpdateRequest {
  firstName: string;
  lastName: string;
  middleName: string;
  countryOfArrival: string;
  arrivalDate: string;
  hasRussianLanguageCertificate: boolean;
  hasWorkPatent: boolean;
  hasPaidStateDuty: boolean;
}
