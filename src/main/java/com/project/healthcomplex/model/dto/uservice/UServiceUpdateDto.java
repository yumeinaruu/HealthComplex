package com.project.healthcomplex.model.dto.uservice;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UServiceUpdateDto {
    @NotNull
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    private String name;

    @NotNull
    private BigDecimal price;

    @NotNull
    @Size(min = 1, max = 10)
    private String duration;

    @NotNull
    @Size(min = 1, max = 256)
    private String url;
}
