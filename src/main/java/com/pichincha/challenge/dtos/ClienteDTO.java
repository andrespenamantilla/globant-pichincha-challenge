package com.pichincha.challenge.dtos;

import lombok.Data;

@Data
public class ClienteDTO {

  private String nombre;

  private String identificacion;

  private String direccion;

  private String telefono;

  private String clienteId;

  private String contrasenia;

  private Boolean estado;

  private PersonaDTO personaDTO;

}