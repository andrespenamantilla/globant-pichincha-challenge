package com.pichincha.challenge.dtos;

import com.pichincha.challenge.entities.enums.Genero;
import lombok.Data;

@Data
public class PersonaDTO {
  private String nombre;

  private Genero genero;

  private Integer edad;

  private String identificacion;

  private String direccion;

  private String telefono;
}