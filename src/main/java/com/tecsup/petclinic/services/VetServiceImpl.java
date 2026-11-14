
package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.mappers.VetMapper;
import com.tecsup.petclinic.repositories.VetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VetServiceImpl implements VetService {

    private final VetRepository vetRepository;
    private final VetMapper vetMapper;

    @Override
    public VetDTO create(VetDTO vetDTO) {
        Vet vet = vetRepository.save(vetMapper.mapToEntity(vetDTO));
        return vetMapper.mapToDto(vet);
    }

    @Override
    public VetDTO update(VetDTO vetDTO) {
        if (vetDTO.getId() == null) {
            throw new IllegalArgumentException("Vet ID is required");
        }

        findById(vetDTO.getId());

        Vet vet = vetRepository.save(vetMapper.mapToEntity(vetDTO));
        return vetMapper.mapToDto(vet);
    }

    @Override
    public void delete(Integer id) {
        VetDTO vet = findById(id);
        vetRepository.delete(vetMapper.mapToEntity(vet));
    }

    @Override
    public VetDTO findById(Integer id) {
        Vet vet = vetRepository.findById(id)
                .orElseThrow(() ->
                        new VetNotFoundException("Veterinarian not found: " + id));

        return vetMapper.mapToDto(vet);
    }

    @Override
    public List<VetDTO> findByLastName(String lastName) {
        return vetRepository.findByLastName(lastName)
                .stream()
                .map(vetMapper::mapToDto)
                .toList();
    }

    @Override
    public List<VetDTO> findAll() {
        return vetRepository.findAll()
                .stream()
                .map(vetMapper::mapToDto)
                .toList();
    }
}
