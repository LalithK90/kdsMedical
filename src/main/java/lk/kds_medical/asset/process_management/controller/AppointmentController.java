package lk.kds_medical.asset.process_management.controller;

import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.appointment.service.AppointmentService;
import lk.kds_medical.asset.doctor.entity.Doctor;
import lk.kds_medical.asset.doctor.service.DoctorService;
import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
import lk.kds_medical.asset.doctor_schedule.service.DoctorScheduleService;
import lk.kds_medical.asset.patient.service.PatientService;
import lk.kds_medical.asset.process_management.model.AppointmentBook;
import lk.kds_medical.asset.process_management.model.AppointmentDoctorSearch;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
/*    List< DoctorSchedule > doctorSchedules = new ArrayList<>();
    appointmentService.byDate(LocalDate.now()).forEach(x -> doctorSchedules.add(x.getDoctorSchedule()));
    */
    List< DoctorSchedule > doctorSchedules = doctorScheduleService.findAll();

    List< AppointmentBook > appointmentBooks = new ArrayList<>();

    for ( DayOfWeek value : DayOfWeek.values() ) {
      AppointmentBook appointmentBook = new AppointmentBook();
      appointmentBook.setDayOfWeek(value);
      appointmentBook.setDoctorSchedules(
          doctorSchedules
              .stream()
              .filter(x -> x.getDayOfWeek().equals(value))
              .collect(Collectors.toList()));
      appointmentBooks.add(appointmentBook);
    }
    model.addAttribute("appointmentBooks", appointmentBooks);
    return "appointment/appointment";
  }

  @GetMapping("/add")
  public String appointmentAdd(Model model){
    model.addAttribute("doctors", doctorService.findAll());
    model.addAttribute("appointmentDoctorSearch", new AppointmentDoctorSearch());
    return "appointment/addAppointment";
  }
}
