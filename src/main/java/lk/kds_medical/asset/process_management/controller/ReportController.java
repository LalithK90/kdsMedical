package lk.kds_medical.asset.process_management.controller;

import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.appointment.entity.enums.AppointmentStatus;
import lk.kds_medical.asset.appointment.service.AppointmentService;
import lk.kds_medical.asset.common_asset.model.TwoDate;
import lk.kds_medical.asset.doctor.entity.Doctor;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    doctorService.findAll().forEach(x -> doctorCounts.add(doctorOne(appointments, x)));
    return doctorCounts;
  }

  private DoctorCount doctorOne(List< Appointment > appointments, Doctor x) {
    DoctorCount doctorCount = new DoctorCount();
    doctorCount.setDoctor(x);
    doctorCount.setAppointmentCount(appointments.stream().filter(y -> y.getDoctorSchedule().getDoctor().equals(x)).count());

    List< Appointment > paidAppointments =
        appointments.stream().filter(z -> z.getAppointmentStatus().equals(AppointmentStatus.PA)).collect(Collectors.toList());

    if ( !paidAppointments.isEmpty() ) {
      doctorCount.setAppointmentPaidCount(paidAppointments.stream().filter(y -> y.getDoctorSchedule().getDoctor().equals(x)).count());
      doctorCount.setTotalIncome(x.getConsultationFee().multiply(new BigDecimal(paidAppointments.size())));
    }

    List< Appointment > bookedAppointments =
        appointments.stream().filter(z -> z.getAppointmentStatus().equals(AppointmentStatus.BK)).collect(Collectors.toList());

    if ( !bookedAppointments.isEmpty() ) {
      doctorCount.setAppointmentBookedCount(bookedAppointments.stream().filter(y -> y.getDoctorSchedule().getDoctor().equals(x)).count());
    }

    List< Appointment > cancelAppointments =
        appointments.stream().filter(z -> z.getAppointmentStatus().equals(AppointmentStatus.CL)).collect(Collectors.toList());

    if ( !cancelAppointments.isEmpty() ) {
      doctorCount.setAppointmentCancelCount(cancelAppointments.stream().filter(y -> y.getDoctorSchedule().getDoctor().equals(x)).count());
    }
    return doctorCount;
  }

  private List< Payment > paymentsAccordingToPaymentMethod(List< Payment > payments, PaymentMethod paymentMethod) {
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
      employeeAppointmentPaymentCounts.add(employeeOne(appointmentsAccordingUser, paymentsAccordingUser,
                                                       x.getEmployee()));

    });

    return employeeAppointmentPaymentCounts;
  }

  private EmployeeAppointmentPaymentCount employeeOne(List< Appointment > appointments,
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
      payments.forEach(x -> paymentTotal.add(x.getTotalAmount()));
      employeeAppointmentPaymentCount.setPaymentCount(payments.size());
      employeeAppointmentPaymentCount.setPaymentTotal(totalAmount(paymentTotal));

      if ( !paymentsAccordingToPaymentMethod(payments, PaymentMethod.CREDIT).isEmpty() ) {
        List< BigDecimal > cardPaymentTotal = new ArrayList<>();
        paymentsAccordingToPaymentMethod(payments, PaymentMethod.CREDIT).forEach(x -> cardPaymentTotal.add(x.getTotalAmount()));
        employeeAppointmentPaymentCount.setCardPaymentCount(paymentsAccordingToPaymentMethod(payments,
                                                                                             PaymentMethod.CREDIT).size());
        employeeAppointmentPaymentCount.setCardPaymentTotal(totalAmount(cardPaymentTotal));
      }

      if ( !paymentsAccordingToPaymentMethod(payments, PaymentMethod.CASH).isEmpty() ) {
        List< BigDecimal > cashPaymentTotal = new ArrayList<>();
        paymentsAccordingToPaymentMethod(payments, PaymentMethod.CASH).forEach(x -> cashPaymentTotal.add(x.getTotalAmount()));
        employeeAppointmentPaymentCount.setCashPaymentCount(paymentsAccordingToPaymentMethod(payments,
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

        List< Payment > paymentAppointmentCard = paymentsAccordingToPaymentMethod(paymentAppointment,
                                                                                  PaymentMethod.CREDIT);
        if ( !paymentAppointmentCard.isEmpty() ) {
          employeeAppointmentPaymentCount.setCardPaymentAppointmentCount(paymentAppointmentCard.size());
          List< BigDecimal > paymentCardPaymentAppointmentAmount = new ArrayList<>();
          paymentAppointmentCard.forEach(x -> paymentCardPaymentAppointmentAmount.add(x.getTotalAmount()));
          employeeAppointmentPaymentCount.setCardPaymentAppointmentTotal(totalAmount(paymentCardPaymentAppointmentAmount));

        }
        List< Payment > paymentAppointmentCash = paymentsAccordingToPaymentMethod(paymentAppointment,
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

        List< Payment > paymentAdditionalCard = paymentsAccordingToPaymentMethod(paymentAdditional,
                                                                                 PaymentMethod.CREDIT);
        if ( !paymentAdditionalCard.isEmpty() ) {
          employeeAppointmentPaymentCount.setCardPaymentAdditionalCount(paymentAdditionalCard.size());
          List< BigDecimal > paymentCardPaymentAdditionalAmount = new ArrayList<>();
          paymentAdditionalCard.forEach(x -> paymentCardPaymentAdditionalAmount.add(x.getTotalAmount()));
          employeeAppointmentPaymentCount.setCardPaymentAdditionalTotal(totalAmount(paymentCardPaymentAdditionalAmount));

        }
        List< Payment > paymentAdditionalCash = paymentsAccordingToPaymentMethod(paymentAdditional,
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

  @GetMapping( "/user" )
  public String oneUser(Model model) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Employee employee = userService.findByUserName(username).getEmployee();
    List< Appointment > appointments =
        appointmentService.findByCreatedBy(username).stream().filter(x -> dateTimeAgeService.localDateTimeToLocalDate(x.getCreatedAt()).equals(LocalDate.now())).collect(Collectors.toList());

    List< Payment > payments =
        paymentService.findByUpdatedBy(username).stream().filter(x -> dateTimeAgeService.localDateTimeToLocalDate(x.getUpdatedAt()).equals(LocalDate.now())).collect(Collectors.toList());

    model.addAttribute("employeeAppointmentPaymentCount", employeeOne(appointments,
                                                                      payments,
                                                                      employee));
    model.addAttribute("date", LocalDate.now());
    return "report/user";
  }


  @GetMapping( "/allUser" )
  public String allUser(Model model) {
    LocalDate localDate = LocalDate.now();
    String message = "This report is belongs to " + localDate.toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(localDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(localDate);
    List< Appointment > appointments = appointmentService.findByCreatedAtIsBetween(startDateTime, endDateTime);

    List< Payment > payments = paymentService.findByUpdatedAtIsBetween(startDateTime, endDateTime);

    model.addAttribute("employeeAppointmentPaymentCounts", employeeAll(appointments,
                                                                       payments));
    model.addAttribute("message", message);
    return "report/userAll";
  }

  @PostMapping( "/allUser" )
  public String allUser(@ModelAttribute( "twoDate" ) TwoDate twoDate, Model model) {
    String message =
        "This report is between from " + twoDate.getStartDate().toString() + " to " + twoDate.getEndDate().toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());
    List< Appointment > appointments = appointmentService.findByCreatedAtIsBetween(startDateTime, endDateTime);

    List< Payment > payments = paymentService.findByUpdatedAtIsBetween(startDateTime, endDateTime);

    model.addAttribute("employeeAppointmentPaymentCounts", employeeAll(appointments,
                                                                       payments));
    model.addAttribute("message", message);
    return "report/userAll";
  }



}
