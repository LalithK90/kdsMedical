package lk.kds_medical.asset.doctor.entity;


import lk.kds_medical.asset.common_asset.model.Enum.Gender;
import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.common_asset.model.Enum.Title;
import lk.kds_medical.asset.consultation.entity.Consultation;
import lk.kds_medical.asset.doctor_schedule.entity.DoctorSchedule;
import lk.kds_medical.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Doctor extends AuditEntity {

  @Column( length = 45 )
  private String name;

  private String code;

  @Column( unique = true )
  private Integer slmcNumber;

  private String mobile;

  private String mobileTwo;

  private String land;

  @Email( message = "Please provide valid email" )
  @Column( length = 45 )
  private String email;

  @Column( length = 10 )
  private String description;

  @Enumerated( EnumType.STRING )
  private Title title;

  @Enumerated( EnumType.STRING )
  private Gender gender;

  @Enumerated(EnumType.STRING)
  private LiveDead liveDead;

  @NotNull(message = "consultation fee is required")
  private BigDecimal consultationFee;

  @ManyToOne
  private Consultation consultation;

  @OneToMany( cascade = {CascadeType.MERGE, CascadeType.PERSIST} )
  private List< DoctorSchedule > doctorSchedules;

}
