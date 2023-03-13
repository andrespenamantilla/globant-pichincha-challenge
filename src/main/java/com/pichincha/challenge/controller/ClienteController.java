package com.pichincha.challenge.controller;

import com.pichincha.challenge.dtos.ClienteDTO;
import com.pichincha.challenge.service.ClienteService;
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
@RequestMapping("/clientes")
@Slf4j
public class ClienteController {

  @Autowired
  private ClienteService clienteService;

  @GetMapping
  public List<ClienteDTO> getAllClientes() {
    log.debug("ClienteController.clienteService");
    return clienteService.getAllClientes();
  }

  @GetMapping("/{id}")
  public ClienteDTO getClienteById(@PathVariable Long id) {
    log.debug("ClienteController.getClienteById"+ id);
    return clienteService.getClienteById(id);
  }

  @PostMapping
  public ClienteDTO saveCliente(@RequestBody ClienteDTO clienteDTO) {
    log.debug("ClienteController.saveCliente"+ clienteDTO);
    return clienteService.saveCliente(clienteDTO);
  }

  @PutMapping("/{id}")
  public ClienteDTO updateCliente(@PathVariable String clienteId, @RequestBody ClienteDTO cliente) {
    log.debug("ClienteController.updateCliente"+ cliente);
    return clienteService.updateCliente(clienteId, cliente);
    }

  @DeleteMapping("/{id}")
  public void deleteCliente(@PathVariable Long id) {
    log.debug("ClienteController.deleteCliente"+ id);
    clienteService.deleteCliente(id);
  }
}
