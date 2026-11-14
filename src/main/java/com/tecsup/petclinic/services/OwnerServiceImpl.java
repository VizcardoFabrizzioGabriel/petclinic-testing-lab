package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.mappers.OwnerMapper;
import com.tecsup.petclinic.repositories.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    @Override
    public OwnerDTO create(OwnerDTO ownerDTO) {
        Owner owner = ownerRepository.save(ownerMapper.mapToEntity(ownerDTO));
        return ownerMapper.mapToDto(owner);
    }

    @Override
    public OwnerDTO update(OwnerDTO ownerDTO) {
        if (ownerDTO.getId() == null) {
            throw new IllegalArgumentException("Owner ID is required");
        }

        findById(ownerDTO.getId());

        Owner owner = ownerRepository.save(ownerMapper.mapToEntity(ownerDTO));
        return ownerMapper.mapToDto(owner);
    }

    @Override
    public void delete(Integer id) {
        OwnerDTO owner = findById(id);
        ownerRepository.delete(ownerMapper.mapToEntity(owner));
    }

    @Override
    public OwnerDTO findById(Integer id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() ->
                        new OwnerNotFoundException("Owner not found: " + id));

        return ownerMapper.mapToDto(owner);
    }

    @Override
    public List<OwnerDTO> findByLastName(String lastName) {
        return ownerRepository.findByLastName(lastName)
                .stream()
                .map(ownerMapper::mapToDto)
                .toList();
    }

    @Override
    public List<OwnerDTO> findByCity(String city) {
        return ownerRepository.findByCity(city)
                .stream()
                .map(ownerMapper::mapToDto)
                .toList();
    }

    @Override
    public List<OwnerDTO> findAll() {
        return ownerRepository.findAll()
                .stream()
                .map(ownerMapper::mapToDto)
                .toList();
    }
}
