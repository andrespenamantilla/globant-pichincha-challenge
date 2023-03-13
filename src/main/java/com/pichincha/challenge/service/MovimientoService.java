package com.pichincha.challenge.service;

import com.pichincha.challenge.dtos.CuentaDTO;
import com.pichincha.challenge.dtos.MovimientoDTO;
import com.pichincha.challenge.entities.Movimiento;
import com.pichincha.challenge.entities.enums.TipoMovimiento;
import com.pichincha.challenge.exception.CuentaNoExisteException;
import com.pichincha.challenge.exception.SaldoInsuficienteException;
import com.pichincha.challenge.repository.MovimientoRepository;
import com.pichincha.challenge.utils.converter.EntityConverter;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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
    log.debug("MovimientoService.getMovimientoById "+id);
    return MOVIMIENTO_CONVERTER.toT1(movimientoRepository.findById(id).get());
  }

  public MovimientoDTO saveMovimiento(MovimientoDTO movimientoDto) {
    log.debug("MovimientoService.saveMovimiento "+ movimientoDto);
    Movimiento movimiento = MOVIMIENTO_CONVERTER.toT2(movimientoDto);
    if (movimientoDto.getId() == null) {
      movimiento.setId(getIndex());
    }
     movimientoRepository.save(movimiento);
    return MOVIMIENTO_CONVERTER.toT1(movimiento);
  }

  public MovimientoDTO registrarMovimientoEnCuenta(MovimientoDTO nuevoMovimiento, String numeroCuenta) {
    log.debug("MovimientoService.registrarMovimientoEnCuenta "+ nuevoMovimiento + " "+numeroCuenta);
    CuentaDTO cuentaDTO = cuentaService.findCuentaPorNumeroCuenta(numeroCuenta);

    if (cuentaDTO == null) {
      throw new CuentaNoExisteException("No se encontró la cuenta");
    }

    if (nuevoMovimiento.getTipoMovimiento().equals(TipoMovimiento.DEPOSITO)
        && nuevoMovimiento.getValor() > 0) {
      return registrarDeposito(nuevoMovimiento, cuentaDTO);
    }

    if (nuevoMovimiento.getTipoMovimiento().equals(TipoMovimiento.RETIRO)
        && cuentaDTO.getSaldoInicial() > 0) {
      return registrarRetiro(nuevoMovimiento, cuentaDTO);
    }

    throw new RuntimeException("Tipo de movimiento no válido");
  }


  private MovimientoDTO registrarDeposito(MovimientoDTO nuevoMovimiento, CuentaDTO cuentaDTO) {
    log.debug("MovimientoService.registrarDeposito "+ nuevoMovimiento + "/ "+cuentaDTO);

    Double saldo = cuentaDTO.getSaldoInicial() + nuevoMovimiento.getValor();
    cuentaDTO.setSaldoInicial(saldo);
    cuentaService.saveCuenta(cuentaDTO);

    nuevoMovimiento.setSaldo(saldo);
    nuevoMovimiento.setFecha(new Date());
    nuevoMovimiento.setCuentaId(cuentaDTO.getNumeroCuenta());
    saveMovimiento(nuevoMovimiento);

    return nuevoMovimiento;
  }

  private MovimientoDTO registrarRetiro(MovimientoDTO nuevoMovimiento, CuentaDTO cuentaDTO) {

    log.debug("MovimientoService.registrarRetiro "+ nuevoMovimiento + "/ "+cuentaDTO);

    if (nuevoMovimiento.getValor() <= cuentaDTO.getSaldoInicial()) {
      Double saldo = cuentaDTO.getSaldoInicial() - nuevoMovimiento.getValor();
      cuentaDTO.setSaldoInicial(saldo);
      cuentaService.saveCuenta(cuentaDTO);

      nuevoMovimiento.setSaldo(saldo);
      nuevoMovimiento.setFecha(new Date());
      nuevoMovimiento.setCuentaId(cuentaDTO.getNumeroCuenta());
      saveMovimiento(nuevoMovimiento);

      return nuevoMovimiento;
    } else {
      nuevoMovimiento.setCuentaId(cuentaDTO.getNumeroCuenta());
      nuevoMovimiento.setSaldo(cuentaDTO.getSaldoInicial());
      saveMovimiento(nuevoMovimiento);
      throw new SaldoInsuficienteException("Saldo insuficiente para hacer el retiro");
    }
  }

  public void deleteMovimiento(Long id) {
    log.debug("MovimientoService.deleteMovimiento "+ id);
    movimientoRepository.deleteById(id);
  }

  public Long getIndex(){
    Long i = (long) (movimientoRepository.findAll().size() + 2);
    return i;
  }

}
