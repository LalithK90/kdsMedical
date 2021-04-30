package lk.kds_medical.asset.process_management.controller;

import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.appointment.entity.enums.AppointmentStatus;
import lk.kds_medical.asset.appointment.service.AppointmentService;
import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.discount_ratio.service.DiscountRatioService;
import lk.kds_medical.asset.doctor.controller.DoctorController;
import lk.kds_medical.asset.doctor.service.DoctorService;
import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
import lk.kds_medical.asset.doctor_schedule.service.DoctorScheduleService;
import lk.kds_medical.asset.patient.service.PatientService;
import lk.kds_medical.asset.payment.entity.Payment;
import lk.kds_medical.asset.payment.entity.enums.PaymentMethod;
import lk.kds_medical.asset.payment.entity.enums.PaymentPrintOrNot;
import lk.kds_medical.asset.payment.entity.enums.PaymentValidOrNot;
import lk.kds_medical.asset.payment.service.PaymentService;
import lk.kds_medical.asset.process_management.model.AppointmentBook;
import lk.kds_medical.asset.process_management.model.AppointmentDoctorSearch;
import lk.kds_medical.util.service.EmailService;
import lk.kds_medical.util.service.MakeAutoGenerateNumberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/appointment" )
public class AppointmentController {
  private final DoctorService doctorService;
  private final PatientService patientService;
  private final AppointmentService appointmentService;
  private final DoctorScheduleService doctorScheduleService;
  private final DiscountRatioService discountRatioService;
  private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;
  private final PaymentService paymentService;
  private final EmailService emailService;

  public AppointmentController(DoctorService doctorService, PatientService patientService,
                               AppointmentService appointmentService, DoctorScheduleService doctorScheduleService,
                               DiscountRatioService discountRatioService,
                               MakeAutoGenerateNumberService makeAutoGenerateNumberService,
                               PaymentService paymentService, EmailService emailService) {
    this.doctorService = doctorService;
    this.patientService = patientService;
    this.appointmentService = appointmentService;
    this.doctorScheduleService = doctorScheduleService;
    this.discountRatioService = discountRatioService;
    this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
    this.paymentService = paymentService;
    this.emailService = emailService;
  }

  @GetMapping
  public String appointment(Model model) {

    LocalDate today = LocalDate.now();

    List< AppointmentBook > appointmentBooks = new ArrayList<>();

    for ( int i = 0; i < 7; i++ ) {
      DayOfWeek dayOfWeek = today.getDayOfWeek();
      AppointmentBook appointmentBook = new AppointmentBook();
      appointmentBook.setDayOfWeek(dayOfWeek);
      appointmentBook.setAppointmentDate(today);

      List< DoctorSchedule > doctorSchedules = new ArrayList<>();

      //get all appointments on today

      for ( DoctorSchedule doctorSchedule : doctorScheduleService.findAll()
          .stream().filter(x -> x.getDayOfWeek().equals(dayOfWeek)).collect(Collectors.toList()) ) {
        //all active appointment
        List< Appointment > appointments = appointmentService.byDateAndDoctorSchedule(today, doctorSchedule)
            .stream()
            .filter(x -> !x.getAppointmentStatus().equals(AppointmentStatus.CL))
            .collect(Collectors.toList());
        doctorSchedule.setAppointments(appointments);
        doctorSchedule.setCount(appointments.size());
        doctorSchedule.setAppointmentDate(today);
        doctorSchedules.add(doctorSchedule);
      }
      appointmentBook.setDoctorSchedules(doctorSchedules);
      appointmentBooks.add(appointmentBook);
      today = today.plusDays(1);
    }
    model.addAttribute("appointmentBooks", appointmentBooks);
    return "appointment/appointment";
  }

  @GetMapping( "/add" )
  public String appointmentAdd(Model model) {
    model.addAttribute("doctors", doctorService.findAll());
    model.addAttribute("appointmentDoctorSearch", new AppointmentDoctorSearch());
    model.addAttribute("doctorScheduleUrl", MvcUriComponentsBuilder
        .fromMethodName(DoctorController.class, "doctorScheduleForDoctor", "")
        .build()
        .toString());
    return "appointment/finderAppointment";
  }

  @PostMapping( "/find" )
  public String findAppointment(@Valid @ModelAttribute AppointmentDoctorSearch appointmentDoctorSearch,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
    return common(appointmentDoctorSearch, bindingResult, redirectAttributes, model);

  }

