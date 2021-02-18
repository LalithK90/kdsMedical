package lk.kds_medical.asset.appointment.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kds_medical.util.audit.AuditEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode( callSuper = true )
@JsonFilter( "Appointment" )
public class Appointment extends AuditEntity {
  @DateTimeFormat( pattern = "yyyy-MM-dd HH:MM" )
  private LocalDate startAt;
}
