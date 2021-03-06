package lk.kds_medical.asset.payment.service;


import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.payment.dao.PaymentDao;
import lk.kds_medical.asset.payment.entity.Payment;
import lk.kds_medical.asset.payment.entity.enums.PaymentValidOrNot;
import lk.kds_medical.asset.payment_additional_service.entity.PaymentAdditionalService;
import lk.kds_medical.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService implements AbstractService< Payment, Integer > {

  private PaymentDao paymentDao;

  @Autowired
  public PaymentService(PaymentDao paymentDao) {
    this.paymentDao = paymentDao;
  }


  @Cacheable( value = "Payment" )
  public List< Payment > findAll() {
    return paymentDao.findAll().stream()
        .filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE) && x.getPaymentValidOrNot().equals(PaymentValidOrNot.VALID))
        .collect(Collectors.toList());
  }


  public Payment findById(Integer id) {
    return paymentDao.getOne(id);
  }


  public Payment persist(Payment payment) {
    if ( payment.getId() == null ) {
      payment.setLiveDead(LiveDead.ACTIVE);
    }
    return paymentDao.save(payment);
  }

  public boolean delete(Integer id) {
    Payment payment = paymentDao.getOne(id);
    payment.setLiveDead(LiveDead.STOP);
    paymentDao.save(payment);
    return false;
  }


  public List< Payment > search(Payment payment) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< Payment > consultationExample = Example.of(payment, matcher);
    return paymentDao.findAll(consultationExample);
  }

  public Payment lastPayment() {
    return paymentDao.findFirstByOrderByIdDesc();
  }

  public Payment findByAppointment(Appointment appointment) {
    return paymentDao.findByAppointment(appointment);
  }

  public Payment findByPaymentAdditionalService(PaymentAdditionalService paymentAdditionalService) {
    return paymentDao.findByPaymentAdditionalService(paymentAdditionalService);
  }

  public List< Payment > findByCreatedAtIsBetweenAndCreatedBy(LocalDateTime startDateTime, LocalDateTime endDateTime,
                                                              String username) {
    return paymentDao.findByCreatedAtIsBetweenAndCreatedBy(startDateTime, endDateTime, username);
  }

  public List< Payment > findByCreatedAtIsBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return paymentDao.findByCreatedAtIsBetween(startDateTime, endDateTime);
  }

  public List< Payment> findByUpdatedBy(String username) {
  return paymentDao.findByUpdatedBy(username);
  }

  public List< Payment> findByUpdatedAtIsBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return paymentDao.findByUpdatedAtIsBetween(startDateTime,endDateTime);
  }
}
