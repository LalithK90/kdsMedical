package lk.kds_medical.asset.payment_appointment.service;


import lk.kds_medical.asset.payment_appointment.dao.PaymentAppointmentDao;
import lk.kds_medical.asset.payment_appointment.entity.PaymentAppointment;
import lk.kds_medical.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PaymentAppointmentService implements AbstractService< PaymentAppointment, Integer > {

  private PaymentAppointmentDao paymentAppointmentDao;

  @Autowired
  public PaymentAppointmentService(PaymentAppointmentDao paymentAppointmentDao) {
    this.paymentAppointmentDao = paymentAppointmentDao;
  }


  @Cacheable( value = "PaymentAppointment" )
  public List< PaymentAppointment > findAll() {
    return paymentAppointmentDao.findAll();
  }


  public PaymentAppointment findById(Integer id) {
    return paymentAppointmentDao.getOne(id);
  }

  @Transactional
  public PaymentAppointment persist(PaymentAppointment payment) {
    return paymentAppointmentDao.save(payment);
  }

  @Transactional
  public boolean delete(Integer id) {
    paymentAppointmentDao.deleteById(id);
    return false;
  }


  public List< PaymentAppointment > search(PaymentAppointment payment) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< PaymentAppointment > consultationExample = Example.of(payment, matcher);
    return paymentAppointmentDao.findAll(consultationExample);
  }
}
