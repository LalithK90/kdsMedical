package lk.kds_medical.asset.payment_additional_service.service;



import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.employee.entity.Employee;
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
import java.util.stream.Collectors;

@Service
public class PaymentAdditionalServiceService implements AbstractService< PaymentAdditionalService, Integer> {

    private PaymentAdditionalServiceDao paymentAdditionalServiceDao;
    @Autowired
    public PaymentAdditionalServiceService(PaymentAdditionalServiceDao paymentAdditionalServiceDao){
        this.paymentAdditionalServiceDao = paymentAdditionalServiceDao;
    }


    @Cacheable(value = "PaymentAdditionalService")
    public List<PaymentAdditionalService> findAll() {
        return paymentAdditionalServiceDao.findAll().stream()
            .filter(x->x.getLiveDead().equals(LiveDead.ACTIVE))
            .collect(Collectors.toList());
    }


    public PaymentAdditionalService findById(Integer id) {
        return paymentAdditionalServiceDao.getOne(id);
    }


    public PaymentAdditionalService persist(PaymentAdditionalService paymentAdditionalService) {
        if(paymentAdditionalService.getId()==null){
            paymentAdditionalService.setLiveDead(LiveDead.ACTIVE);}
        return paymentAdditionalServiceDao.save(paymentAdditionalService);
    }

    public boolean delete(Integer id) {
        PaymentAdditionalService paymentAdditionalService =  paymentAdditionalServiceDao.getOne(id);
        paymentAdditionalService.setLiveDead(LiveDead.STOP);
        paymentAdditionalServiceDao.save(paymentAdditionalService);
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

    public PaymentAdditionalService lastPaymentAdditionalService() {
    return paymentAdditionalServiceDao.findFirstByOrderByIdDesc();
    }
}
