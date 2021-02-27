package lk.kds_medical.asset.payment.service;


import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.payment.dao.PaymentDao;
import lk.kds_medical.asset.payment.entity.Payment;
import lk.kds_medical.asset.payment_additional_service.entity.PaymentAdditionalService;
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
public class PaymentService implements AbstractService<Payment, Integer> {

    private PaymentDao paymentDao;
    @Autowired
    public PaymentService(PaymentDao paymentDao){
        this.paymentDao = paymentDao;
    }


    @Cacheable(value = "Payment")
    public List<Payment> findAll() {
        return paymentDao.findAll().stream()
            .filter(x->x.getLiveDead().equals(LiveDead.ACTIVE))
            .collect(Collectors.toList());
    }


    public Payment findById(Integer id) {
        return paymentDao.getOne(id);
    }


    public Payment persist(Payment payment) {
        if(payment.getId()==null){
            payment.setLiveDead(LiveDead.ACTIVE);}
        return paymentDao.save(payment);
    }

    public boolean delete(Integer id) {
        Payment payment =  paymentDao.getOne(id);
        payment.setLiveDead(LiveDead.STOP);
        paymentDao.save(payment);
        return false;
    }



    public List<Payment> search(Payment payment) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Payment> consultationExample = Example.of(payment, matcher);
        return paymentDao.findAll(consultationExample);
    }

  public Payment lastPayment() {
        return paymentDao.findFirstByOrderByIdDesc();
  }
}
