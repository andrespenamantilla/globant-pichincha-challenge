package com.pichincha.challenge.service;

import com.pichincha.challenge.dtos.MovimientoDTO;
import com.pichincha.challenge.entities.Cuenta;
import com.pichincha.challenge.entities.Movimiento;
import com.pichincha.challenge.entities.enums.TipoMovimiento;
import com.pichincha.challenge.exception.CuentaNoExisteException;
import com.pichincha.challenge.exception.SaldoInsuficienteException;
import com.pichincha.challenge.repository.MovimientoRepository;
import com.pichincha.challenge.utils.converter.EntityConverter;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovimientoService {

  private final static EntityConverter<MovimientoDTO, Movimiento> MOVIMIENTO_CONVERTER = new EntityConverter<>(MovimientoDTO.class, Movimiento.class);


  @Autowired
  private MovimientoRepository movimientoRepository;

  @Autowired
  private CuentaService cuentaService;

  public List<Movimiento> getAllMovimientos() {
    return movimientoRepository.findAll();
  }

  public MovimientoDTO getMovimientoById(Long id) {
    return MOVIMIENTO_CONVERTER.toT1(movimientoRepository.findById(id).get());
  }

  public MovimientoDTO saveMovimiento(MovimientoDTO movimientoDto) {

    Movimiento movimiento = MOVIMIENTO_CONVERTER.toT2(movimientoDto);
    if (movimientoDto.getId() == null) {
      movimiento.setId(getIndex());
    }
     movimientoRepository.save(movimiento);
    return MOVIMIENTO_CONVERTER.toT1(movimiento);
  }

  public MovimientoDTO registrarMovimientoEnCuenta(MovimientoDTO nuevoMovimiento, String numeroCuenta) {
    Cuenta cuenta = cuentaService.findCuentaPorNumeroCuenta(numeroCuenta);

    if (cuenta == null) {
      throw new CuentaNoExisteException("No se encontró la cuenta");
    }

    if (nuevoMovimiento.getTipoMovimiento().equals(TipoMovimiento.DEPOSITO)
        && nuevoMovimiento.getValor() > 0) {
      return registrarDeposito(nuevoMovimiento, cuenta);
    }

    if (nuevoMovimiento.getTipoMovimiento().equals(TipoMovimiento.RETIRO)
        && cuenta.getSaldoInicial() > 0) {
      return registrarRetiro(nuevoMovimiento, cuenta);
    }

    throw new RuntimeException("Tipo de movimiento no válido");
  }


  private MovimientoDTO registrarDeposito(MovimientoDTO nuevoMovimiento, Cuenta cuenta) {
    Double saldo = cuenta.getSaldoInicial() + nuevoMovimiento.getValor();
    cuenta.setSaldoInicial(saldo);
    cuentaService.saveCuenta(cuenta);

    nuevoMovimiento.setSaldo(saldo);
    nuevoMovimiento.setFecha(new Date());
    nuevoMovimiento.setCuentaId(cuenta.getNumeroCuenta());
    saveMovimiento(nuevoMovimiento);

    return nuevoMovimiento;
  }

  private MovimientoDTO registrarRetiro(MovimientoDTO nuevoMovimiento, Cuenta cuenta) {
    if (nuevoMovimiento.getValor() <= cuenta.getSaldoInicial()) {
      Double saldo = cuenta.getSaldoInicial() - nuevoMovimiento.getValor();
      cuenta.setSaldoInicial(saldo);
      cuentaService.saveCuenta(cuenta);

      nuevoMovimiento.setSaldo(saldo);
      nuevoMovimiento.setFecha(new Date());
      nuevoMovimiento.setCuentaId(cuenta.getNumeroCuenta());
      saveMovimiento(nuevoMovimiento);

      return nuevoMovimiento;
    } else {
      nuevoMovimiento.setCuentaId(cuenta.getNumeroCuenta());
      nuevoMovimiento.setSaldo(cuenta.getSaldoInicial());
      saveMovimiento(nuevoMovimiento);
      throw new SaldoInsuficienteException("Saldo insuficiente para hacer el retiro");
    }
  }

  public void deleteMovimiento(Long id) {
    movimientoRepository.deleteById(id);
  }

  public Long getIndex(){
    Long i = (long) (movimientoRepository.findAll().size() + 2);
    return i;
  }

}
