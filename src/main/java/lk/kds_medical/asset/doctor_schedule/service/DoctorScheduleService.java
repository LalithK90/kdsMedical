package lk.kds_medical.asset.doctor_schedule.service;


import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.doctor.entity.Doctor;
import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
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
import java.util.stream.Collectors;

@Service
public class DoctorScheduleService implements AbstractService< DoctorSchedule, Integer> {

    private DoctorScheduleDao doctorScheduleDao;
    @Autowired
    public DoctorScheduleService(DoctorScheduleDao doctorScheduleDao){
        this.doctorScheduleDao = doctorScheduleDao;
    }


    @Cacheable(value = "DoctorSchedule")
    public List<DoctorSchedule> findAll() {
        return doctorScheduleDao.findAll().stream()
            .filter(x->x.getLiveDead().equals(LiveDead.ACTIVE))
            .collect(Collectors.toList());
    }


    public DoctorSchedule findById(Integer id) {
        return doctorScheduleDao.getOne(id);
    }

    public DoctorSchedule persist(DoctorSchedule doctorSchedule) {
        if ( doctorSchedule.getId()==null ){
            doctorSchedule.setLiveDead(LiveDead.ACTIVE);
        }
        return doctorScheduleDao.save(doctorSchedule);
    }

    public boolean delete(Integer id) {
        DoctorSchedule doctorSchedule =  doctorScheduleDao.getOne(id);
        doctorSchedule.setLiveDead(LiveDead.STOP);
        doctorScheduleDao.save(doctorSchedule);
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

  public List<DoctorSchedule> findByDoctor(Doctor doctor) {
      List<DoctorSchedule> abc = doctorScheduleDao.findByDoctor(doctor);
      System.out.println(abc.size());
  return abc;
    }
}
