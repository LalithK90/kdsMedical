package lk.kds_medical.asset.doctor.controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.kds_medical.asset.common_asset.model.Enum.Gender;
import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.common_asset.model.Enum.Title;
import lk.kds_medical.asset.consultation.service.ConsultationService;
import lk.kds_medical.asset.doctor.entity.Doctor;
import lk.kds_medical.asset.doctor.service.DoctorService;
import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
import lk.kds_medical.asset.user_management.service.UserService;
import lk.kds_medical.util.service.DateTimeAgeService;
import lk.kds_medical.util.service.MakeAutoGenerateNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
      if ( x.isActive() ) {
        x.setDoctor(doctor);
        x.setLiveDead(LiveDead.ACTIVE);
        doctorSchedules.add(x);
      }
    });
    doctor.setDoctorSchedules(doctorSchedules);
    try {
      doctorService.persist(doctor);
    } catch ( Exception e ) {
      ObjectError error = new ObjectError("doctor",
                                          "Please make sure that resolve following error \n. <br> System message -->"
                                              + e.getCause().getCause().getMessage());
      result.addError(error);
      return common(model, doctor, true);
    }
    return "redirect:/doctor";
  }

  @GetMapping( value = "/remove/{id}" )
  public String removeDoctor(@PathVariable Integer id) {
    doctorService.delete(id);
    //todo if there is an payment need to change its status
    return "redirect:/doctor";
  }

  @GetMapping( "/doctorSchedule/{id}" )
  @ResponseBody
  public MappingJacksonValue doctorScheduleForDoctor(@PathVariable Integer id) {
    List< DoctorSchedule > doctorSchedules = new ArrayList<>();
    doctorService.findById(id).getDoctorSchedules()
        .stream()
        .filter(DoctorSchedule::isActive)
        .collect(Collectors.toList())
        .forEach(x -> {
          x.setCount(x.getAppointments().size());
          doctorSchedules.add(x);
        });

    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(doctorSchedules);

    SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "dayOfWeek", "arrivalTime", "count");

    FilterProvider filters = new SimpleFilterProvider()
        .addFilter("DoctorSchedule", simpleBeanPropertyFilter);

    mappingJacksonValue.setFilters(filters);
    return mappingJacksonValue;
  }

}
