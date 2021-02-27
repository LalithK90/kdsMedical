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
import lk.kds_medical.asset.payment.entity.enums.PaymentMethod;
import lk.kds_medical.asset.payment.entity.enums.PaymentPrintOrNot;
import lk.kds_medical.asset.process_management.model.AppointmentBook;
import lk.kds_medical.asset.process_management.model.AppointmentDoctorSearch;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

  public AppointmentController(DoctorService doctorService, PatientService patientService,
                               AppointmentService appointmentService, DoctorScheduleService doctorScheduleService,
                               DiscountRatioService discountRatioService) {
    this.doctorService = doctorService;
    this.patientService = patientService;
    this.appointmentService = appointmentService;
    this.doctorScheduleService = doctorScheduleService;
    this.discountRatioService = discountRatioService;
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
        List< Appointment > appointments = appointmentService.byDate(today)
            .stream().filter(y -> y.getDoctorSchedule().equals(doctorSchedule)).collect(Collectors.toList());
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
    DoctorSchedule doctorSchedule = doctorScheduleService.findById(appointmentDoctorSearch.getDoctorSchedule().getId());
    LocalDate appointmentDate = appointmentDoctorSearch.getAppointmentDate();
    if ( !bindingResult.hasErrors() && doctorSchedule.getDayOfWeek().equals(appointmentDate.getDayOfWeek()) ) {
      Appointment appointment = new Appointment();
      appointment.setAppointmentStatus(AppointmentStatus.BK);
      appointment.setDoctorSchedule(doctorSchedule);
      appointment.setLiveDead(LiveDead.ACTIVE);
      appointment.setDate(appointmentDate);
      model.addAttribute("appointmentStatuses", AppointmentStatus.values());
      model.addAttribute("doctorSchedules", doctorScheduleService.findByDoctor(appointmentDoctorSearch.getDoctor()));
      model.addAttribute("patients", patientService.findAll());
      model.addAttribute("appointment", appointment);
      System.out.println(doctorSchedule.getDoctor().getConsultationFee());
      model.addAttribute("consultationFee", doctorSchedule.getDoctor().getConsultationFee());
      model.addAttribute("invoicePrintOrNots", PaymentPrintOrNot.values());
      model.addAttribute("paymentMethods", PaymentMethod.values());
      model.addAttribute("discountRatios", discountRatioService.findAll());
      return "appointment/addAppointment";
    } else {
      redirectAttributes.addFlashAttribute("message", "Selected date is not matched with doctor schedule.");
      return "redirect:/appointment/add";
    }
  }
  //todo need to save appointment method
  //todo need to add appointment edit

}
