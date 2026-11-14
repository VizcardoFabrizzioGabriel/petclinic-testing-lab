package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OwnerServiceTest {

    @Autowired
    private OwnerService ownerService;

    private final List<Integer> createdIds = new ArrayList<>();

    @AfterEach
    public void cleanUp() {
        for (Integer id : createdIds) {
            try {
                ownerService.delete(id);
            } catch (OwnerNotFoundException ignored) {
                // El registro ya fue eliminado por la propia prueba
            }
        }
        createdIds.clear();
    }

    private OwnerDTO createTestOwner() {
        OwnerDTO ownerDTO = OwnerDTO.builder()
                .firstName("Fabrizzio")
                .lastName("VizcardoTest")
                .address("Av. Prueba 123")
                .city("LimaTest")
                .telephone("987654321")
                .build();

        OwnerDTO created = ownerService.create(ownerDTO);
        createdIds.add(created.getId());
        return created;
    }

    @Test
    public void testCreateOwner() {

        OwnerDTO created = createTestOwner();

        assertNotNull(created.getId());
        assertEquals("Fabrizzio", created.getFirstName());
        assertEquals("VizcardoTest", created.getLastName());
        assertEquals("LimaTest", created.getCity());
    }

    @Test
    public void testFindOwnerById() {

        OwnerDTO owner = ownerService.findById(1);

        assertNotNull(owner);
        assertEquals("George", owner.getFirstName());
        assertEquals("Franklin", owner.getLastName());
    }

    @Test
    public void testFindOwnerById_NotFound() {

        assertThrows(
                OwnerNotFoundException.class,
                () -> ownerService.findById(999999)
        );
    }

    @Test
    public void testFindByLastName() {

        createTestOwner();

        List<OwnerDTO> owners = ownerService.findByLastName("VizcardoTest");

        assertFalse(owners.isEmpty());
        assertEquals("Fabrizzio", owners.get(0).getFirstName());
    }

    @Test
    public void testFindByCity() {

        createTestOwner();

        List<OwnerDTO> owners = ownerService.findByCity("LimaTest");

        assertFalse(owners.isEmpty());
        assertTrue(owners.stream().allMatch(owner -> "LimaTest".equals(owner.getCity())));
    }

    @Test
    public void testUpdateOwner() {

        OwnerDTO created = createTestOwner();

        created.setFirstName("Fabrizzio Actualizado");
        created.setCity("ArequipaTest");
        created.setTelephone("999888777");

        OwnerDTO updated = ownerService.update(created);

        assertEquals("Fabrizzio Actualizado", updated.getFirstName());
        assertEquals("ArequipaTest", updated.getCity());
        assertEquals("999888777", updated.getTelephone());
    }

    @Test
    public void testDeleteOwner() {

        OwnerDTO created = createTestOwner();
        Integer id = created.getId();

        ownerService.delete(id);

        assertThrows(
                OwnerNotFoundException.class,
                () -> ownerService.findById(id)
        );
    }
}
