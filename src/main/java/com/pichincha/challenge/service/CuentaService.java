package com.pichincha.challenge.service;

import com.pichincha.challenge.dtos.CuentaDTO;
import com.pichincha.challenge.entities.Cuenta;
import com.pichincha.challenge.repository.CuentaRepository;
import com.pichincha.challenge.utils.converter.EntityConverter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CuentaService {

  private final static EntityConverter<CuentaDTO, Cuenta> CUENTA_CONVERTER = new EntityConverter<>(
      CuentaDTO.class, Cuenta.class);

  @Autowired
  private CuentaRepository cuentaRepository;

  public List<CuentaDTO> getAllCuentas() {
    log.debug("CuentaService.getAllCuentas ");
    return cuentaRepository.findAll().stream().map(CUENTA_CONVERTER::toT1).collect(Collectors.toList());
  }

  public CuentaDTO getCuentaById(Long id) {
    log.debug("CuentaService.getCuentaById "+ id);
    Optional<Cuenta> cuenta = cuentaRepository.findById(id);
     if(cuenta.isPresent()){
      return CUENTA_CONVERTER.toT1(cuenta.get());
     }
     else{
       return null;
     }
  }

  public CuentaDTO saveCuenta(CuentaDTO cuentaDTO) {
    log.debug("CuentaService.saveCuenta "+ cuentaDTO);

    Cuenta cuenta = CUENTA_CONVERTER.toT2(cuentaDTO);
    if (cuenta.getId() == null) {
      Long index =  getIndex();
      cuenta.setId(index);
    }
    cuentaRepository.save(cuenta);
    return CUENTA_CONVERTER.toT1(cuenta);
  }

  public CuentaDTO updateCuenta(CuentaDTO cuentaDTO){
    log.debug("CuentaService.updateCuenta "+ cuentaDTO);

    Cuenta cuenta = CUENTA_CONVERTER.toT2(getCuentaById(cuentaDTO.getId()));
    //Sólo deberia actualizar el tipo de cuenta  y el estado
    cuenta.setTipoCuenta(cuentaDTO.getTipoCuenta());
    cuenta.setEstado(cuentaDTO.getEstado());
    CuentaDTO cuentaModificadaDTO =  CUENTA_CONVERTER.toT1(cuenta);
    saveCuenta(cuentaModificadaDTO);
    return cuentaModificadaDTO;
  }

  public void deleteCuenta(Long id) {
    log.debug("CuentaService.deleteCuenta "+ id);
    cuentaRepository.deleteById(id);
  }

  public CuentaDTO findCuentaPorNumeroCuenta(String numeroCuenta) {
    log.debug("CuentaService.findCuentaPorNumeroCuenta "+ numeroCuenta);
    Optional<Cuenta> cuentaEncontrada = cuentaRepository.findCuentaByNumeroCuenta(numeroCuenta);
    if (cuentaEncontrada.isPresent()) {
      return CUENTA_CONVERTER.toT1(cuentaEncontrada.get());
    } else {
      return null;
    }
  }

  public Long getIndex() {
    log.debug("CuentaService.getIndex ");
    Long i = (long) (cuentaRepository.findAll().size() + 2);
    return i;
  }
}
