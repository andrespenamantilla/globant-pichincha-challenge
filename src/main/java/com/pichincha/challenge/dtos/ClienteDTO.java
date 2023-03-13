package com.pichincha.challenge.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ClienteDTO {

  @Schema(hidden = true)
  private String clienteId;

  private String contrasenia;

  private Boolean estado;

  private PersonaDTO personaDTO;

}