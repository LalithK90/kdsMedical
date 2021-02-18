package lk.kds_medical.asset.payment_additional_service.service;



import lk.kds_medical.asset.payment_additional_service.dao.PaymentAdditionalServiceDao;
import lk.kds_medical.asset.payment_additional_service.entity.PaymentAdditionalService;
import lk.kds_medical.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PaymentAdditionalServiceService implements AbstractService< PaymentAdditionalService, Integer> {

    private PaymentAdditionalServiceDao paymentAdditionalServiceDao;
    @Autowired
    public PaymentAdditionalServiceService(PaymentAdditionalServiceDao paymentAdditionalServiceDao){
        this.paymentAdditionalServiceDao = paymentAdditionalServiceDao;
    }


    @Cacheable(value = "PaymentAdditionalService")
    public List<PaymentAdditionalService> findAll() {
        return paymentAdditionalServiceDao.findAll();
    }


    public PaymentAdditionalService findById(Integer id) {
        return paymentAdditionalServiceDao.getOne(id);
    }

    @Transactional
    public PaymentAdditionalService persist(PaymentAdditionalService paymentAdditionalService) {
        return paymentAdditionalServiceDao.save(paymentAdditionalService);
    }

    @Transactional
    public boolean delete(Integer id) {
        paymentAdditionalServiceDao.deleteById(id);
        return false;
    }


    public List<PaymentAdditionalService> search(PaymentAdditionalService paymentAdditionalService) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<PaymentAdditionalService> consultationExample = Example.of(paymentAdditionalService, matcher);
        return paymentAdditionalServiceDao.findAll(consultationExample);
    }
}
