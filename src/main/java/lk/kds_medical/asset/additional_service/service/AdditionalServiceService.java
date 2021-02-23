package lk.kds_medical.asset.additional_service.service;


import lk.kds_medical.asset.additional_service.dao.AdditionalServiceDao;
import lk.kds_medical.asset.additional_service.entity.AdditionalService;
import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.additional_service.entity.AdditionalService;
import lk.kds_medical.util.interfaces.AbstractService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdditionalServiceService implements AbstractService< AdditionalService, Integer > {
    private final AdditionalServiceDao additionalServiceDao;

    public AdditionalServiceService(AdditionalServiceDao additionalServiceDao) {
        this.additionalServiceDao = additionalServiceDao;
    }


    public List< AdditionalService > findAll() {
        return additionalServiceDao.findAll().stream()
            .filter(x->x.getLiveDead().equals(LiveDead.ACTIVE))
            .collect(Collectors.toList());
    }

    public AdditionalService findById(Integer id) {
        return additionalServiceDao.getOne(id);
    }

    public AdditionalService persist(AdditionalService additionalService) {
        if(additionalService.getId()==null){
            additionalService.setLiveDead(LiveDead.ACTIVE);}
        return additionalServiceDao.save(additionalService);
    }

    public boolean delete(Integer id) {
        AdditionalService additionalService =  additionalServiceDao.getOne(id);
        additionalService.setLiveDead(LiveDead.STOP);
        additionalServiceDao.save(additionalService);
        return false;
    }

    public List< AdditionalService > search(AdditionalService additionalService) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< AdditionalService > additionalServiceExample = Example.of(additionalService, matcher);

        return additionalServiceDao.findAll(additionalServiceExample);
    }
}
