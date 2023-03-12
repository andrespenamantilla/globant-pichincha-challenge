package com.pichincha.challenge.repository;

import com.pichincha.challenge.entities.Cuenta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
  Optional<Cuenta> findCuentaByNumeroCuenta(String numeroCuenta);

 List<Cuenta> findAllByCliente_Id(String clienteId);
}