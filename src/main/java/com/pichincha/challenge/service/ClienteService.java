package com.pichincha.challenge.service;

import com.pichincha.challenge.dtos.ClienteDTO;
import com.pichincha.challenge.dtos.PersonaDTO;
import com.pichincha.challenge.entities.Cliente;
import com.pichincha.challenge.repository.ClienteRepository;
import com.pichincha.challenge.utils.converter.EntityConverter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClienteService {

  private final static EntityConverter<ClienteDTO, Cliente> CLIENTE_CONVERTER = new EntityConverter<>(
      ClienteDTO.class, Cliente.class);

  @Autowired
  private ClienteRepository clienteRepository;

  @Autowired
  private PersonaService personaService;

  public List<ClienteDTO> getAllClientes() {
    log.debug("ClienteService.getAllClientes");
    return clienteRepository.findAll().stream().map(CLIENTE_CONVERTER::toT1)
        .collect(Collectors.toList());
  }

  public ClienteDTO getClienteById(Long id) {
    log.debug("ClienteService.getClienteById "+ id);

    Optional<Cliente> cliente = clienteRepository.findById(id);
    if (cliente.isPresent()) {
      PersonaDTO personaDTO = personaService.getPersonaById(cliente.get().getId());
      ClienteDTO clienteDTO = CLIENTE_CONVERTER.toT1(cliente.get());
      clienteDTO.setPersonaDTO(personaDTO);
      return clienteDTO;
    } else {
      return null;
    }
  }

  public ClienteDTO saveCliente(ClienteDTO clienteDTO) {
    log.debug("ClienteService.saveCliente "+ clienteDTO);
    Cliente cliente = CLIENTE_CONVERTER.toT2(clienteDTO);
    PersonaDTO personaDTO  = clienteDTO.getPersonaDTO();
    if(cliente.getId() == null){
      cliente.setClienteId(getIndex()+"");
      cliente.setDireccion(personaDTO.getDireccion());
      cliente.setIdentificacion(personaDTO.getIdentificacion());
      cliente.setNombre(personaDTO.getNombre());
      cliente.setGenero(personaDTO.getGenero());
      cliente.setEdad(personaDTO.getEdad());
      cliente.setTelefono(personaDTO.getTelefono());
      cliente.setId(personaService.getIndex());
    }
    clienteRepository.save(cliente);
    return clienteDTO;
  }


  public ClienteDTO updateCliente(String clienteId, ClienteDTO clienteDTO) {
    log.debug("ClienteService.updateCliente "+ clienteDTO + "");
    if (clienteRepository.findByClienteId(clienteId) != null) {
      return saveCliente(clienteDTO);
    } else {
      return null;
    }
  }

  public void deleteCliente(Long id) {
    log.debug("ClienteService.updateCliente "+ id);
    clienteRepository.deleteById(id);
  }

  public Long getIndex() {
    Long i = (long) (clienteRepository.findAll().size() + 2);
    return i;
  }
}
