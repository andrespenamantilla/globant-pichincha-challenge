package com.pichincha.challenge.dtos;


import com.pichincha.challenge.entities.enums.TipoCuenta;
import lombok.Data;
@Data
public class CuentaDTO {

  private Long id;

  private String numeroCuenta;

  private TipoCuenta tipoCuenta;

  private Double saldoInicial;

  private Boolean estado;

  private Long clienteId;
}