package com.pichincha.challenge.entities;

import com.pichincha.challenge.entities.enums.Genero;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "persona")
@Inheritance(strategy = InheritanceType.JOINED)

public class Persona {

  @Id
  @Column(name = "id")
  @Schema(accessMode = Schema.AccessMode.READ_ONLY)

  private Long id;

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "genero")
  @Enumerated(EnumType.STRING)
  private Genero genero;

  @Column(name = "edad")
  private Integer edad;

  @Column(name = "identificacion", unique = true)
  private String identificacion;

  @Column(name = "direccion")
  private String direccion;

  @Column(name = "telefono")
  private String telefono;
}