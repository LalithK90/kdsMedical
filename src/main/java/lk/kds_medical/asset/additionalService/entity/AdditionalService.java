package lk.kds_medical.asset.additionalService.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.kds_medical.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("AdditionalService")
public class AdditionalService extends AuditEntity {

    @NotEmpty
    private String name;

    @NotEmpty
    private BigDecimal price;
}
