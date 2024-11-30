package com.project.healthcomplex.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignDto {
    @NotNull
    private Long user_id;

    @NotNull
    private Long service_id;

    @NotNull
    private Long timeId;
}
