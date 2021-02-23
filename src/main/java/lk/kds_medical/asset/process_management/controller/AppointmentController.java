package lk.kds_medical.asset.process_management.controller;

import lk.kds_medical.asset.appointment.service.AppointmentService;
import lk.kds_medical.asset.doctor.entity.Doctor;
import lk.kds_medical.asset.doctor.service.DoctorService;
import lk.kds_medical.asset.patient.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.DayOfWeek;
import java.util.List;

@Controller
@RequestMapping( "/appointment" )
public class AppointmentController {
  private final DoctorService doctorService;
  private final PatientService patientService;
  private final AppointmentService appointmentService;

  public AppointmentController(DoctorService doctorService, PatientService patientService,
                               AppointmentService appointmentService) {
    this.doctorService = doctorService;
    this.patientService = patientService;
    this.appointmentService = appointmentService;
  }

  @GetMapping
  public String appointment(Model model) {
    List< Doctor > doctors = doctorService.findAll();
    for ( DayOfWeek dayOfWeek : DayOfWeek.values() ) {
      doctors.forEach(x -> {
        x.getDoctorSchedules().forEach(y -> {
          y.getDayOfWeek().equals(dayOfWeek);
        });
      });
    }
    return "appointment/appointment";
  }
}
