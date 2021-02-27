package lk.kds_medical.asset.appointment.service;


import lk.kds_medical.asset.appointment.dao.AppointmentDao;
import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.payment.entity.Payment;
import lk.kds_medical.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService implements AbstractService< Appointment, Integer> {

    private AppointmentDao appointmentDao;
    @Autowired
    public AppointmentService(AppointmentDao appointmentDao){
        this.appointmentDao = appointmentDao;
    }


    @Cacheable(value = "Appointment")
    public List<Appointment> findAll() {
        return appointmentDao.findAll()
            .stream()
            .filter(x->x.getLiveDead().equals(LiveDead.ACTIVE))
            .collect(Collectors.toList());
    }


    public Appointment findById(Integer id) {
        return appointmentDao.getOne(id);
    }


    public Appointment persist(Appointment appointment) {
        if ( appointment.getId()==null ){
            appointment.setLiveDead(LiveDead.ACTIVE);
        }
        return appointmentDao.save(appointment);
    }

    public boolean delete(Integer id) {
        Appointment appointment =  appointmentDao.getOne(id);
        appointment.setLiveDead(LiveDead.STOP);
        appointmentDao.save(appointment);
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
    public List<Appointment> byDate(LocalDate localDate) {
        return appointmentDao.findByDateAfter(localDate);
    }

  public Appointment lastAppointment() {
        return appointmentDao.findFirstByOrderByIdDesc();
  }
}
