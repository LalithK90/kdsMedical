package lk.kds_medical.asset.consultation.entity;


import lk.kds_medical.asset.common_asset.model.Enum.LiveDead;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45, unique = true)
    private String name;

    @Enumerated( EnumType.STRING )
    private LiveDead liveDead;

}
