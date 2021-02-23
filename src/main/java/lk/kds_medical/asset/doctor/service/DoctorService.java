package lk.kds_medical.asset.doctor.service;


import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.doctor.dao.DoctorDao;
import lk.kds_medical.asset.doctor.entity.Doctor;
import lk.kds_medical.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService implements AbstractService< Doctor, Integer > {

  private final DoctorDao doctorDao;

  @Autowired
  public DoctorService(DoctorDao doctorDao) {
    this.doctorDao = doctorDao;
  }


  @Cacheable( value = "doctor" )
  public List< Doctor > findAll() {
    return doctorDao.findAll().stream().filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE)).collect(Collectors.toList());
  }

  @CachePut( value = "doctor" )
  public Doctor findById(Integer id) {
    return doctorDao.getOne(id);
  }

  @CachePut( value = "doctor" )

  public Doctor persist(Doctor doctor) {
    if ( doctor.getId() == null ) {
      doctor.setLiveDead(LiveDead.ACTIVE);
    }
    return doctorDao.save(doctor);
  }

  @CacheEvict( value = "doctor" )
  public boolean delete(Integer id) {
    Doctor doctor = doctorDao.getOne(id);
    doctor.setLiveDead(LiveDead.STOP);
    doctorDao.save(doctor);
    return false;
  }

  @CachePut( value = "doctor" )
  public List< Doctor > search(Doctor doctor) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< Doctor > doctorExample = Example.of(doctor, matcher);
    return doctorDao.findAll(doctorExample);
  }

  public Doctor lastDoctor() {
    return doctorDao.findFirstByOrderByIdDesc();
  }
}
