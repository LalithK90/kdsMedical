package lk.kds_medical.asset.report;

import lk.kds_medical.asset.additional_service.entity.AdditionalService;
import lk.kds_medical.asset.appointment.service.AppointmentService;
import lk.kds_medical.asset.doctor.service.DoctorService;
import lk.kds_medical.asset.doctor_schedule.service.DoctorScheduleService;
import lk.kds_medical.asset.patient.service.PatientService;
import lk.kds_medical.asset.payment.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportController {
private final PaymentService paymentService;
private final PatientService patientService;
private final AppointmentService appointmentService;
private final DoctorService doctorService;
private final DoctorScheduleService doctorScheduleService;
private final AdditionalService additionalService;

  public ReportController(PaymentService paymentService, PatientService patientService,
                          AppointmentService appointmentService, DoctorService doctorService, DoctorScheduleService doctorScheduleService, AdditionalService additionalService) {
    this.paymentService = paymentService;
    this.patientService = patientService;
    this.appointmentService = appointmentService;
    this.doctorService = doctorService;
    this.doctorScheduleService = doctorScheduleService;
    this.additionalService = additionalService;
  }



}
