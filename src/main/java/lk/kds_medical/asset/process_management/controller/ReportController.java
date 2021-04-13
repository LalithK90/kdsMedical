package lk.kds_medical.asset.process_management.controller;

import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.appointment.entity.enums.AppointmentStatus;
import lk.kds_medical.asset.appointment.service.AppointmentService;
import lk.kds_medical.asset.doctor.service.DoctorService;
import lk.kds_medical.asset.doctor_schedule.service.DoctorScheduleService;
import lk.kds_medical.asset.employee.entity.Employee;
import lk.kds_medical.asset.employee.service.EmployeeService;
import lk.kds_medical.asset.patient.service.PatientService;
import lk.kds_medical.asset.payment.entity.Payment;
import lk.kds_medical.asset.payment.entity.enums.PaymentMethod;
import lk.kds_medical.asset.payment.entity.enums.PaymentValidOrNot;
import lk.kds_medical.asset.payment.service.PaymentService;
import lk.kds_medical.asset.process_management.model.DoctorCount;
import lk.kds_medical.asset.process_management.model.DoctorScheduleCount;
import lk.kds_medical.asset.process_management.model.EmployeeAppointmentPaymentCount;
import lk.kds_medical.asset.user_management.service.UserService;
import lk.kds_medical.util.service.DateTimeAgeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/report" )
public class ReportController {
  private final AppointmentService appointmentService;
  private final DoctorScheduleService doctorScheduleService;
  private final DateTimeAgeService dateTimeAgeService;
  private final DoctorService doctorService;
  private final PatientService patientService;
  private final PaymentService paymentService;
  private final UserService userService;
  private final EmployeeService employeeService;

  public ReportController(AppointmentService appointmentService, DoctorScheduleService doctorScheduleService,
                          DateTimeAgeService dateTimeAgeService, DoctorService doctorService,
                          PatientService patientService, PaymentService paymentService, UserService userService,
                          EmployeeService employeeService) {
    this.appointmentService = appointmentService;
    this.doctorScheduleService = doctorScheduleService;
    this.dateTimeAgeService = dateTimeAgeService;
    this.doctorService = doctorService;
    this.patientService = patientService;
    this.paymentService = paymentService;
    this.userService = userService;
    this.employeeService = employeeService;
  }

  private long appointmentAppointmentStatusCount(List< Appointment > appointments,
                                                 AppointmentStatus appointmentStatus) {
    if ( appointments.isEmpty() ) {
      return 0;
    } else {
      return appointments.stream().filter(x -> x.getAppointmentStatus().equals(appointmentStatus)).count();
    }
  }

  private List< DoctorScheduleCount > accordingDoctorSchedules(List< Appointment > appointments) {
    List< DoctorScheduleCount > doctorScheduleCounts = new ArrayList<>();

    doctorScheduleService.findAll().forEach(x -> {
      DoctorScheduleCount doctorScheduleCount = new DoctorScheduleCount();
      doctorScheduleCount.setDoctorSchedule(x);
      doctorScheduleCount.setAppointmentCount(appointments.stream().filter(y -> y.getDoctorSchedule().equals(x)).count());
      doctorScheduleCounts.add(doctorScheduleCount);
    });

    return doctorScheduleCounts;
  }

  private List< DoctorCount > accordingDoctor(List< Appointment > appointments) {
    List< DoctorCount > doctorCounts = new ArrayList<>();

    doctorService.findAll().forEach(x -> {
      DoctorCount doctorCount = new DoctorCount();
      doctorCount.setDoctor(x);
      doctorCount.setAppointmentCount(appointments.stream().filter(y -> y.getDoctorSchedule().getDoctor().equals(x)).count());
      doctorCounts.add(doctorCount);
    });

    return doctorCounts;
  }

  private List< Payment > paymentsAccodingToPaymentMethod(List< Payment > payments, PaymentMethod paymentMethod) {
    return payments.stream().filter(x -> x.getPaymentMethod().equals(paymentMethod)).collect(Collectors.toList());
  }

