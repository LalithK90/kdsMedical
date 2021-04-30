package lk.kds_medical.asset.payment.controller;

import lk.kds_medical.asset.common_asset.model.TwoDate;
import lk.kds_medical.asset.employee.entity.Employee;
import lk.kds_medical.asset.employee.entity.enums.Designation;
import lk.kds_medical.asset.payment.entity.Payment;
import lk.kds_medical.asset.payment.entity.enums.PaymentMethod;
import lk.kds_medical.asset.payment.entity.enums.PaymentValidOrNot;
import lk.kds_medical.asset.payment.service.PaymentService;
import lk.kds_medical.asset.user_management.service.UserService;
import lk.kds_medical.util.service.DateTimeAgeService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/payment" )
public class PaymentController {
  private final PaymentService paymentService;
  private final DateTimeAgeService dateTimeAgeService;
  private final UserService userService;

  public PaymentController(PaymentService paymentService, DateTimeAgeService dateTimeAgeService,
                           UserService userService) {
    this.paymentService = paymentService;
    this.dateTimeAgeService = dateTimeAgeService;
    this.userService = userService;
  }

  private String payment(String searchUrl, List< Payment > payments, LocalDate startDate, LocalDate endDate,
                         Model model) {
    model.addAttribute("payments", payments);
    model.addAttribute("searchUrl", searchUrl);
    model.addAttribute("message", "This report is belong to " + startDate + " to " + endDate + "\n if you need to " +
        "more please use above search method");
    return "payment/payment";
  }

