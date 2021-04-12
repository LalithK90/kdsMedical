package lk.kds_medical.asset.payment.controller;

import lk.kds_medical.asset.payment.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( "/payment" )
public class PaymentController {
  private final PaymentService paymentService;

  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @GetMapping
  public String finaAll(Model model) {
    model.addAttribute("payments", paymentService.findAll());
    return "payment/payment";
  }

  @GetMapping( "/{id}" )
  public String view(@PathVariable( "id" ) Integer id, Model model) {
    model.addAttribute("paymentDetail", paymentService.findById(id));
    return "payment/payment-detail";
  }

}
