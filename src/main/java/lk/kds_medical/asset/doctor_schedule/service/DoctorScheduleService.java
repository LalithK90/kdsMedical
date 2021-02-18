package lk.kds_medical.asset.doctor_schedule.service;


import lk.kds_medical.asset.doctor_schedule.dao.DoctorScheduleDao;
import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
import lk.kds_medical.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DoctorScheduleService implements AbstractService< DoctorSchedule, Integer> {

    private DoctorScheduleDao doctorScheduleDao;
    @Autowired
    public DoctorScheduleService(DoctorScheduleDao doctorScheduleDao){
        this.doctorScheduleDao = doctorScheduleDao;
    }


    @Cacheable(value = "DoctorSchedule")
    public List<DoctorSchedule> findAll() {
        return doctorScheduleDao.findAll();
    }


    public DoctorSchedule findById(Integer id) {
        return doctorScheduleDao.getOne(id);
    }

    @Transactional
    public DoctorSchedule persist(DoctorSchedule doctorSchedule) {
        return doctorScheduleDao.save(doctorSchedule);
    }

    @Transactional
    public boolean delete(Integer id) {
        doctorScheduleDao.deleteById(id);
        return false;
    }


    public List<DoctorSchedule> search(DoctorSchedule doctorSchedule) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<DoctorSchedule> consultationExample = Example.of(doctorSchedule, matcher);
        return doctorScheduleDao.findAll(consultationExample);
    }
}
