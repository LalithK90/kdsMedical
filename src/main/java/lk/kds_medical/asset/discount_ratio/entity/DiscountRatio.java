package lk.kds_medical.asset.discount_ratio.entity;

import lk.kds_medical.asset.discount_ratio.entity.Enum.DiscountRatioStatus;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DiscountRatio {
  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  private Integer id;


  private String name;


  @Column( nullable = false, precision = 10, scale = 2 )
  private BigDecimal amount;

  @Enumerated( EnumType.STRING )
  private DiscountRatioStatus discountRatioStatus;

/*    @OneToMany
    @JoinColumn(name = "discount_ratio_id")
    private List<Invoice> invoices = new ArrayList<>();*/


}
