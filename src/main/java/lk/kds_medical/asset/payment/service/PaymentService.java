package lk.kds_medical.asset.payment.service;


import lk.kds_medical.asset.payment.dao.PaymentDao;
import lk.kds_medical.asset.payment.entity.Payment;
import lk.kds_medical.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PaymentService implements AbstractService<Payment, Integer> {

    private PaymentDao paymentDao;
    @Autowired
    public PaymentService(PaymentDao paymentDao){
        this.paymentDao = paymentDao;
    }


    @Cacheable(value = "Payment")
    public List<Payment> findAll() {
        return paymentDao.findAll();
    }


    public Payment findById(Integer id) {
        return paymentDao.getOne(id);
    }

    @Transactional
    public Payment persist(Payment payment) {
        return paymentDao.save(payment);
    }

    @Transactional
    public boolean delete(Integer id) {
        paymentDao.deleteById(id);
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
}
