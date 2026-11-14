package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SpecialtyServiceTest {

    @Autowired
    private SpecialtyService specialtyService;

    private final List<Integer> createdIds = new ArrayList<>();

    @AfterEach
    public void cleanUp() {
        for (Integer id : createdIds) {
            try {
                specialtyService.delete(id);
            } catch (SpecialtyNotFoundException ignored) {
                // El registro ya fue eliminado por la propia prueba
            }
        }
        createdIds.clear();
    }

    private SpecialtyDTO createTestSpecialty() {
        SpecialtyDTO specialtyDTO = SpecialtyDTO.builder()
                .name("cardiologyTest")
                .office("ConsultorioTest")
                .openHour(8)
                .closeHour(16)
                .build();

        SpecialtyDTO created = specialtyService.create(specialtyDTO);
        createdIds.add(created.getId());
        return created;
    }

    @Test
    public void testCreateSpecialty() {

        SpecialtyDTO created = createTestSpecialty();

        assertNotNull(created.getId());
        assertEquals("cardiologyTest", created.getName());
        assertEquals("ConsultorioTest", created.getOffice());
        assertEquals(8, created.getOpenHour());
        assertEquals(16, created.getCloseHour());
    }

    @Test
    public void testFindSpecialtyById() {

        SpecialtyDTO specialty = specialtyService.findById(1);

        assertNotNull(specialty);
        assertEquals("radiology", specialty.getName());
        assertEquals("Farewell", specialty.getOffice());
    }

    @Test
    public void testFindSpecialtyById_NotFound() {

        assertThrows(
                SpecialtyNotFoundException.class,
                () -> specialtyService.findById(999999)
        );
    }

    @Test
    public void testFindByName() {

        createTestSpecialty();

        List<SpecialtyDTO> specialties = specialtyService.findByName("cardiologyTest");

        assertFalse(specialties.isEmpty());
        assertEquals("ConsultorioTest", specialties.get(0).getOffice());
    }

    @Test
    public void testFindByOffice() {

        createTestSpecialty();

        List<SpecialtyDTO> specialties = specialtyService.findByOffice("ConsultorioTest");

        assertFalse(specialties.isEmpty());
        assertEquals("cardiologyTest", specialties.get(0).getName());
    }

    @Test
    public void testUpdateSpecialty() {

        SpecialtyDTO created = createTestSpecialty();

        created.setName("cardiologyUpdated");
        created.setOffice("ConsultorioUpdated");
        created.setCloseHour(18);

        SpecialtyDTO updated = specialtyService.update(created);

        assertEquals("cardiologyUpdated", updated.getName());
        assertEquals("ConsultorioUpdated", updated.getOffice());
        assertEquals(18, updated.getCloseHour());
    }

    @Test
    public void testDeleteSpecialty() {

        SpecialtyDTO created = createTestSpecialty();
        Integer id = created.getId();

        specialtyService.delete(id);

        assertThrows(
                SpecialtyNotFoundException.class,
                () -> specialtyService.findById(id)
        );
    }
}
