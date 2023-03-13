package com.pichincha.challenge.service;

import com.pichincha.challenge.dtos.PersonaDTO;
import com.pichincha.challenge.entities.Persona;
import com.pichincha.challenge.repository.PersonaRepository;
import com.pichincha.challenge.utils.converter.EntityConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PersonaService {
  @Autowired
  private PersonaRepository personaRepository;
  private final static EntityConverter<PersonaDTO, Persona> PERSONA_CONVERTER = new EntityConverter<>(PersonaDTO.class, Persona.class);

  public PersonaDTO getPersonaById(Long id) {
    log.debug("PersonaService.getPersonaById "+id);
    Persona persona =  personaRepository.findById(id).orElse(null);
    return PERSONA_CONVERTER.toT1(persona);
  }

  public Long getIndex() {
    Long i = (long) (personaRepository.findAll().size() + 2);
    return i;
  }
}
