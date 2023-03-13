package com.pichincha.challenge.controller;
import com.pichincha.challenge.dtos.CuentaDTO;
import com.pichincha.challenge.service.CuentaService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CuentaController {

  @Autowired
  private CuentaService cuentaService;

  @GetMapping
  public List<CuentaDTO> getAllCuentas() {
    return cuentaService.getAllCuentas();
  }

  @GetMapping("/{id}")
  public CuentaDTO getCuentaById(@PathVariable Long id) {
    log.debug("CuentaController.getCuentaById "+ id);
    return cuentaService.getCuentaById(id);
  }

  @PostMapping
  public CuentaDTO saveCuenta(@RequestBody CuentaDTO cuentaDTO) {
    log.debug("CuentaController.saveCuenta "+ cuentaDTO);
    return cuentaService.saveCuenta(cuentaDTO);
  }

  @PutMapping("/{id}")
  public CuentaDTO updateCuenta(@PathVariable Long id, @RequestBody CuentaDTO cuentaDTO) {
    log.debug("CuentaController.updateCuenta" + cuentaDTO);
      return cuentaService.saveCuenta(cuentaDTO);
  }

  @DeleteMapping("/{id}")
  public void deleteCuenta(@PathVariable Long id) {
    log.debug("CuentaController.deleteCuenta "+id);
    cuentaService.deleteCuenta(id);
  }
}

