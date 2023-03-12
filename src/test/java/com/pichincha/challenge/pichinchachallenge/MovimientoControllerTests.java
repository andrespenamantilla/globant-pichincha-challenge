package com.pichincha.challenge.pichinchachallenge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


import com.pichincha.challenge.controller.MovimientoController;
import com.pichincha.challenge.dtos.MovimientoDTO;
import com.pichincha.challenge.entities.Cuenta;
import com.pichincha.challenge.entities.Movimiento;
import com.pichincha.challenge.entities.enums.TipoMovimiento;
import com.pichincha.challenge.exception.CuentaNoExisteException;
import com.pichincha.challenge.exception.ErrorResponse;
import com.pichincha.challenge.exception.SaldoInsuficienteException;
import com.pichincha.challenge.service.MovimientoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
 public class MovimientoControllerTests {

  @Mock
   MovimientoService movimientoService;

  @InjectMocks
   MovimientoController movimientoController;

  @Test
  public void testHacerMovimientoConDepositoExitoso() {
    String numeroCuenta = "1234567890";
    MovimientoDTO movimiento = new MovimientoDTO();
    movimiento.setTipoMovimiento(TipoMovimiento.DEPOSITO);
    movimiento.setSaldo(100.0);

    Cuenta cuenta = new Cuenta();
    cuenta.setNumeroCuenta(numeroCuenta);
    cuenta.setSaldoInicial(500.0);

    when(movimientoService.registrarMovimientoEnCuenta(movimiento, numeroCuenta)).thenReturn(movimiento);

    ResponseEntity<Object> responseEntity = movimientoController.hacerMovimiento(numeroCuenta, movimiento);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertTrue(responseEntity.getBody() instanceof Movimiento);
    Movimiento resultado = (Movimiento) responseEntity.getBody();
    assertEquals(movimiento, resultado);
    assertTrue(responseEntity.getBody() instanceof MovimientoDTO);
  }

  @Test
  public void testHacerMovimientoConRetiroExitoso() {
    String numeroCuenta = "1234567890";
    MovimientoDTO movimiento = new MovimientoDTO();
    movimiento.setTipoMovimiento(TipoMovimiento.RETIRO);
    movimiento.setSaldo(100.0);

    Cuenta cuenta = new Cuenta();
    cuenta.setNumeroCuenta(numeroCuenta);
    cuenta.setSaldoInicial(500.0);

    when(movimientoService.registrarMovimientoEnCuenta(movimiento, numeroCuenta)).thenReturn(movimiento);

    ResponseEntity<Object> responseEntity = movimientoController.hacerMovimiento(numeroCuenta, movimiento);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertTrue(responseEntity.getBody() instanceof MovimientoDTO);
  }

  @Test
  public void testHacerMovimientoConSaldoInsuficiente() {
    String numeroCuenta = "1234567890";

    MovimientoDTO movimiento = new MovimientoDTO();

    movimiento.setTipoMovimiento(TipoMovimiento.RETIRO);
    movimiento.setSaldo(1000.0);
    when(movimientoService.registrarMovimientoEnCuenta(movimiento, numeroCuenta)).thenThrow(new SaldoInsuficienteException("Saldo insuficiente para hacer el retiro"));

    ResponseEntity<Object> responseEntity = movimientoController.hacerMovimiento(numeroCuenta, movimiento);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    assertEquals("SALDO_INSUFICIENTE", ((ErrorResponse) responseEntity.getBody()).getCodigoError());
    assertTrue(responseEntity.getBody() instanceof ErrorResponse);

  }

  @Test
  public void testHacerMovimientoConCuentaNoExiste() {
    String numeroCuenta = "1234567890";
    MovimientoDTO movimiento = new MovimientoDTO();
    movimiento.setTipoMovimiento(TipoMovimiento.DEPOSITO);
    movimiento.setSaldo(100.0);
    when(movimientoService.registrarMovimientoEnCuenta(movimiento, numeroCuenta)).thenThrow(new CuentaNoExisteException("No se encontró la cuenta"));

    ResponseEntity<?> responseEntity = movimientoController.hacerMovimiento(numeroCuenta, movimiento);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    assertEquals("CUENTA_NO_EXISTE", ((ErrorResponse) responseEntity.getBody()).getCodigoError());
    assertTrue(responseEntity.getBody() instanceof ErrorResponse);

  }

}