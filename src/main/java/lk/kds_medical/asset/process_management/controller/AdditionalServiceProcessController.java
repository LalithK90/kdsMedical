package lk.kds_medical.asset.process_management.controller;

import lk.kds_medical.asset.additional_service.controller.AdditionalServiceController;
import lk.kds_medical.asset.additional_service.service.AdditionalServiceService;
import lk.kds_medical.asset.common_asset.model.TwoDate;
import lk.kds_medical.asset.discount_ratio.service.DiscountRatioService;
import lk.kds_medical.asset.doctor.service.DoctorService;
import lk.kds_medical.asset.employee.entity.Employee;
import lk.kds_medical.asset.employee.entity.enums.Designation;
import lk.kds_medical.asset.employee.service.EmployeeService;
import lk.kds_medical.asset.patient.service.PatientService;
import lk.kds_medical.asset.payment.entity.enums.PaymentMethod;
import lk.kds_medical.asset.payment.entity.enums.PaymentPrintOrNot;
import lk.kds_medical.asset.payment.service.PaymentService;
import lk.kds_medical.asset.payment_additional_service.entity.PaymentAdditionalService;
import lk.kds_medical.asset.user_management.service.UserService;
import lk.kds_medical.util.service.DateTimeAgeService;
import lk.kds_medical.util.service.MakeAutoGenerateNumberService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping( "/additionServicePatient" )
public class AdditionalServiceProcessController {
  private final AdditionalServiceService additionalServiceService;
  private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;
  private final PatientService patientService;
  private final DoctorService doctorService;
  private final PaymentService paymentService;
  private final DiscountRatioService discountRatioService;
  private final DateTimeAgeService dateTimeAgeService;
  private final EmployeeService employeeService;
  private final UserService userService;


  public AdditionalServiceProcessController(AdditionalServiceService additionalServiceService,
                                            MakeAutoGenerateNumberService makeAutoGenerateNumberService,
                                            PatientService patientService, DoctorService doctorService,
                                            PaymentService paymentService,
                                            DiscountRatioService discountRatioService,
                                            DateTimeAgeService dateTimeAgeService, EmployeeService employeeService,
                                            UserService userService) {
    this.additionalServiceService = additionalServiceService;
    this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
    this.patientService = patientService;
    this.doctorService = doctorService;
    this.paymentService = paymentService;
    this.discountRatioService = discountRatioService;
    this.dateTimeAgeService = dateTimeAgeService;
    this.employeeService = employeeService;
    this.userService = userService;
  }

  private String commonFindAll(Model model, LocalDate startDate, LocalDate endDate) {
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(startDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(endDate);

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Employee employee = userService.findByUserName(username).getEmployee();
    if ( !employee.getDesignation().equals(Designation.MANAGER) ) {
      model.addAttribute("additionServicePatients",
                         additionalServiceService.findByCreatedAtIsBetweenAndCreatedBy(startDateTime, endDateTime, username));
    } else {
      model.addAttribute("additionServicePatients", additionalServiceService.findByCreatedAtIsBetween(startDateTime,
                                                                                                      endDateTime));
    }
    model.addAttribute("message", "This report is belong to " + startDate + " to " + endDate + "\n if you need to " +
        "more please use above search method");
    return "additionServicePatient/additionServicePatient";
  }

  //todo:
  @GetMapping
  public String findAll(Model model) {
    return commonFindAll(model, dateTimeAgeService.getPastDateByMonth(3), LocalDate.now());
  }

  @PostMapping( "/search" )
  public String findAllSearch(@ModelAttribute TwoDate twoDate, Model model) {
    return commonFindAll(model, twoDate.getStartDate(), twoDate.getEndDate());
  }

  @GetMapping( "/add" )
  public String add(Model model) {
    model.addAttribute("additionalServices", additionalServiceService.findAll());
    model.addAttribute("patients", patientService.findAll());
    model.addAttribute("invoicePrintOrNots", PaymentPrintOrNot.values());
    model.addAttribute("paymentMethods", PaymentMethod.values());
    model.addAttribute("doctors", doctorService.findAll());
    model.addAttribute("discountRatios", discountRatioService.findAll());
    model.addAttribute("paymentAdditionalService", new PaymentAdditionalService());
    model.addAttribute("additionalServicePriceUrl", MvcUriComponentsBuilder
                           .fromMethodName(AdditionalServiceController.class, "findPriceById", "")
                           .build()
                           .toString()
                      );
    return "additionServicePatient/addAdditionServicePatient";
  }
  //todo save method
}
