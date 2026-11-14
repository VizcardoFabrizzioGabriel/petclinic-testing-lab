
package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VetServiceTest {

    @Autowired
    private VetService vetService;

    @Test
    public void testCreateVet() {

        VetDTO vetDTO = VetDTO.builder()
                .firstName("Carlos")
                .lastName("MendozaTest")
                .email("carlos.test@petclinic.com")
                .phone("999111222")
                .active(true)
                .build();

        VetDTO newVet = vetService.create(vetDTO);

        assertNotNull(newVet.getId());
        assertEquals("Carlos", newVet.getFirstName());
        assertEquals("MendozaTest", newVet.getLastName());
    }

    @Test
    public void testUpdateVet() {

        VetDTO vetDTO = VetDTO.builder()
                .firstName("Pedro")
                .lastName("GarciaTest")
                .email("pedro.test@petclinic.com")
                .phone("999222333")
                .active(true)
                .build();

        VetDTO createdVet = vetService.create(vetDTO);

        createdVet.setFirstName("Pedro Actualizado");
        createdVet.setLastName("GarciaNuevo");
        createdVet.setPhone("999444555");

        VetDTO updatedVet = vetService.update(createdVet);

        assertEquals("Pedro Actualizado", updatedVet.getFirstName());
        assertEquals("GarciaNuevo", updatedVet.getLastName());
        assertEquals("999444555", updatedVet.getPhone());
    }

    @Test
    public void testFindVetById() {

        VetDTO vet = vetService.findById(1);

        assertNotNull(vet);
        assertEquals("James", vet.getFirstName());
        assertEquals("Carter", vet.getLastName());
    }

    @Test
    public void testFindVetByLastName() {

        var vets = vetService.findByLastName("Carter");

        assertFalse(vets.isEmpty());
        assertEquals("James", vets.get(0).getFirstName());
    }

    @Test
    public void testDeleteVet() {

        VetDTO vetDTO = VetDTO.builder()
                .firstName("Delete")
                .lastName("TestVet")
                .email("delete.test@petclinic.com")
                .phone("999555666")
                .active(true)
                .build();

        VetDTO createdVet = vetService.create(vetDTO);

        Integer id = createdVet.getId();

        vetService.delete(id);

        assertThrows(
                VetNotFoundException.class,
                () -> vetService.findById(id)
        );
    }
}
