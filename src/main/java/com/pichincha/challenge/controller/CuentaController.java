package com.pichincha.challenge.controller;
import com.pichincha.challenge.entities.Cuenta;
import com.pichincha.challenge.service.CuentaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

  @Autowired
  private CuentaService cuentaService;

  @GetMapping
  public List<Cuenta> getAllCuentas() {
    return cuentaService.getAllCuentas();
  }

  @GetMapping("/{id}")
  public Cuenta getCuentaById(@PathVariable Long id) {
    return cuentaService.getCuentaById(id);
  }

  @PostMapping
  public Cuenta saveCuenta(@RequestBody Cuenta cuenta) {
    return cuentaService.saveCuenta(cuenta);
  }

  @PutMapping("/{id}")
  public Cuenta updateCuenta(@PathVariable Long id, @RequestBody Cuenta cuenta) {
    Cuenta currentCuenta = cuentaService.getCuentaById(id);
    if (currentCuenta != null) {
      currentCuenta.setNumeroCuenta(cuenta.getNumeroCuenta());
      currentCuenta.setTipoCuenta(cuenta.getTipoCuenta());
      currentCuenta.setSaldoInicial(cuenta.getSaldoInicial());
      currentCuenta.setEstado(cuenta.getEstado());
      currentCuenta.setCliente(cuenta.getCliente());
      return cuentaService.saveCuenta(currentCuenta);
    }
    return null;
  }

  @DeleteMapping("/{id}")
  public void deleteCuenta(@PathVariable Long id) {
    cuentaService.deleteCuenta(id);
  }
}

