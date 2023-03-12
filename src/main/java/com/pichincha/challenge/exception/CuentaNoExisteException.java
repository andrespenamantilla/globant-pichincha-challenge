package com.pichincha.challenge.exception;

public class CuentaNoExisteException  extends RuntimeException{
  public CuentaNoExisteException(String mensaje) {
    super(mensaje);
  }
}
