package com.pichincha.challenge.exception;

public class ErrorResponse {
  private String codigoError;
  private String mensajeError;

  public ErrorResponse(String codigoError, String mensajeError) {
    this.codigoError = codigoError;
    this.mensajeError = mensajeError;
  }

  public String getCodigoError() {
    return codigoError;
  }

  public void setCodigoError(String codigoError) {
    this.codigoError = codigoError;
  }

  public String getMensajeError() {
    return mensajeError;
  }

  public void setMensajeError(String mensajeError) {
    this.mensajeError = mensajeError;
  }
}