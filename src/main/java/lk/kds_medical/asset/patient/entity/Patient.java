package lk.kds_medical.asset.patient.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kds_medical.asset.additional_service.entity.AdditionalService;
import lk.kds_medical.asset.appointment.entity.Appointment;
import lk.kds_medical.asset.common_asset.model.Enum.Gender;
import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lk.kds_medical.asset.common_asset.model.Enum.Title;
import lk.kds_medical.asset.payment_additional_service.entity.PaymentAdditionalService;
import lk.kds_medical.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "Patient" )
public class Patient extends AuditEntity {

  @Size( min = 3, message = "Your name cannot be accepted" )
  private String name;

  @Column( unique = true )
  private String code;

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate dateOfBirth;

  @Size( max = 12, min = 10, message = "NIC number is contained numbers between 9 and X/V or 12 " )
  @Column( unique = true )
  private String nic;

  @Size( max = 10, message = "Mobile number length should be contained 10 and 9" )
  private String mobileOne;


  private String mobileTwo;

  @Column( columnDefinition = "VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NULL", length = 255 )
  private String address;

  @Column( unique = true )
  private String email;

  @Enumerated( EnumType.STRING )
  private Title title;

  @Enumerated( EnumType.STRING )
  private Gender gender;

  @Enumerated( EnumType.STRING )
  private LiveDead liveDead;

  @OneToMany( mappedBy = "patient" )
  private List< PaymentAdditionalService > paymentAdditionalServices;

  @OneToMany( mappedBy = "patient" )
  private List< Appointment > appointments;
}
