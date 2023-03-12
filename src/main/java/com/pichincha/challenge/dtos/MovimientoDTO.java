package com.pichincha.challenge.dtos;
import com.pichincha.challenge.entities.enums.TipoMovimiento;
import lombok.Data;

import java.util.Date;

@Data
public class MovimientoDTO {

  private Long id;

  private Date fecha;

  private TipoMovimiento tipoMovimiento;

  private Double valor;

  private Double saldo;

  private String cuentaId;
}