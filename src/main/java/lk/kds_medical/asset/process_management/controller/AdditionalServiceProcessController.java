package lk.kds_medical.asset.process_management.controller;

import lk.kds_medical.asset.additional_service.service.AdditionalServiceService;
import lk.kds_medical.asset.appointment.entity.enums.AppointmentStatus;
import lk.kds_medical.asset.discount_ratio.service.DiscountRatioService;
import lk.kds_medical.asset.patient.service.PatientService;
import lk.kds_medical.asset.payment.entity.enums.PaymentMethod;
import lk.kds_medical.asset.payment.entity.enums.PaymentPrintOrNot;
import lk.kds_medical.asset.payment.service.PaymentService;
import lk.kds_medical.asset.payment_additional_service.entity.PaymentAdditionalService;
import lk.kds_medical.util.service.MakeAutoGenerateNumberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/additionServicePatient")
public class AdditionalServiceProcessController {
private final AdditionalServiceService additionalServiceService;
private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;
private final PatientService patientService;
private final PaymentService paymentService;
private final DiscountRatioService discountRatioService;


  public AdditionalServiceProcessController(AdditionalServiceService additionalServiceService,
                                            MakeAutoGenerateNumberService makeAutoGenerateNumberService,
                                            PatientService patientService, PaymentService paymentService,
                                            DiscountRatioService discountRatioService) {
    this.additionalServiceService = additionalServiceService;
    this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
    this.patientService = patientService;
    this.paymentService = paymentService;
    this.discountRatioService = discountRatioService;
  }
//todo:
  @GetMapping
  public String findAll(Model model){
    model.addAttribute("additionServicePatients", additionalServiceService.findAll());
    return "additionServicePatient/additionServicePatient";
  }

  @GetMapping("/add")
  public String add(Model model){
    model.addAttribute("additionalService", additionalServiceService.findAll());
    model.addAttribute("appointmentStatuses", AppointmentStatus.values());model.addAttribute("patients", patientService.findAll());
    model.addAttribute("invoicePrintOrNots", PaymentPrintOrNot.values());
    model.addAttribute("paymentMethods", PaymentMethod.values());
    model.addAttribute("discountRatios", discountRatioService.findAll());
    model.addAttribute("paymentAdditionalService", new PaymentAdditionalService());
    return "additionServicePatient/addAdditionServicePatient";
  }
  //todo save method
}
