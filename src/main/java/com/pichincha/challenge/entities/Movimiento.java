package com.pichincha.challenge.entities;

import com.pichincha.challenge.entities.enums.TipoMovimiento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.Date;
import lombok.Data;


@Data
@Entity
@Table(name = "movimiento")

public class Movimiento {

  @Id
  @Column(name = "id")
  @Schema(accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Column(name = "fecha")
  @Schema(description = "Fecha resultante del movimiento", required = false)
  private Date fecha;

  @Column(name = "tipo_movimiento")
  @Enumerated(EnumType.STRING)
  private TipoMovimiento tipoMovimiento;

  @Column(name = "valor")
  @PositiveOrZero(message = "El valor debe ser mayor o igual a cero.")
  private Double valor;

  @Column(name = "saldo")
  @Schema(description = "Saldo resultante del movimiento", required = false)
  private Double saldo;

  @Column(name ="numero_cuenta")
  private String cuentaId;

}