package lk.kds_medical.asset.doctor.controller;


import lk.kds_medical.asset.common_asset.model.Enum.Gender;
import lk.kds_medical.asset.common_asset.model.Enum.Title;
import lk.kds_medical.asset.consultation.service.ConsultationService;
import lk.kds_medical.asset.doctor.entity.Doctor;
import lk.kds_medical.asset.doctor.service.DoctorService;
import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
import lk.kds_medical.asset.user_management.service.UserService;
import lk.kds_medical.util.service.DateTimeAgeService;
import lk.kds_medical.util.service.MakeAutoGenerateNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping( "/doctor" )
public class DoctorController {
  private final DoctorService doctorService;
  private final ConsultationService consultationService;
  private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;

  @Autowired
  public DoctorController(DoctorService doctorService, ConsultationService consultationService,
                          DateTimeAgeService dateTimeAgeService, UserService userService,
                          MakeAutoGenerateNumberService makeAutoGenerateNumberService) {
    this.doctorService = doctorService;
    this.consultationService = consultationService;
    this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
  }

  @GetMapping
  public String doctorPage(Model model) {
    model.addAttribute("doctors", doctorService.findAll());
    return "doctor/doctor";
  }

  @GetMapping( "/{id}" )
  public String doctorView(@PathVariable( "id" ) Integer id, Model model) {
    model.addAttribute("doctorDetail", doctorService.findById(id));
    return "doctor/doctor-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String editDoctorFrom(@PathVariable( "id" ) Integer id, Model model) {
    return common(model, doctorService.findById(id), false);
  }

  @GetMapping( value = "/add" )
  public String doctorAddFrom(Model model) {
    return common(model, new Doctor(), true);
  }

  private String common(Model model, Doctor doctor, boolean addStatus) {
    if ( addStatus ) {
      List< DoctorSchedule > doctorSchedules = new ArrayList<>();
      for ( DayOfWeek value : DayOfWeek.values() ) {
        DoctorSchedule doctorSchedule = new DoctorSchedule();
        doctorSchedule.setDoctor(doctor);
        doctorSchedule.setActive(false);
        doctorSchedule.setDayOfWeek(value);
        doctorSchedules.add(doctorSchedule);
      }
      doctor.setDoctorSchedules(doctorSchedules);
    }
    model.addAttribute("addStatus", addStatus);
    model.addAttribute("consultations", consultationService.findAll());
    model.addAttribute("doctor", doctor);
    model.addAttribute("title", Title.values());
    model.addAttribute("gender", Gender.values());
    return "doctor/addDoctor";
  }

  // Above method support to send data to front end - All List, update, edit
  //Bellow method support to do back end function save, delete, update, search

  @PostMapping( value = {"/save", "/update"} )
  public String addDoctor(@Valid @ModelAttribute Doctor doctor, BindingResult result, Model model) {

    if ( result.hasErrors() ) {
      return common(model, doctor, true);
    }

    if ( doctor.getId() == null ) {
      Doctor lastDoctor = doctorService.lastDoctor();
      if ( lastDoctor == null ) {
        doctor.setCode("KDSD" + makeAutoGenerateNumberService.numberAutoGen(null).toString());
      } else {
        doctor.setCode("KDSD" + makeAutoGenerateNumberService.numberAutoGen(lastDoctor.getCode().substring(4)).toString());
      }
    }
    List< DoctorSchedule > doctorSchedules = new ArrayList<>();
    doctor.getDoctorSchedules().forEach(x -> {
      x.setDoctor(doctor);
      doctorSchedules.add(x);
    });
    doctor.setDoctorSchedules(doctorSchedules);
    doctorService.persist(doctor);
    return "redirect:/doctor";
  }

  @GetMapping( value = "/remove/{id}" )
  public String removeDoctor(@PathVariable Integer id) {
    doctorService.delete(id);
    return "redirect:/doctor";
  }

}
