package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.exceptions.VetSpecialtyAlreadyExistsException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VetSpecialtyServiceTest {

    @Autowired
    private VetSpecialtyService vetSpecialtyService;

    private final List<VetSpecialtyDTO> createdAssignments = new ArrayList<>();

    @AfterEach
    public void cleanUp() {
        for (VetSpecialtyDTO assignment : createdAssignments) {
            try {
                vetSpecialtyService.remove(assignment.getVetId(), assignment.getSpecialtyId());
            } catch (VetSpecialtyNotFoundException ignored) {
                // La relación ya fue eliminada por la propia prueba
            }
        }
        createdAssignments.clear();
    }

    private VetSpecialtyDTO assignTestSpecialty(Integer vetId, Integer specialtyId) {
        VetSpecialtyDTO vetSpecialtyDTO = VetSpecialtyDTO.builder()
                .vetId(vetId)
                .specialtyId(specialtyId)
                .certificationDate(LocalDate.of(2024, 5, 10))
                .yearsExperience(3)
                .isPrimary(false)
                .notes("asignación de prueba")
                .build();

        VetSpecialtyDTO created = vetSpecialtyService.assign(vetSpecialtyDTO);
        createdAssignments.add(created);
        return created;
    }

    @Test
    public void testAssignSpecialtyToVet() {

        VetSpecialtyDTO created = assignTestSpecialty(1, 1);

        assertNotNull(created);
        assertEquals(1, created.getVetId());
        assertEquals(1, created.getSpecialtyId());
        assertEquals(3, created.getYearsExperience());
        assertEquals(LocalDate.of(2024, 5, 10), created.getCertificationDate());
    }

    @Test
    public void testAssignDuplicate_ShouldFail() {

        assignTestSpecialty(1, 2);

        VetSpecialtyDTO duplicate = VetSpecialtyDTO.builder()
                .vetId(1)
                .specialtyId(2)
                .yearsExperience(5)
                .build();

        assertThrows(
                VetSpecialtyAlreadyExistsException.class,
                () -> vetSpecialtyService.assign(duplicate)
        );
    }

    @Test
    public void testFindSpecialtiesByVet() {

        List<SpecialtyDTO> specialties = vetSpecialtyService.findSpecialtiesByVet(2);

        assertFalse(specialties.isEmpty());
        assertTrue(specialties.stream().anyMatch(specialty -> "radiology".equals(specialty.getName())));
    }

    @Test
    public void testFindVetsBySpecialty() {

        List<VetDTO> vets = vetSpecialtyService.findVetsBySpecialty(2);

        assertFalse(vets.isEmpty());
        assertTrue(vets.stream().anyMatch(vet -> vet.getId().equals(3)));
        assertTrue(vets.stream().anyMatch(vet -> vet.getId().equals(4)));
    }

    @Test
    public void testFindSpecialtiesByVet_Empty() {

        List<VetSpecialtyDTO> assignments = vetSpecialtyService.findByVetId(6);

        assertTrue(assignments.isEmpty());
    }

    @Test
    public void testRemoveSpecialtyFromVet() {

        VetSpecialtyDTO created = assignTestSpecialty(1, 3);

        vetSpecialtyService.remove(created.getVetId(), created.getSpecialtyId());

        assertThrows(
                VetSpecialtyNotFoundException.class,
                () -> vetSpecialtyService.findById(created.getVetId(), created.getSpecialtyId())
        );
    }

    @Test
    public void testFindById_NotFound() {

        assertThrows(
                VetSpecialtyNotFoundException.class,
                () -> vetSpecialtyService.findById(6, 1)
        );
    }
}
