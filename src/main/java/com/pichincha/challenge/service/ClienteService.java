package com.pichincha.challenge.service;

import com.pichincha.challenge.dtos.ClienteDTO;
import com.pichincha.challenge.dtos.PersonaDTO;
import com.pichincha.challenge.entities.Cliente;
import com.pichincha.challenge.entities.Persona;
import com.pichincha.challenge.repository.ClienteRepository;
import com.pichincha.challenge.repository.PersonaRepository;
import com.pichincha.challenge.utils.converter.EntityConverter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

  private final static EntityConverter<ClienteDTO, Cliente> CLIENTE_CONVERTER = new EntityConverter<>(
      ClienteDTO.class, Cliente.class);
  private final static EntityConverter<PersonaDTO, Persona> PERSONA_CONVERTER = new EntityConverter<>(
      PersonaDTO.class, Persona.class);

  @Autowired
  private ClienteRepository clienteRepository;

  @Autowired
  private PersonaRepository personaRepository;

  public List<Cliente> getAllClientes() {
    return clienteRepository.findAll();
  }

  public Cliente getClienteById(Long id) {
    return clienteRepository.findById(id).orElse(null);
  }

  public Cliente saveCliente(Cliente cliente) {
    //No se debe hacer la secuencia no se porque no funciona
    Long i = (long) (clienteRepository.findAll().size() + 2);
    cliente.setId(i);
    return clienteRepository.save(cliente);

  }


  public ClienteDTO updateCliente(String clienteId, ClienteDTO clienteDTO) {

    Persona persona = PERSONA_CONVERTER.toT2(clienteDTO.getPersonaDTO());
    Cliente cliente = CLIENTE_CONVERTER.toT2(clienteDTO);

    if (clienteRepository.findByClienteId(clienteId) != null) {
      personaRepository.save(persona);
      clienteRepository.save(cliente);
      return clienteDTO;
    }
    return null;
  }

  public void deleteCliente(Long id) {
    clienteRepository.deleteById(id);
  }
}
