package lk.kds_medical.asset.patient.service;


import lk.kds_medical.asset.patient.dao.PatientDao;
import lk.kds_medical.asset.patient.entity.Patient;
import lk.kds_medical.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@CacheConfig(cacheNames = "patient")

public class PatientService implements AbstractService<Patient,Integer> {

    private final PatientDao patientDao;

    @Autowired
    public PatientService(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    @Cacheable
    public List<Patient> findAll() {
        return patientDao.findAll();
    }

    @Cacheable
    public Patient findById(Integer id) {
        return patientDao.getOne(id);
    }

    @Caching( evict = {@CacheEvict( value = "patient", allEntries = true )},
            put = {@CachePut( value = "patient", key = "#patient.id" )} )
    @Transactional
    public Patient persist(Patient patient) {
        return patientDao.save(patient);
    }

    @CacheEvict( allEntries = true )
    public boolean delete(Integer id) {
        patientDao.deleteById(id);
        return false;
    }

    @Cacheable
    public List< Patient > search(Patient patient) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< Patient > PatientExample = Example.of(patient, matcher);
        return patientDao.findAll(PatientExample);
    }

    public boolean isPatientPresent(Patient patient) {
        return patientDao.equals(patient);
    }


  /*  public Patient lastPatient() {
        return patientDao.findFirstByOderByIdDesc();
    }*/

    @Cacheable
    public Patient findByNic(String nic) {
        return patientDao.findByNic(nic);
    }
}
