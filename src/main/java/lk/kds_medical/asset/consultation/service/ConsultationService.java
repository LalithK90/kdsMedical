package lk.kds_medical.asset.consultation.service;


import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.consultation.dao.ConsultationDao;
import lk.kds_medical.asset.consultation.entity.Consultation;
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
public class ConsultationService implements AbstractService< Consultation, Integer > {

  private ConsultationDao consultationDao;

  @Autowired
  public ConsultationService(ConsultationDao consultationDao) {
    this.consultationDao = consultationDao;
  }


  @Cacheable( value = "Consultation" )
  public List< Consultation > findAll() {
    return consultationDao.findAll().stream().filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE)).collect(Collectors.toList());
  }


  public Consultation findById(Integer id) {
    return consultationDao.getOne(id);
  }


  public Consultation persist(Consultation consultation) {
    if ( consultation.getId() == null ) {
      consultation.setLiveDead(LiveDead.ACTIVE);
    }
    return consultationDao.save(consultation);
  }

  public boolean delete(Integer id) {
    Consultation consultation = consultationDao.getOne(id);
    consultation.setLiveDead(LiveDead.STOP);
    consultationDao.save(consultation);
    return false;
  }

  public List< Consultation > search(Consultation consultation) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< Consultation > consultationExample = Example.of(consultation, matcher);
    return consultationDao.findAll(consultationExample);
  }
}
