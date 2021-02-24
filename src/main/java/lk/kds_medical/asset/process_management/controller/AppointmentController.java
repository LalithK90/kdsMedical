package lk.kds_medical.asset.process_management.controller;

import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.appointment.service.AppointmentService;
import lk.kds_medical.asset.doctor.controller.DoctorController;
import lk.kds_medical.asset.doctor.service.DoctorService;
import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
import lk.kds_medical.asset.doctor_schedule.service.DoctorScheduleService;
import lk.kds_medical.asset.patient.service.PatientService;
import lk.kds_medical.asset.process_management.model.AppointmentBook;
import lk.kds_medical.asset.process_management.model.AppointmentDoctorSearch;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

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


  public AppointmentController(DoctorService doctorService, PatientService patientService,
                               AppointmentService appointmentService, DoctorScheduleService doctorScheduleService) {
    this.doctorService = doctorService;
    this.patientService = patientService;
    this.appointmentService = appointmentService;
    this.doctorScheduleService = doctorScheduleService;
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

  @PostMapping( "/save" )
  public String addAppointment(@ModelAttribute AppointmentDoctorSearch appointmentDoctorSearch) {
    return "appointment/addAppointment";
  }

}
