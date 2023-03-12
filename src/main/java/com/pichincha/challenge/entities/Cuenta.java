package com.pichincha.challenge.entities;

import com.pichincha.challenge.entities.enums.TipoCuenta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@Entity
@Table(name = "cuenta")

public class Cuenta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "numero_cuenta",unique = true)
  private String numeroCuenta;

  @Column(name = "tipo_cuenta")
  @Enumerated(EnumType.STRING)
  private TipoCuenta tipoCuenta;

  @Column(name = "saldo_inicial")
  @PositiveOrZero(message = "El valor debe ser mayor o igual a cero.")
  private Double saldoInicial;

  @Column(name = "estado")
  private Boolean estado;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cliente_id", referencedColumnName = "id")
  private Cliente cliente;

}