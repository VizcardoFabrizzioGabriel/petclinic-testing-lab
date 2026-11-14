package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VisitServiceTest {

    private static final Integer PET_ID = 1;
    private static final Integer VET_ID = 1;

    @Autowired
    private VisitService visitService;

    private final List<Integer> createdIds = new ArrayList<>();

    @AfterEach
    public void cleanUp() {
        for (Integer id : createdIds) {
            try {
                visitService.delete(id);
            } catch (VisitNotFoundException ignored) {
                // El registro ya fue eliminado por la propia prueba
            }
        }
        createdIds.clear();
    }

    private VisitDTO createTestVisit() {
        VisitDTO visitDTO = VisitDTO.builder()
                .petId(PET_ID)
                .vetId(VET_ID)
                .visitDate(LocalDate.of(2026, 1, 15))
                .description("visita de prueba")
                .cost(new BigDecimal("50.00"))
                .build();

        VisitDTO created = visitService.create(visitDTO);
        createdIds.add(created.getId());
        return created;
    }

    @Test
    public void testCreateVisit() {

        VisitDTO created = createTestVisit();

        assertNotNull(created.getId());
        assertEquals(PET_ID, created.getPetId());
        assertEquals(VET_ID, created.getVetId());
        assertEquals(LocalDate.of(2026, 1, 15), created.getVisitDate());
        assertEquals(0, created.getCost().compareTo(new BigDecimal("50.00")));
    }

    @Test
    public void testFindVisitById() {

        VisitDTO created = createTestVisit();

        VisitDTO found = visitService.findById(created.getId());

        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
        assertEquals("visita de prueba", found.getDescription());
    }

    @Test
    public void testFindVisitById_NotFound() {

        assertThrows(
                VisitNotFoundException.class,
                () -> visitService.findById(999999)
        );
    }

    @Test
    public void testFindByPetId() {

        VisitDTO created = createTestVisit();

        List<VisitDTO> visits = visitService.findByPetId(PET_ID);

        assertFalse(visits.isEmpty());
        assertTrue(visits.stream().anyMatch(visit -> visit.getId().equals(created.getId())));
    }

    @Test
    public void testFindByVetId() {

        VisitDTO created = createTestVisit();

        List<VisitDTO> visits = visitService.findByVetId(VET_ID);

        assertFalse(visits.isEmpty());
        assertTrue(visits.stream().anyMatch(visit -> visit.getId().equals(created.getId())));
    }

    @Test
    public void testUpdateVisit() {

        VisitDTO created = createTestVisit();

        created.setDescription("visita actualizada");
        created.setCost(new BigDecimal("75.50"));
        created.setVisitDate(LocalDate.of(2026, 2, 20));

        VisitDTO updated = visitService.update(created);

        assertEquals("visita actualizada", updated.getDescription());
        assertEquals(0, updated.getCost().compareTo(new BigDecimal("75.50")));
        assertEquals(LocalDate.of(2026, 2, 20), updated.getVisitDate());
    }

    @Test
    public void testDeleteVisit() {

        VisitDTO created = createTestVisit();
        Integer id = created.getId();

        visitService.delete(id);

        assertThrows(
                VisitNotFoundException.class,
                () -> visitService.findById(id)
        );
    }
}
