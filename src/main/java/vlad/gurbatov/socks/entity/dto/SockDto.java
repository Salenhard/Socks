package vlad.gurbatov.socks.entity.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

/**
 * DTO for {@link vlad.gurbatov.socks.entity.Sock}
 */
@Getter
@Setter
public class SockDto {
    private Long id;
    @NotBlank
    private String color;
    @Min(0)
    @Max(100)
    private Integer cotton;
    @Min(0)
    private Integer amount;
}