  private List< Payment > paymentsAccodingToValidOrNot(List< Payment > payments, PaymentValidOrNot paymentValidOrNot) {
    return payments.stream().filter(x -> x.getPaymentValidOrNot().equals(paymentValidOrNot)).collect(Collectors.toList());
  }

  private BigDecimal totalAmount(List< BigDecimal > amount) {
    return amount.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private List< EmployeeAppointmentPaymentCount > employeeAll(List< Appointment > appointments,
                                                              List< Payment > payments) {
    List< EmployeeAppointmentPaymentCount > employeeAppointmentPaymentCounts = new ArrayList<>();
    userService.findAll().forEach(x -> {
      List< Appointment > appointmentsAccordingUser =
          appointments.stream().filter(y -> y.getCreatedBy().equals(x.getUsername())).collect(Collectors.toList());
      List< Payment > paymentsAccordingUser =
          payments.stream().filter(z -> z.getCreatedBy().equals(x.getUsername())).collect(Collectors.toList());
      employeeAppointmentPaymentCounts.add(employee(appointments, payments, x.getEmployee()));

    });

    return employeeAppointmentPaymentCounts;
  }

  private EmployeeAppointmentPaymentCount employee(List< Appointment > appointments,
                                                   List< Payment > payments, Employee employee) {
    EmployeeAppointmentPaymentCount employeeAppointmentPaymentCount = new EmployeeAppointmentPaymentCount();
    employeeAppointmentPaymentCount.setEmployee(employeeService.findById(employee.getId()));
    employeeAppointmentPaymentCount.setAppointmentCount(appointments.size());
    employeeAppointmentPaymentCount.setAppointmentCancelCount(appointmentAppointmentStatusCount(appointments,
                                                                                                AppointmentStatus.CL));
    employeeAppointmentPaymentCount.setAppointmentBookingCount(appointmentAppointmentStatusCount(appointments,
                                                                                                 AppointmentStatus.BK));
    employeeAppointmentPaymentCount.setAppointmentPaidCount(appointmentAppointmentStatusCount(appointments,
                                                                                              AppointmentStatus.PA));
    if ( !payments.isEmpty() ) {
      employeeAppointmentPaymentCount.setAdditionalServiceCount(payments.stream().filter(x -> x.getAppointment() != null).count());
      List< BigDecimal > paymentTotal = new ArrayList<>();
      employeeAppointmentPaymentCount.setPaymentCount(payments.size());
      employeeAppointmentPaymentCount.setPaymentTotal(totalAmount(paymentTotal));

      if ( !paymentsAccodingToPaymentMethod(payments, PaymentMethod.CREDIT).isEmpty() ) {
        List< BigDecimal > cardPaymentTotal = new ArrayList<>();
        paymentsAccodingToPaymentMethod(payments, PaymentMethod.CREDIT).forEach(x -> cardPaymentTotal.add(x.getTotalAmount()));
        employeeAppointmentPaymentCount.setCardPaymentCount(paymentsAccodingToPaymentMethod(payments,
                                                                                            PaymentMethod.CREDIT).size());
        employeeAppointmentPaymentCount.setCardPaymentTotal(totalAmount(cardPaymentTotal));
      }

      if ( !paymentsAccodingToPaymentMethod(payments, PaymentMethod.CASH).isEmpty() ) {
        List< BigDecimal > cashPaymentTotal = new ArrayList<>();
        paymentsAccodingToPaymentMethod(payments, PaymentMethod.CASH).forEach(x -> cashPaymentTotal.add(x.getTotalAmount()));
        employeeAppointmentPaymentCount.setCashPaymentCount(paymentsAccodingToPaymentMethod(payments,
                                                                                            PaymentMethod.CASH).size());
        employeeAppointmentPaymentCount.setCashPaymentTotal(totalAmount(cashPaymentTotal));
      }

      List< Payment > paymentAppointment =
          payments.stream().filter(x -> x.getAppointment() != null).collect(Collectors.toList());

      if ( !paymentAppointment.isEmpty() ) {
        List< BigDecimal > paymentAppointmentTotalAmount = new ArrayList<>();
        paymentAppointment.forEach(x -> paymentAppointmentTotalAmount.add(x.getTotalAmount()));
        employeeAppointmentPaymentCount.setPaymentAppointmentCount(paymentAppointment.size());
        employeeAppointmentPaymentCount.setPaymentAppointmentTotal(totalAmount(paymentAppointmentTotalAmount));

        List< Payment > paymentAppointmentCard = paymentsAccodingToPaymentMethod(paymentAppointment,
                                                                                 PaymentMethod.CREDIT);
        if ( !paymentAppointmentCard.isEmpty() ) {
          employeeAppointmentPaymentCount.setCardPaymentAppointmentCount(paymentAppointmentCard.size());
          List< BigDecimal > paymentCardPaymentAppointmentAmount = new ArrayList<>();
          paymentAppointmentCard.forEach(x -> paymentCardPaymentAppointmentAmount.add(x.getTotalAmount()));
          employeeAppointmentPaymentCount.setCardPaymentAppointmentTotal(totalAmount(paymentCardPaymentAppointmentAmount));

        }
        List< Payment > paymentAppointmentCash = paymentsAccodingToPaymentMethod(paymentAppointment,
                                                                                 PaymentMethod.CASH);
        if ( !paymentAppointmentCash.isEmpty() ) {
          employeeAppointmentPaymentCount.setCashPaymentAppointmentCount(paymentAppointmentCash.size());
          List< BigDecimal > paymentCashPaymentAppointmentAmount = new ArrayList<>();
          paymentAppointmentCash.forEach(x -> paymentCashPaymentAppointmentAmount.add(x.getTotalAmount()));
          employeeAppointmentPaymentCount.setCashPaymentAppointmentTotal(totalAmount(paymentCashPaymentAppointmentAmount));

        }
      }

      List< Payment > paymentAdditional =
          payments.stream().filter(x -> x.getPaymentAdditionalService() != null).collect(Collectors.toList());

      if ( !paymentAdditional.isEmpty() ) {
        List< BigDecimal > paymentAdditionalTotalAmount = new ArrayList<>();
        paymentAdditional.forEach(x -> paymentAdditionalTotalAmount.add(x.getTotalAmount()));
        employeeAppointmentPaymentCount.setPaymentAdditionalCount(paymentAppointment.size());
        employeeAppointmentPaymentCount.setPaymentAdditionalTotal(totalAmount(paymentAdditionalTotalAmount));

        List< Payment > paymentAdditionalCard = paymentsAccodingToPaymentMethod(paymentAdditional,
                                                                                PaymentMethod.CREDIT);
        if ( !paymentAdditionalCard.isEmpty() ) {
          employeeAppointmentPaymentCount.setCardPaymentAdditionalCount(paymentAdditionalCard.size());
          List< BigDecimal > paymentCardPaymentAdditionalAmount = new ArrayList<>();
          paymentAdditionalCard.forEach(x -> paymentCardPaymentAdditionalAmount.add(x.getTotalAmount()));
          employeeAppointmentPaymentCount.setCardPaymentAdditionalTotal(totalAmount(paymentCardPaymentAdditionalAmount));

        }
        List< Payment > paymentAdditionalCash = paymentsAccodingToPaymentMethod(paymentAdditional,
                                                                                PaymentMethod.CASH);
        if ( !paymentAdditionalCash.isEmpty() ) {
          employeeAppointmentPaymentCount.setCashPaymentAdditionalCount(paymentAdditionalCash.size());
          List< BigDecimal > paymentCashPaymentAppointmentAmount = new ArrayList<>();
          paymentAdditionalCash.forEach(x -> paymentCashPaymentAppointmentAmount.add(x.getTotalAmount()));
          employeeAppointmentPaymentCount.setCashPaymentAdditionalTotal(totalAmount(paymentCashPaymentAppointmentAmount));

        }
      }
    }

    return employeeAppointmentPaymentCount;
  }


}
