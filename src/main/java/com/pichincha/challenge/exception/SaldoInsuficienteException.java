package com.pichincha.challenge.exception;

public class SaldoInsuficienteException extends RuntimeException {
  public SaldoInsuficienteException(String mensaje) {
    super(mensaje);
  }
}