package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.mappers.SpecialtyMapper;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyMapper specialtyMapper;

    @Override
    public SpecialtyDTO create(SpecialtyDTO specialtyDTO) {
        Specialty specialty = specialtyRepository.save(specialtyMapper.mapToEntity(specialtyDTO));
        return specialtyMapper.mapToDto(specialty);
    }

    @Override
    public SpecialtyDTO update(SpecialtyDTO specialtyDTO) {
        if (specialtyDTO.getId() == null) {
            throw new IllegalArgumentException("Specialty ID is required");
        }

        findById(specialtyDTO.getId());

        Specialty specialty = specialtyRepository.save(specialtyMapper.mapToEntity(specialtyDTO));
        return specialtyMapper.mapToDto(specialty);
    }

    @Override
    public void delete(Integer id) {
        SpecialtyDTO specialty = findById(id);
        specialtyRepository.delete(specialtyMapper.mapToEntity(specialty));
    }

    @Override
    public SpecialtyDTO findById(Integer id) {
        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() ->
                        new SpecialtyNotFoundException("Specialty not found: " + id));

        return specialtyMapper.mapToDto(specialty);
    }

    @Override
    public List<SpecialtyDTO> findByName(String name) {
        return specialtyRepository.findByName(name)
                .stream()
                .map(specialtyMapper::mapToDto)
                .toList();
    }

    @Override
    public List<SpecialtyDTO> findByOffice(String office) {
        return specialtyRepository.findByOffice(office)
                .stream()
                .map(specialtyMapper::mapToDto)
                .toList();
    }

    @Override
    public List<SpecialtyDTO> findAll() {
        return specialtyRepository.findAll()
                .stream()
                .map(specialtyMapper::mapToDto)
                .toList();
    }
}
