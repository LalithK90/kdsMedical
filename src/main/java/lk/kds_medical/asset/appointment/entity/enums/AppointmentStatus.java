package lk.kds_medical.asset.appointment.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppointmentStatus {
  BK("Booking"),
  PA("Paid Booking"),
  CL("Cancel");

  private final String appointmentStatus;
}
