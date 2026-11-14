package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.Type;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.mappers.TypeMapper;
import com.tecsup.petclinic.repositories.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {

    private final TypeRepository typeRepository;
    private final TypeMapper typeMapper;

    @Override
    public TypeDTO create(TypeDTO typeDTO) {
        Type type = typeRepository.save(typeMapper.mapToEntity(typeDTO));
        return typeMapper.mapToDto(type);
    }

    @Override
    public TypeDTO update(TypeDTO typeDTO) {
        if (typeDTO.getId() == null) {
            throw new IllegalArgumentException("Type ID is required");
        }

        findById(typeDTO.getId());

        Type type = typeRepository.save(typeMapper.mapToEntity(typeDTO));
        return typeMapper.mapToDto(type);
    }

    @Override
    public void delete(Integer id) {
        TypeDTO type = findById(id);
        typeRepository.delete(typeMapper.mapToEntity(type));
    }

    @Override
    public TypeDTO findById(Integer id) {
        Type type = typeRepository.findById(id)
                .orElseThrow(() ->
                        new TypeNotFoundException("Pet type not found: " + id));

        return typeMapper.mapToDto(type);
    }

    @Override
    public List<TypeDTO> findByName(String name) {
        return typeRepository.findByName(name)
                .stream()
                .map(typeMapper::mapToDto)
                .toList();
    }

    @Override
    public List<TypeDTO> findByActive(Boolean active) {
        return typeRepository.findByActive(active)
                .stream()
                .map(typeMapper::mapToDto)
                .toList();
    }

    @Override
    public List<TypeDTO> findAll() {
        return typeRepository.findAll()
                .stream()
                .map(typeMapper::mapToDto)
                .toList();
    }
}
