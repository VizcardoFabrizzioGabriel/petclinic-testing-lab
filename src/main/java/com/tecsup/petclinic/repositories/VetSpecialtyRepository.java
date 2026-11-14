package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.entities.VetSpecialtyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VetSpecialtyRepository extends JpaRepository<VetSpecialty, VetSpecialtyId> {

    List<VetSpecialty> findByIdVetId(Integer vetId);

    List<VetSpecialty> findByIdSpecialtyId(Integer specialtyId);

    boolean existsByIdVetIdAndIdSpecialtyId(Integer vetId, Integer specialtyId);
}
