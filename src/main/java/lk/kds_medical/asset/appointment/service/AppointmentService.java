package lk.kds_medical.asset.appointment.service;


import lk.kds_medical.asset.appointment.dao.AppointmentDao;
import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
import lk.kds_medical.asset.patient.entity.Patient;
import lk.kds_medical.asset.payment.entity.Payment;
import lk.kds_medical.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public List<Appointment> byDateAndDoctorSchedule(LocalDate date, DoctorSchedule doctorSchedule) {
        return appointmentDao.findByDateAndDoctorSchedule(date, doctorSchedule);

    }

  public Appointment lastAppointment() {
        return appointmentDao.findFirstByOrderByIdDesc();
  }

    public Appointment findByDoctorScheduleAndPatientAndDate(DoctorSchedule doctorSchedule, Patient patient, LocalDate date) {
    return appointmentDao.findByDoctorScheduleAndPatientAndDate(doctorSchedule,patient,date);
    }

  public List< Appointment> findByCreatedBy(String username) {
  return appointmentDao.findByCreatedBy(username);
    }

  public List< Appointment> findByCreatedAtIsBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
  return  appointmentDao.findByCreatedAtIsBetween(startDateTime, endDateTime);
    }
}
