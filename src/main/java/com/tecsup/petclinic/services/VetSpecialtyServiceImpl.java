package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.entities.VetSpecialtyId;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.exceptions.VetSpecialtyAlreadyExistsException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.mappers.SpecialtyMapper;
import com.tecsup.petclinic.mappers.VetMapper;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import com.tecsup.petclinic.repositories.VetRepository;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VetSpecialtyServiceImpl implements VetSpecialtyService {

    private final VetSpecialtyRepository vetSpecialtyRepository;
    private final VetRepository vetRepository;
    private final SpecialtyRepository specialtyRepository;
    private final VetMapper vetMapper;
    private final SpecialtyMapper specialtyMapper;

    @Override
    public VetSpecialtyDTO assign(VetSpecialtyDTO vetSpecialtyDTO) {

        if (vetSpecialtyDTO.getVetId() == null || vetSpecialtyDTO.getSpecialtyId() == null) {
            throw new IllegalArgumentException("Vet ID and Specialty ID are required");
        }

        if (!vetRepository.existsById(vetSpecialtyDTO.getVetId())) {
            throw new VetNotFoundException("Veterinarian not found: " + vetSpecialtyDTO.getVetId());
        }

        if (!specialtyRepository.existsById(vetSpecialtyDTO.getSpecialtyId())) {
            throw new SpecialtyNotFoundException("Specialty not found: " + vetSpecialtyDTO.getSpecialtyId());
        }

        VetSpecialtyId id = new VetSpecialtyId(
                vetSpecialtyDTO.getVetId(),
                vetSpecialtyDTO.getSpecialtyId()
        );

        if (vetSpecialtyRepository.existsById(id)) {
            throw new VetSpecialtyAlreadyExistsException(
                    "The specialty is already assigned to the veterinarian"
            );
        }

        VetSpecialty vetSpecialty = new VetSpecialty(
                id,
                vetSpecialtyDTO.getCertificationDate(),
                vetSpecialtyDTO.getYearsExperience() == null ? 0 : vetSpecialtyDTO.getYearsExperience(),
                vetSpecialtyDTO.getIsPrimary() != null && vetSpecialtyDTO.getIsPrimary(),
                vetSpecialtyDTO.getNotes()
        );

        return toDto(vetSpecialtyRepository.save(vetSpecialty));
    }

    @Override
    public void remove(Integer vetId, Integer specialtyId) {
        VetSpecialty vetSpecialty = vetSpecialtyRepository
                .findById(new VetSpecialtyId(vetId, specialtyId))
                .orElseThrow(() -> new VetSpecialtyNotFoundException(
                        "Assignment not found for vet " + vetId + " and specialty " + specialtyId
                ));

        vetSpecialtyRepository.delete(vetSpecialty);
    }

    @Override
    public VetSpecialtyDTO findById(Integer vetId, Integer specialtyId) {
        VetSpecialty vetSpecialty = vetSpecialtyRepository
                .findById(new VetSpecialtyId(vetId, specialtyId))
                .orElseThrow(() -> new VetSpecialtyNotFoundException(
                        "Assignment not found for vet " + vetId + " and specialty " + specialtyId
                ));

        return toDto(vetSpecialty);
    }

    @Override
    public List<VetSpecialtyDTO> findByVetId(Integer vetId) {
        return vetSpecialtyRepository.findByIdVetId(vetId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<VetSpecialtyDTO> findBySpecialtyId(Integer specialtyId) {
        return vetSpecialtyRepository.findByIdSpecialtyId(specialtyId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<SpecialtyDTO> findSpecialtiesByVet(Integer vetId) {
        List<Integer> specialtyIds = vetSpecialtyRepository.findByIdVetId(vetId)
                .stream()
                .map(vetSpecialty -> vetSpecialty.getId().getSpecialtyId())
                .toList();

        return specialtyRepository.findAllById(specialtyIds)
                .stream()
                .map(specialtyMapper::mapToDto)
                .toList();
    }

    @Override
    public List<VetDTO> findVetsBySpecialty(Integer specialtyId) {
        List<Integer> vetIds = vetSpecialtyRepository.findByIdSpecialtyId(specialtyId)
                .stream()
                .map(vetSpecialty -> vetSpecialty.getId().getVetId())
                .toList();

        return vetRepository.findAllById(vetIds)
                .stream()
                .map(vetMapper::mapToDto)
                .toList();
    }

    private VetSpecialtyDTO toDto(VetSpecialty vetSpecialty) {
        return VetSpecialtyDTO.builder()
                .vetId(vetSpecialty.getId().getVetId())
                .specialtyId(vetSpecialty.getId().getSpecialtyId())
                .certificationDate(vetSpecialty.getCertificationDate())
                .yearsExperience(vetSpecialty.getYearsExperience())
                .isPrimary(vetSpecialty.getIsPrimary())
                .notes(vetSpecialty.getNotes())
                .build();
    }
}
