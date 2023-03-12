package com.pichincha.challenge.controller;

import com.pichincha.challenge.dtos.ClienteDTO;
import com.pichincha.challenge.entities.Cliente;
import com.pichincha.challenge.service.ClienteService;
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
@RequestMapping("/clientes")
public class ClienteController {

  @Autowired
  private ClienteService clienteService;

  @GetMapping
  public List<Cliente> getAllClientes() {
    return clienteService.getAllClientes();
  }

  @GetMapping("/{id}")
  public Cliente getClienteById(@PathVariable Long id) {
    return clienteService.getClienteById(id);
  }

  @PostMapping
  public Cliente saveCliente(@RequestBody Cliente cliente) {
    return clienteService.saveCliente(cliente);
  }

  @PutMapping("/{id}")
  public ClienteDTO updateCliente(@PathVariable String clienteId, @RequestBody ClienteDTO cliente) {
  return clienteService.updateCliente(clienteId, cliente);
    }

  @DeleteMapping("/{id}")
  public void deleteCliente(@PathVariable Long id) {
    clienteService.deleteCliente(id);
  }
}
