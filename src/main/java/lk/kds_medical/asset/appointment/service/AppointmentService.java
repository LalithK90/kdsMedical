package lk.kds_medical.asset.appointment.service;


import lk.kds_medical.asset.appointment.dao.AppointmentDao;
import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AppointmentService implements AbstractService< Appointment, Integer> {

    private AppointmentDao appointmentDao;
    @Autowired
    public AppointmentService(AppointmentDao appointmentDao){
        this.appointmentDao = appointmentDao;
    }


    @Cacheable(value = "Appointment")
    public List<Appointment> findAll() {
        return appointmentDao.findAll();
    }


    public Appointment findById(Integer id) {
        return appointmentDao.getOne(id);
    }

    @Transactional
    public Appointment persist(Appointment appointment) {
        return appointmentDao.save(appointment);
    }

    @Transactional
    public boolean delete(Integer id) {
        appointmentDao.deleteById(id);
        return false;
    }


    public List<Appointment> search(Appointment appointment) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Appointment> consultationExample = Example.of(appointment, matcher);
        return appointmentDao.findAll(consultationExample);
    }
}
