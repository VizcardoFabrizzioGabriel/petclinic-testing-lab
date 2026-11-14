package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.mappers.VisitMapper;
import com.tecsup.petclinic.repositories.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;

    @Override
    public VisitDTO create(VisitDTO visitDTO) {
        Visit visit = visitRepository.save(visitMapper.mapToEntity(visitDTO));
        return visitMapper.mapToDto(visit);
    }

    @Override
    public VisitDTO update(VisitDTO visitDTO) {
        if (visitDTO.getId() == null) {
            throw new IllegalArgumentException("Visit ID is required");
        }

        findById(visitDTO.getId());

        Visit visit = visitRepository.save(visitMapper.mapToEntity(visitDTO));
        return visitMapper.mapToDto(visit);
    }

    @Override
    public void delete(Integer id) {
        VisitDTO visit = findById(id);
        visitRepository.delete(visitMapper.mapToEntity(visit));
    }

    @Override
    public VisitDTO findById(Integer id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() ->
                        new VisitNotFoundException("Visit not found: " + id));

        return visitMapper.mapToDto(visit);
    }

    @Override
    public List<VisitDTO> findByPetId(Integer petId) {
        return visitRepository.findByPetId(petId)
                .stream()
                .map(visitMapper::mapToDto)
                .toList();
    }

    @Override
    public List<VisitDTO> findByVetId(Integer vetId) {
        return visitRepository.findByVetId(vetId)
                .stream()
                .map(visitMapper::mapToDto)
                .toList();
    }

    @Override
    public List<VisitDTO> findAll() {
        return visitRepository.findAll()
                .stream()
                .map(visitMapper::mapToDto)
                .toList();
    }
}
