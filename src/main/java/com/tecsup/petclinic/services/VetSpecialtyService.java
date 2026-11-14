package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.exceptions.VetSpecialtyAlreadyExistsException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;

import java.util.List;

public interface VetSpecialtyService {

    VetSpecialtyDTO assign(VetSpecialtyDTO vetSpecialtyDTO) throws VetSpecialtyAlreadyExistsException;

    void remove(Integer vetId, Integer specialtyId) throws VetSpecialtyNotFoundException;

    VetSpecialtyDTO findById(Integer vetId, Integer specialtyId) throws VetSpecialtyNotFoundException;

    List<VetSpecialtyDTO> findByVetId(Integer vetId);

    List<VetSpecialtyDTO> findBySpecialtyId(Integer specialtyId);

    List<SpecialtyDTO> findSpecialtiesByVet(Integer vetId);

    List<VetDTO> findVetsBySpecialty(Integer specialtyId);
}
