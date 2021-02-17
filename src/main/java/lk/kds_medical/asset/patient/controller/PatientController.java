package lk.kds_medical.asset.patient.controller;


import lk.kds_medical.asset.commonAsset.service.CommonService;
import lk.kds_medical.asset.patient.entity.Patient;
import lk.kds_medical.asset.patient.service.PatientService;
import lk.kds_medical.asset.userManagement.service.UserService;
import lk.kds_medical.util.service.DateTimeAgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Controller
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final DateTimeAgeService dateTimeAgeService;
    private final CommonService commonService;
    private final UserService userService;

    @Autowired
    public PatientController(PatientService patientService,DateTimeAgeService dateTimeAgeService, CommonService commonService,
                              UserService userService) {
        this.patientService = patientService;
        this.dateTimeAgeService = dateTimeAgeService;
        this.commonService = commonService;
        this.userService = userService;
    }
//----> Employee details management - start <----//

    // Common things for an employee add and update
    private String commonThings(Model model) {
        commonService.commonEmployeeAndOffender(model);
        return "patient/addPatient";
    }

   /* //When scr called file will send to
    @GetMapping( "/file/{filename}" )
    public ResponseEntity< byte[] > downloadFile(@PathVariable( "filename" ) String filename) {
        EmployeeFiles file = employeeFilesService.findByNewID(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getPic());
    }*/

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

        patient.setMobileOne(commonService.commonMobileNumberLengthValidator(patient.getMobileOne()));
        patient.setMobileTwo(commonService.commonMobileNumberLengthValidator(patient.getMobileTwo()));
        //after save employee files and save employee
        patientService.persist(patient);
       /* try {
            patient.setMobileOne(commonService.commonMobileNumberLengthValidator(patient.getMobileOne()));
            patient.setMobileTwo(commonService.commonMobileNumberLengthValidator(patient.getMobileTwo()));
            //after save employee files and save employee
            patientService.persist(patient);

            //if employee state is not working he or she cannot access to the system
            *//*if ( !patient.getEmployeeStatus().equals(EmployeeStatus.WORKING) ) {
                User user = userService.findUserByEmployee(employeeService.findByNic(employee.getNic()));
                //if employee not a user
                if ( user != null ) {
                    user.setEnabled(false);
                    userService.persist(user);
                }
            }*//*
            //save employee images file
          *//*  for ( MultipartFile file : employee.getFiles() ) {
                if ( file.getOriginalFilename() != null ) {
                    EmployeeFiles employeeFiles = employeeFilesService.findByName(file.getOriginalFilename());
                    if ( employeeFiles != null ) {
                        // update new contents
                        employeeFiles.setPic(file.getBytes());
                        // Save all to database
                    } else {
                        employeeFiles = new EmployeeFiles(file.getOriginalFilename(),
                                file.getContentType(),
                                file.getBytes(),
                                employee.getNic().concat("-" + LocalDateTime.now()),
                                UUID.randomUUID().toString().concat("employee"));
                        employeeFiles.setEmployee(employee);
                    }
                    employeeFilesService.persist(employeeFiles);
                }
            }*//*
            return "redirect:/patient";

        } catch ( Exception e ) {
            ObjectError error = new ObjectError("patient",
                    "There is already in the system. <br>System message -->" + e.toString());
            result.addError(error);
            model.addAttribute("addStatus", true);
            model.addAttribute("patient", patient);
            return commonThings(model);
        }*/

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

//----> Employee details management - end <----//
    //````````````````````````````````````````````````````````````````````````````//
//----> EmployeeWorkingPlace - details management - start <----//

  /*  //Send form to add working place before find employee
    @GetMapping( value = "/workingPlace" )
    public String addEmployeeWorkingPlaceForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("employeeDetailShow", false);
        return "employeeWorkingPlace/addEmployeeWorkingPlace";
    }
*/
    //Send a searched employee to add working place
/*
    @PostMapping( value = "/workingPlace" )
    public String addWorkingPlaceEmployeeDetails(@ModelAttribute( "employee" ) Employee employee, Model model) {

        List< Employee > employees = employeeService.search(employee);
        if ( employees.size() == 1 ) {
            model.addAttribute("employeeDetailShow", true);
            model.addAttribute("employeeNotFoundShow", false);
            model.addAttribute("employeeDetail", employees.get(0));
            model.addAttribute("files", employeeFilesService.employeeFileDownloadLinks(employee).get(0));
            model.addAttribute("employeeWorkingPlaceHistoryObject", new EmployeeWorkingPlaceHistory());
            model.addAttribute("workingPlaceChangeReason", WorkingPlaceChangeReason.values());
            model.addAttribute("province", Province.values());
            model.addAttribute("districtUrl", MvcUriComponentsBuilder
                    .fromMethodName(WorkingPlaceRestController.class, "getDistrict", "")
                    .build()
                    .toString());
            model.addAttribute("stationUrl", MvcUriComponentsBuilder
                    .fromMethodName(WorkingPlaceRestController.class, "getStation", "")
                    .build()
                    .toString());
            return "employeeWorkingPlace/addEmployeeWorkingPlace";
        }
        model.addAttribute("employee", new Employee());
        model.addAttribute("employeeDetailShow", false);
        model.addAttribute("employeeNotFoundShow", true);
        model.addAttribute("employeeNotFound", "There is not employee in the system according to the provided details" +
                " \n Could you please search again !!");

        return "employeeWorkingPlace/addEmployeeWorkingPlace";
    }

    @PostMapping( value = "/workingPlace/add" )
    public String addWorkingPlaceEmployee(@ModelAttribute( "employeeWorkingPlaceHistory" ) EmployeeWorkingPlaceHistory employeeWorkingPlaceHistory, Model model) {
        System.out.println(employeeWorkingPlaceHistory.toString());
        // -> need to write validation before the save working place
        //before saving set employee current working palace
        WorkingPlace workingPlace = employeeWorkingPlaceHistory.getWorkingPlace();

        employeeWorkingPlaceHistory.setWorkingPlace(employeeWorkingPlaceHistory.getEmployee().getWorkingPlace());
        employeeWorkingPlaceHistory.getEmployee().setWorkingPlace(workingPlace);

        employeeWorkingPlaceHistory.setWorkingDuration(dateTimeAgeService.dateDifference(employeeWorkingPlaceHistory.getFrom_place(), employeeWorkingPlaceHistory.getTo_place()));
        employeeWorkingPlaceHistoryService.persist(employeeWorkingPlaceHistory);
        return "redirect:/employee";
    }
*/

//----> EmployeeWorkingPlace - details management - end <----//

}

