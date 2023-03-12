package com.pichincha.challenge.controller;

import com.pichincha.challenge.dtos.MovimientoDTO;
import com.pichincha.challenge.entities.Movimiento;
import com.pichincha.challenge.exception.CuentaNoExisteException;
import com.pichincha.challenge.exception.SaldoInsuficienteException;
import com.pichincha.challenge.exception.ErrorResponse;
import com.pichincha.challenge.service.CuentaService;
import com.pichincha.challenge.service.MovimientoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

  @Autowired
  private MovimientoService movimientoService;
  private CuentaService cuentaService;


  @GetMapping
  public List<Movimiento> getAllMovimientos() {
    return movimientoService.getAllMovimientos();
  }

  @GetMapping("/{id}")
  public MovimientoDTO getMovimientoById(@PathVariable Long id) {
    return movimientoService.getMovimientoById(id);
  }

  @PostMapping
  public MovimientoDTO saveMovimiento(@RequestBody MovimientoDTO movimiento) {
    return movimientoService.saveMovimiento(movimiento);
  }


  @PostMapping("/cuentas/{numeroCuenta}/movimientos")
  public ResponseEntity<Object> hacerMovimiento(@PathVariable String numeroCuenta, @RequestBody MovimientoDTO movimiento) {

    try {
      MovimientoDTO resultado = movimientoService.registrarMovimientoEnCuenta(movimiento,numeroCuenta);
      return ResponseEntity.ok(resultado);
    } catch (SaldoInsuficienteException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("SALDO_INSUFICIENTE", e.getMessage()));
    }
    catch (CuentaNoExisteException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("CUENTA_NO_EXISTE", e.getMessage()));
    }
    catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("ERROR_GENERICO", e.getMessage()));
    }
  }


  @DeleteMapping("/{id}")
  public void deleteMovimiento(@PathVariable Long id) {
    movimientoService.deleteMovimiento(id);
  }
}
