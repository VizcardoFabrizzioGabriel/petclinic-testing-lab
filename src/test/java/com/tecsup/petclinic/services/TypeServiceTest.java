package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TypeServiceTest {

    @Autowired
    private TypeService typeService;

    private final List<Integer> createdIds = new ArrayList<>();

    @AfterEach
    public void cleanUp() {
        for (Integer id : createdIds) {
            try {
                typeService.delete(id);
            } catch (TypeNotFoundException ignored) {
                // El registro ya fue eliminado por la propia prueba
            }
        }
        createdIds.clear();
    }

    private TypeDTO createTestType() {
        TypeDTO typeDTO = TypeDTO.builder()
                .name("guineaPigTest")
                .description("Small rodent for testing")
                .active(true)
                .sizeCategory("small")
                .averageLifespan(6)
                .careLevel("low")
                .build();

        TypeDTO created = typeService.create(typeDTO);
        createdIds.add(created.getId());
        return created;
    }

    @Test
    public void testCreateType() {

        TypeDTO created = createTestType();

        assertNotNull(created.getId());
        assertEquals("guineaPigTest", created.getName());
        assertEquals("small", created.getSizeCategory());
        assertEquals(6, created.getAverageLifespan());
        assertEquals("low", created.getCareLevel());
    }

    @Test
    public void testFindTypeById() {

        TypeDTO type = typeService.findById(1);

        assertNotNull(type);
        assertEquals("cat", type.getName());
        assertEquals("Domestic feline", type.getDescription());
    }

    @Test
    public void testFindTypeById_NotFound() {

        assertThrows(
                TypeNotFoundException.class,
                () -> typeService.findById(999999)
        );
    }

    @Test
    public void testFindByName() {

        createTestType();

        List<TypeDTO> types = typeService.findByName("guineaPigTest");

        assertFalse(types.isEmpty());
        assertEquals("small", types.get(0).getSizeCategory());
    }

    @Test
    public void testFindByActive() {

        TypeDTO created = createTestType();

        List<TypeDTO> types = typeService.findByActive(true);

        assertFalse(types.isEmpty());
        assertTrue(types.stream().anyMatch(type -> type.getId().equals(created.getId())));
    }

    @Test
    public void testUpdateType() {

        TypeDTO created = createTestType();

        created.setName("guineaPigUpdated");
        created.setDescription("Updated description");
        created.setAverageLifespan(7);
        created.setActive(false);

        TypeDTO updated = typeService.update(created);

        assertEquals("guineaPigUpdated", updated.getName());
        assertEquals("Updated description", updated.getDescription());
        assertEquals(7, updated.getAverageLifespan());
        assertFalse(updated.getActive());
    }

    @Test
    public void testDeleteType() {

        TypeDTO created = createTestType();
        Integer id = created.getId();

        typeService.delete(id);

        assertThrows(
                TypeNotFoundException.class,
                () -> typeService.findById(id)
        );
    }
}
