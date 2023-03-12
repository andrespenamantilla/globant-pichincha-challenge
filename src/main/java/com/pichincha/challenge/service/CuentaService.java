package com.pichincha.challenge.service;

import com.pichincha.challenge.dtos.CuentaDTO;
import com.pichincha.challenge.entities.Cuenta;
import com.pichincha.challenge.repository.CuentaRepository;
import com.pichincha.challenge.utils.converter.EntityConverter;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuentaService {

  private final static EntityConverter<CuentaDTO, Cuenta> CUENTA_CONVERTER = new EntityConverter<>(CuentaDTO.class, Cuenta.class);


  @Autowired
  private CuentaRepository cuentaRepository;

  @Autowired
  private EntityManager entityManager;

  public List<Cuenta> getAllCuentas() {
    return cuentaRepository.findAll();
  }

  public Cuenta getCuentaById(Long id) {
    return cuentaRepository.findById(id).orElse(null);
  }

  public Cuenta saveCuenta(Cuenta cuenta) {
   if(cuenta.getId() == null){
     //Esto No se debe hacer pero  la secuencia no se porque no funciona en la entidad
     Long i = (long) (cuentaRepository.findAll().size() + 2);
     cuenta.setId(i);
   }
    return cuentaRepository.save(cuenta);
  }

  public void deleteCuenta(Long id) {
    cuentaRepository.deleteById(id);
  }

  public Cuenta findCuentaPorNumeroCuenta(String numeroCuenta) {
    Optional<Cuenta> cuentaEncontrada = cuentaRepository.findCuentaByNumeroCuenta(numeroCuenta);
    if (cuentaEncontrada.isPresent()) {
      return cuentaEncontrada.get();
    } else {
      return null;
    }

  }
}
