package lk.kds_medical.asset.payment_appointment.service;


import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.employee.entity.Employee;
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
import java.util.stream.Collectors;

@Service
public class PaymentAppointmentService implements AbstractService< PaymentAppointment, Integer > {

  private PaymentAppointmentDao paymentAppointmentDao;

  @Autowired
  public PaymentAppointmentService(PaymentAppointmentDao paymentAppointmentDao) {
    this.paymentAppointmentDao = paymentAppointmentDao;
  }


  @Cacheable( value = "PaymentAppointment" )
  public List< PaymentAppointment > findAll() {
    return paymentAppointmentDao.findAll().stream()
        .filter(x->x.getLiveDead().equals(LiveDead.ACTIVE))
        .collect(Collectors.toList());
  }


  public PaymentAppointment findById(Integer id) {
    return paymentAppointmentDao.getOne(id);
  }


  public PaymentAppointment persist(PaymentAppointment paymentAppointment) {
    if(paymentAppointment.getId()==null){
      paymentAppointment.setLiveDead(LiveDead.ACTIVE);}
    return paymentAppointmentDao.save(paymentAppointment);
  }

  public boolean delete(Integer id) {
    PaymentAppointment paymentAppointment =  paymentAppointmentDao.getOne(id);
    paymentAppointment.setLiveDead(LiveDead.STOP);
    paymentAppointmentDao.save(paymentAppointment);
    return false;
  }


  public List< PaymentAppointment > search(PaymentAppointment paymentAppointment) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< PaymentAppointment > consultationExample = Example.of(paymentAppointment, matcher);
    return paymentAppointmentDao.findAll(consultationExample);
  }
}
