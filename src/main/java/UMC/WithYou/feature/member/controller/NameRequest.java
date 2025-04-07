package UMC.WithYou.feature.member.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NameRequest {
    @NotBlank(message = "name is mandatory")
    @Schema(description = "name", example = "장호진", required = true)
    private String name;
}
