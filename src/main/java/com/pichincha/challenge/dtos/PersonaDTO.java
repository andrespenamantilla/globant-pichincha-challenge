package com.pichincha.challenge.dtos;

import com.pichincha.challenge.entities.enums.Genero;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PersonaDTO {

  @Schema(hidden = true)
  private Long Id;
  private String nombre;

  private Genero genero;

  private Integer edad;

  private String identificacion;

  private String direccion;

  private String telefono;
}