  private String common(AppointmentDoctorSearch appointmentDoctorSearch, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes, Model model) {
    DoctorSchedule doctorSchedule = doctorScheduleService.findById(appointmentDoctorSearch.getDoctorSchedule().getId());
    LocalDate appointmentDate = appointmentDoctorSearch.getAppointmentDate();
    if ( !bindingResult.hasErrors() && doctorSchedule.getDayOfWeek().equals(appointmentDate.getDayOfWeek()) ) {
      Appointment appointment = new Appointment();
      appointment.setAppointmentStatus(AppointmentStatus.BK);
      appointment.setDoctorSchedule(doctorSchedule);
      appointment.setLiveDead(LiveDead.ACTIVE);
      appointment.setDate(appointmentDate);
      model.addAttribute("appointment", appointment);
      model.addAttribute("consultationFee", doctorSchedule.getDoctor().getConsultationFee());
      model.addAttribute("doctorSchedules", doctorScheduleService.findByDoctor(appointmentDoctorSearch.getDoctor()));
      List< AppointmentStatus > appointmentStatuses = new ArrayList<>();
      appointmentStatuses.add(AppointmentStatus.BK);
      appointmentStatuses.add(AppointmentStatus.PA);
      model.addAttribute("appointmentStatuses", appointmentStatuses);
      model.addAttribute("patients", patientService.findAll());
      model.addAttribute("invoicePrintOrNots", PaymentPrintOrNot.values());
      model.addAttribute("paymentMethods", PaymentMethod.values());
      model.addAttribute("discountRatios", discountRatioService.findAll());
      return "appointment/addAppointment";
    } else {
      redirectAttributes.addFlashAttribute("message", "Selected date is not matched with doctor schedule.");
      return "redirect:/appointment/add";
    }
  }

  @PostMapping( "/save" )
  public String makeAppointment(@ModelAttribute Appointment appointment, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes, Model model) {

    Appointment todayAppointment =
        appointmentService.findByDoctorScheduleAndPatientAndDate(appointment.getDoctorSchedule(),
                                                                 appointment.getPatient(), appointment.getDate());

    if ( todayAppointment != null ) {
      model.addAttribute("message", "There is already appointment for this patient ");
      return common(new AppointmentDoctorSearch(appointment.getDoctorSchedule().getDoctor(),
                                                appointment.getDate(),
                                                appointment.getDoctorSchedule(), ""), bindingResult, redirectAttributes,
                    model);
    }

    if ( appointment.getPayments().get(0).getTotalAmount() != null ) {
      appointment.getPayments().forEach(x -> {
        x.setAppointment(appointment);
        if ( x.getId() == null ) {
          Payment lastPayment = paymentService.lastPayment();
          if ( lastPayment == null ) {
            x.setCode("KDSP" + makeAutoGenerateNumberService.numberAutoGen(null).toString());
          } else {
            x.setCode("KDSP" + makeAutoGenerateNumberService.numberAutoGen(lastPayment.getCode().substring(4)).toString());
          }
        }
      });
    } else {
      appointment.setPayments(null);
    }

    if ( appointment.getId() == null ) {
      Appointment lastAppointment = appointmentService.lastAppointment();
      if ( lastAppointment == null ) {
        appointment.setNumber(1);
        appointment.setCode("KDSA" + makeAutoGenerateNumberService.numberAutoGen(null).toString());
      } else {
        appointment.setNumber(lastAppointment.getNumber() + 1);
        appointment.setCode("KDSA" + makeAutoGenerateNumberService.numberAutoGen(lastAppointment.getCode().substring(4)).toString());
      }
    }

    Appointment appointmentDb = appointmentService.persist(appointment);
    if ( appointmentDb.getAppointmentStatus().equals(AppointmentStatus.PA) ) {
      return "redirect:/payment/" + paymentService.findByAppointment(appointmentDb).getId();
    } else {
      return "redirect:/appointment";
    }
  }

  @PostMapping( "/view" )
  public String viewAppointment(@Valid @ModelAttribute AppointmentDoctorSearch appointmentDoctorSearch, Model model) {
    List< Appointment > appointments =
        appointmentService.byDateAndDoctorSchedule(appointmentDoctorSearch.getAppointmentDate(),
                                                   appointmentDoctorSearch.getDoctorSchedule())
            .stream()
            .filter(x -> !x.getAppointmentStatus().equals(AppointmentStatus.CL))
            .collect(Collectors.toList());
    model.addAttribute("appointments", appointments);
    model.addAttribute("doctorDetail", appointments.get(0).getDoctorSchedule().getDoctor());
    return "appointment/appointment-view";
  }

