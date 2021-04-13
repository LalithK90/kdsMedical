package lk.kds_medical.asset.process_management.model;


import lk.kds_medical.asset.employee.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAppointmentPaymentCount {

  private Employee employee;

  private long appointmentCount;
  private long appointmentCancelCount;
  private long appointmentBookingCount;
  private long appointmentPaidCount;

  private long additionalServiceCount;

  private long paymentCount;
  private BigDecimal paymentTotal;

  private long cardPaymentCount;
  private BigDecimal cardPaymentTotal;

  private long cashPaymentCount;
  private BigDecimal cashPaymentTotal;

  //appointments
  private long paymentAppointmentCount;
  private BigDecimal paymentAppointmentTotal;

  private long cardPaymentAppointmentCount;
  private BigDecimal cardPaymentAppointmentTotal;

  private long cashPaymentAppointmentCount;
  private BigDecimal cashPaymentAppointmentTotal;

//additionalService
  private long paymentAdditionalCount;
  private BigDecimal paymentAdditionalTotal;

  private long cardPaymentAdditionalCount;
  private BigDecimal cardPaymentAdditionalTotal;

  private long cashPaymentAdditionalCount;
  private BigDecimal cashPaymentAdditionalTotal;

}
