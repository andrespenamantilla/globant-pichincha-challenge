package com.pichincha.challenge.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "cliente")
public class Cliente extends Persona {

  @Column(name = "cliente_id", unique = true)
  private String clienteId;

  @Column(name = "contrasenia")
  private String contrasenia;

  @Column(name = "estado")
  private Boolean estado;
}