  private List< Payment > findPaymentList(LocalDate startDate, LocalDate endDate) {
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(startDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(endDate);

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Employee employee = userService.findByUserName(username).getEmployee();
    List< Payment > payments = new ArrayList<>();
    if ( !employee.getDesignation().equals(Designation.MANAGER) ) {
      payments.addAll(paymentService.findByCreatedAtIsBetweenAndCreatedBy(startDateTime, endDateTime,
                                                                          username));
    } else {
      payments.addAll(paymentService.findByCreatedAtIsBetween(startDateTime, endDateTime));
    }
    return payments;
  }

  @GetMapping
  public String findAll(Model model) {
    LocalDate startDate = dateTimeAgeService.getPastDateByMonth(3);
    LocalDate endDate = LocalDate.now();
    return payment("search", findPaymentList(startDate, endDate), startDate, endDate, model);
  }

  @PostMapping
  public String findAllSearch(@ModelAttribute TwoDate twoDate, Model model) {
    return payment("search", findPaymentList(twoDate.getStartDate(), twoDate.getEndDate()), twoDate.getStartDate(),
                   twoDate.getEndDate(), model);
  }

  @GetMapping( "/additional" )
  public String findAllAdditional(Model model) {
    LocalDate startDate = dateTimeAgeService.getPastDateByMonth(3);
    LocalDate endDate = LocalDate.now();
    var payments =
        findPaymentList(startDate, endDate).stream().filter(x -> x.getPaymentAdditionalService() != null).collect(Collectors.toList());
    return payment("additional", payments, startDate, endDate, model);
  }

  @PostMapping( "/additional" )
  public String findAllAdditionalSearch(@ModelAttribute TwoDate twoDate, Model model) {
    var payments =
        findPaymentList(twoDate.getStartDate(), twoDate.getEndDate()).stream().filter(x -> x.getPaymentAdditionalService() != null).collect(Collectors.toList());
    return payment("additional", payments, twoDate.getStartDate(),
                   twoDate.getEndDate(), model);
  }

  @GetMapping( "/appointment" )
  public String findAllAppointment(Model model) {
    LocalDate startDate = dateTimeAgeService.getPastDateByMonth(3);
    LocalDate endDate = LocalDate.now();
    var payments =
        findPaymentList(startDate, endDate).stream().filter(x -> x.getAppointment() != null).collect(Collectors.toList());
    return payment("appointment", payments, startDate, endDate, model);
  }

  @PostMapping( "/appointment" )
  public String findAllAppointmentSearch(@ModelAttribute TwoDate twoDate, Model model) {
    var payments =
        findPaymentList(twoDate.getStartDate(), twoDate.getEndDate()).stream().filter(x -> x.getAppointment() != null).collect(Collectors.toList());
    return payment("appointment", payments, twoDate.getStartDate(),
                   twoDate.getEndDate(), model);
  }

  @GetMapping( "/card" )
  public String findAllCard(Model model) {
    LocalDate startDate = dateTimeAgeService.getPastDateByMonth(3);
    LocalDate endDate = LocalDate.now();
    var payments =
        findPaymentList(startDate, endDate).stream().filter(x -> x.getPaymentMethod().equals(PaymentMethod.CREDIT)).collect(Collectors.toList());
    return payment("card", payments, startDate, endDate, model);
  }

  @PostMapping( "/card" )
  public String findAllCard(@ModelAttribute TwoDate twoDate, Model model) {
    var payments =
        findPaymentList(twoDate.getStartDate(), twoDate.getEndDate()).stream().filter(x -> x.getPaymentMethod().equals(PaymentMethod.CREDIT)).collect(Collectors.toList());
    return payment("card", payments, twoDate.getStartDate(),
                   twoDate.getEndDate(), model);
  }

  @GetMapping( "/cash" )
  public String findAllCash(Model model) {
    LocalDate startDate = dateTimeAgeService.getPastDateByMonth(3);
    LocalDate endDate = LocalDate.now();
    var payments =
        findPaymentList(startDate, endDate).stream().filter(x -> x.getPaymentMethod().equals(PaymentMethod.CASH)).collect(Collectors.toList());
    return payment("cash", payments, startDate, endDate, model);
  }

  @PostMapping( "/cash" )
  public String findAllCash(@ModelAttribute TwoDate twoDate, Model model) {
    var payments =
        findPaymentList(twoDate.getStartDate(), twoDate.getEndDate()).stream().filter(x -> x.getPaymentMethod().equals(PaymentMethod.CASH)).collect(Collectors.toList());
    return payment("cash", payments, twoDate.getStartDate(),
                   twoDate.getEndDate(), model);
  }

  @GetMapping( "/valid" )
  public String findAllValid(Model model) {
    LocalDate startDate = dateTimeAgeService.getPastDateByMonth(3);
    LocalDate endDate = LocalDate.now();
    var payments =
        findPaymentList(startDate, endDate).stream().filter(x -> x.getPaymentValidOrNot().equals(PaymentValidOrNot.VALID)).collect(Collectors.toList());
    return payment("valid", payments, startDate, endDate, model);
  }

  @PostMapping( "/valid" )
  public String findAllValid(@ModelAttribute TwoDate twoDate, Model model) {
    var payments =
        findPaymentList(twoDate.getStartDate(), twoDate.getEndDate()).stream().filter(x -> x.getPaymentValidOrNot().equals(PaymentValidOrNot.VALID)).collect(Collectors.toList());
    return payment("valid", payments, twoDate.getStartDate(),
                   twoDate.getEndDate(), model);
  }

  @GetMapping( "/cancel" )
  public String findAllCancel(Model model) {
    LocalDate startDate = dateTimeAgeService.getPastDateByMonth(3);
    LocalDate endDate = LocalDate.now();
    var payments =
        findPaymentList(startDate, endDate).stream().filter(x -> x.getPaymentValidOrNot().equals(PaymentValidOrNot.NOT_VALID)).collect(Collectors.toList());
    return payment("cancel", payments, startDate, endDate, model);
  }

  @PostMapping( "/cancel" )
  public String findAllCancel(@ModelAttribute TwoDate twoDate, Model model) {
    var payments =
        findPaymentList(twoDate.getStartDate(), twoDate.getEndDate()).stream().filter(x -> x.getPaymentValidOrNot().equals(PaymentValidOrNot.NOT_VALID)).collect(Collectors.toList());
    return payment("cancel", payments, twoDate.getStartDate(),
                   twoDate.getEndDate(), model);
  }


  @GetMapping( "/{id}" )
  public String view(@PathVariable( "id" ) Integer id, Model model) {
    model.addAttribute("paymentDetail", paymentService.findById(id));
    return "payment/payment-detail";
  }

}
