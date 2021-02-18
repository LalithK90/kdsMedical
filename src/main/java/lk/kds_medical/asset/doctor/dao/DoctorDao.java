package lk.kds_medical.asset.doctor.dao;

import lk.kds_medical.asset.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorDao extends JpaRepository<Doctor, Integer> {
  Doctor findFirstByOrderByIdDesc();
}

