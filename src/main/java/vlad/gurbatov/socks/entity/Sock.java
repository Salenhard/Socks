package vlad.gurbatov.socks.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Sock {
    @Id
    @GeneratedValue
    private Long id;
    private String color;
    private Integer cottonPercentage;
    private Integer amount;
}
