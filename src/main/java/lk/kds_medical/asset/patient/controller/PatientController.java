package lk.kds_medical.asset.patient.controller;


import lk.kds_medical.asset.common_asset.model.Enum.BloodGroup;
import lk.kds_medical.asset.common_asset.model.Enum.CivilStatus;
import lk.kds_medical.asset.common_asset.model.Enum.Gender;
import lk.kds_medical.asset.common_asset.model.Enum.Title;
import lk.kds_medical.asset.employee.entity.enums.Designation;
import lk.kds_medical.asset.employee.entity.enums.EmployeeStatus;
import lk.kds_medical.asset.patient.entity.Patient;
import lk.kds_medical.asset.patient.service.PatientService;
import lk.kds_medical.asset.user_management.service.UserService;
import lk.kds_medical.util.service.DateTimeAgeService;
import lk.kds_medical.util.service.MakeAutoGenerateNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping( "/patient" )
public class PatientController {

  private final PatientService patientService;
  private final DateTimeAgeService dateTimeAgeService;
  private final UserService userService;
  private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;

  @Autowired
  public PatientController(PatientService patientService, DateTimeAgeService dateTimeAgeService,
                           UserService userService, MakeAutoGenerateNumberService makeAutoGenerateNumberService) {
    this.patientService = patientService;
    this.dateTimeAgeService = dateTimeAgeService;
    this.userService = userService;
    this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
  }

  private String commonThings(Model model) {
    model.addAttribute("title", Title.values());
    model.addAttribute("gender", Gender.values());
    model.addAttribute("civilStatus", CivilStatus.values());
    model.addAttribute("employeeStatus", EmployeeStatus.values());
    model.addAttribute("designation", Designation.values());
    model.addAttribute("bloodGroup", BloodGroup.values());
    return "patient/addPatient";
  }

  //Send all employee data
  @RequestMapping
  public String patientPage(Model model) {
    model.addAttribute("patients", patientService.findAll());
    return "patient/patient";
  }

  //Send on employee details
  @GetMapping( value = "/{id}" )
  public String patientView(@PathVariable( "id" ) Integer id, Model model) {
    Patient patient = patientService.findById(id);
    model.addAttribute("patientDetail", patient);
    model.addAttribute("addStatus", false);
    return "patient/patient-detail";
  }

  //Send employee data edit
  @GetMapping( value = "/edit/{id}" )
  public String editPatientForm(@PathVariable( "id" ) Integer id, Model model) {
    Patient patient = patientService.findById(id);
    model.addAttribute("patient", patient);
    model.addAttribute("newPatient", patient.getCode());
    model.addAttribute("addStatus", false);
    return commonThings(model);
  }

  //Send an employee add form
  @GetMapping( value = {"/add"} )
  public String patientAddForm(Model model) {
    model.addAttribute("addStatus", true);
    model.addAttribute("patient", new Patient());
    return commonThings(model);
  }

  //Employee add and update
  @PostMapping( value = {"/save", "/update"} )
  public String addPatient(@Valid @ModelAttribute Patient patient, BindingResult result, Model model
                          ) {

    if ( result.hasErrors() ) {
      model.addAttribute("addStatus", true);
      model.addAttribute("patient", patient);
      return commonThings(model);
    }

    patient.setMobileOne(makeAutoGenerateNumberService.commonMobileNumberLengthValidator(patient.getMobileOne()));
    patient.setMobileTwo(makeAutoGenerateNumberService.commonMobileNumberLengthValidator(patient.getMobileTwo()));

    if ( patient.getId() == null ) {
      Patient lastPatient = patientService.lastPatient();
      if ( lastPatient == null ) {
        patient.setCode("KDSD" + makeAutoGenerateNumberService.numberAutoGen(null).toString());
      } else {
        patient.setCode("KDSD" + makeAutoGenerateNumberService.numberAutoGen(lastPatient.getCode().substring(4)).toString());
      }
    }
    try {
      patientService.persist(patient);
    } catch ( Exception e ) {
      ObjectError error = new ObjectError("patient",
                                          "Please make sure that resolve following error \n. <br> System message -->" + e.getCause().getCause().getMessage());
      result.addError(error);
      model.addAttribute("addStatus", true);
      model.addAttribute("patient", patient);
      return commonThings(model);
    }


    return "redirect:/patient";
  }

  //If need to employee {but not applicable for this }
  @GetMapping( value = "/remove/{id}" )
  public String removePatient(@PathVariable Integer id) {
    patientService.delete(id);
    return "redirect:/patient";
  }

  //To search employee any giving employee parameter
  @GetMapping( value = "/search" )
  public String search(Model model, Patient patient) {
    model.addAttribute("patientDetail", patientService.search(patient));
    return "patient/patient-detail";
  }
}