  @PostMapping( "/message" )
  public String sendMessage(@Valid @ModelAttribute AppointmentDoctorSearch appointmentDoctorSearch, Model model) {
    List< Appointment > appointments =
        appointmentService.byDateAndDoctorSchedule(appointmentDoctorSearch.getAppointmentDate(),
                                                   appointmentDoctorSearch.getDoctorSchedule())
            .stream()
            .filter(x -> !x.getAppointmentStatus().equals(AppointmentStatus.CL))
            .collect(Collectors.toList());
    model.addAttribute("appointments", appointments);
    model.addAttribute("doctorDetail", appointments.get(0).getDoctorSchedule().getDoctor());
    return "appointment/appointment-message";
  }

  @PostMapping( "/messageSend" )
  public String sendMessageSend(@Valid @ModelAttribute AppointmentDoctorSearch appointmentDoctorSearch, Model model) {
    List< Appointment > appointments =
        appointmentService.byDateAndDoctorSchedule(appointmentDoctorSearch.getAppointmentDate(),
                                                   appointmentDoctorSearch.getDoctorSchedule())
            .stream()
            .filter(x -> !x.getAppointmentStatus().equals(AppointmentStatus.CL))
            .collect(Collectors.toList());
    appointments.forEach(x -> {
      if ( x.getPatient().getEmail() != null ) {
        String message =
            "Regarding that You had booked following appointment " + x.getCode() + appointmentDoctorSearch.getMessage();
        emailService.sendEmail(x.getPatient().getEmail(), "Related Appointment Number " + x.getCode(), message);
      }
    });
    return "redirect:/appointment";
  }


  @GetMapping( "/refund/{id}" )
  public String refundAppointment(@PathVariable( "id" ) Integer id, Model model) {
    Appointment appointment = appointmentService.findById(id);
    appointment.setAppointmentStatus(AppointmentStatus.CL);
    Appointment appointmentDb = appointmentService.persist(appointment);

    Payment payment = paymentService.findByAppointment(appointmentDb);
    payment.setPaymentValidOrNot(PaymentValidOrNot.NOT_VALID);
    model.addAttribute("paymentDetail", paymentService.persist(payment));
    return "payment/payment-detail";
  }

  @GetMapping( "/cancel/{id}" )
  public String cancelAppointment(@PathVariable( "id" ) Integer id) {
    Appointment appointment = appointmentService.findById(id);
    appointment.setAppointmentStatus(AppointmentStatus.CL);
    appointmentService.persist(appointment);
    return "redirect:/appointment";
  }

  @GetMapping( "/pay/{id}" )
  public String payAppointment(@PathVariable( "id" ) Integer id, Model model) {

    Appointment appointment = appointmentService.findById(id);
    model.addAttribute("appointment", appointment);
    model.addAttribute("consultationFee", appointment.getDoctorSchedule().getDoctor().getConsultationFee());
    model.addAttribute("doctorSchedules", appointment.getDoctorSchedule());
    List< AppointmentStatus > appointmentStatuses = new ArrayList<>();
    appointmentStatuses.add(AppointmentStatus.PA);

    model.addAttribute("appointmentStatuses", appointmentStatuses);
    model.addAttribute("patient", patientService.findById(appointment.getPatient().getId()));
    model.addAttribute("invoicePrintOrNots", PaymentPrintOrNot.values());
    model.addAttribute("paymentMethods", PaymentMethod.values());
    model.addAttribute("discountRatios", discountRatioService.findAll());
    return "appointment/addAppointmentPayment";
  }

  @PostMapping( "/update" )
  public String makeAppointmentPayment(@ModelAttribute Appointment appointment) {

    Appointment appointmentDb = appointmentService.findById(appointment.getId());
    appointmentDb.setAppointmentStatus(AppointmentStatus.PA);

    if ( appointment.getPayments().get(0).getTotalAmount() != null ) {
      Payment payment = appointment.getPayments().get(0);
      payment.setAppointment(appointmentService.persist(appointmentDb));
      if ( payment.getId() == null ) {
        Payment lastPayment = paymentService.lastPayment();
        if ( lastPayment == null ) {
          payment.setCode("KDSP" + makeAutoGenerateNumberService.numberAutoGen(null).toString());
        } else {
          payment.setCode("KDSP" + makeAutoGenerateNumberService.numberAutoGen(lastPayment.getCode().substring(4)).toString());
        }
      }
      paymentService.persist(payment);
    }

    return "redirect:/appointment";
  }


}
