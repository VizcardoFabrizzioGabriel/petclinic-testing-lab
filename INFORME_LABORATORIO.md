# Informe del laboratorio PetClinic

## Introducción

Este laboratorio consistió en completar las pruebas y los servicios de seis módulos del
proyecto PetClinic: veterinarios, propietarios, especialidades, visitas, tipos de mascotas
y la relación entre veterinarios y especialidades. El proyecto ya traía el módulo de
mascotas (`Pet`) y el de veterinarios (`Vet`) avanzado, así que mi trabajo se centró en
revisar lo existente y construir los módulos que faltaban siguiendo la misma estructura.

Trabajé sobre el repositorio `petclinic-testing-lab`, que es un fork del proyecto original
del curso. Usé Java 21, Spring Boot 3.5.14, Spring Data JPA, H2, JUnit 5, Lombok y
MapStruct. Las pruebas se ejecutaron contra la base de datos H2 con el perfil `h2`.

## Objetivos

- Verificar que el módulo de veterinarios estuviera completo y que sus pruebas pasaran.
- Implementar los módulos de propietarios, especialidades, visitas y tipos de mascotas.
- Implementar la relación entre veterinarios y especialidades con clave primaria compuesta.
- Escribir pruebas de integración para cada módulo, conectadas a H2.
- Mantener el orden del proyecto: entidades, repositorios, DTO, mappers, servicios y
  excepciones.
- Organizar el trabajo con ramas por módulo e integrar todo en `develop`.
- Dejar evidencia de las pruebas ejecutadas y documentar lo que quedó pendiente.

## Procedimiento

Primero revisé la estructura del proyecto y el estado de Git. Encontré que la rama de
veterinarios tenía siete commits locales sin publicar y que su archivo de pruebas estaba
modificado pero sin confirmar. También noté que muchos archivos aparecían como cambiados
solo por diferencias de fin de línea (CRLF), así que comprobé que no hubiera cambios
reales antes de continuar.

Después ejecuté las pruebas de veterinarios. Pasaron las cinco. Confirmé ese archivo de
pruebas y lo publiqué junto con su rama. Luego integré esa rama en `develop`.

Para cada módulo pendiente seguí el mismo orden: creé una rama desde `develop`, escribí
la entidad, el repositorio, el DTO, el mapper, la excepción, la interfaz del servicio, la
implementación y las pruebas. Hice un commit por archivo, con mensajes en el formato
Conventional Commits. Al terminar cada módulo ejecuté sus pruebas, corregí lo que fue
necesario y fusioné la rama en `develop` con `git merge --no-ff`.

## Desarrollo de los seis casos

### 1. Veterinarios (`vets`)

Revisé las ocho piezas del módulo: entidad, repositorio, DTO, mapper, excepción, interfaz,
implementación y prueba. El `VetServiceTest.java` estaba en `src/test/java`, y no había
clases de prueba dentro de `src/main/java`. Ejecuté las pruebas y las cinco pasaron:
crear, actualizar, buscar por ID, buscar por apellido y eliminar. No rehíce el módulo; solo
confirmé el archivo de pruebas que estaba pendiente y lo subí a Git.

### 2. Propietarios (`owners`)

Creé la entidad `Owner` con los campos de la tabla: nombres, apellidos, dirección, ciudad y
teléfono. El repositorio permite buscar por nombre, apellido y ciudad. El servicio ofrece
crear, actualizar, eliminar, buscar por ID, buscar por apellido, buscar por ciudad y listar
todos. Antes de actualizar o eliminar comprueba que el registro exista y lanza
`OwnerNotFoundException` si no lo encuentra.

Escribí siete pruebas de integración. Cada una crea su propio propietario y lo elimina en
`@AfterEach`, para no depender del orden de ejecución ni dejar registros sueltos. Todas
pasaron.

### 3. Especialidades (`specialties`)

Implementé la entidad `Specialty` con nombre, consultorio y horario de apertura y cierre.
El repositorio permite buscar por nombre y por consultorio, y el servicio mantiene las
operaciones habituales de creación, consulta, actualización y eliminación.

Aquí encontré un problema real: MapStruct no trasladaba los campos `hOpen` y `hClose` al
DTO, y las pruebas devolvían `null` en esas horas. Revisé el mapper generado y confirmé que
el problema era el nombre de las propiedades. Los renombré a `openHour` y `closeHour`,
manteniendo las columnas `h_open` y `h_close` con `@Column`, y las siete pruebas pasaron.

### 4. Visitas (`visits`)

Creé la entidad `Visit` respetando las relaciones de la tabla: `pet_id`, `vet_id`,
`visit_date`, descripción y costo con `BigDecimal`. El repositorio permite buscar visitas
por mascota y por veterinario. El servicio valida que la visita exista antes de actualizar
o eliminar.

Las pruebas usan una mascota y un veterinario que ya existen en los datos iniciales, pero
crean su propia visita y la borran al terminar. Así no se alteran los registros de ejemplo
ni se asume un identificador generado. Las siete pruebas pasaron.

### 5. Tipos de mascotas (`types`)

Implementé la entidad `Type` con nombre, descripción, estado activo, categoría de tamaño,
esperanza de vida promedio y nivel de cuidado. El repositorio permite buscar por nombre y
filtrar por estado. El servicio incluye las operaciones CRUD y las dos búsquedas.

Escribí siete pruebas con un tipo propio para cada una, con limpieza posterior. Todas
pasaron.

### 6. Relación veterinario–especialidad (`vet_specialties`)

Esta tabla tiene clave primaria compuesta por `vet_id` y `specialty_id`. La representé con
`@EmbeddedId` y una clase `VetSpecialtyId` anotada con `@Embeddable`. La entidad conserva
las columnas adicionales: fecha de certificación, años de experiencia, si es la
especialidad principal y notas.

El repositorio permite buscar relaciones por veterinario y por especialidad. El servicio
ofrece asignar una especialidad, eliminarla, buscar la relación, listar las relaciones de
un veterinario, listar las de una especialidad, obtener las especialidades de un
veterinario y obtener los veterinarios de una especialidad. Si se intenta asignar dos veces
la misma relación, lanza una excepción propia.

Escribí siete pruebas: asignación, asignación duplicada, búsqueda en ambos sentidos,
listado vacío, eliminación de la relación y búsqueda inexistente. Todas pasaron.

## Observaciones

1. El módulo de veterinarios ya estaba implementado y sus pruebas funcionaban. Decidí
   verificarlo en lugar de reescribirlo, porque volver a hacer lo mismo solo habría
   agregado ruido al historial.

2. Al inicio, casi todos los archivos aparecían como modificados. Comprobé que la
   diferencia era únicamente de fin de línea y no había cambios reales de código, así que
   restauré esos archivos antes de trabajar para que los commits mostraran solo lo que
   realmente cambié.

3. El error más útil de todo el laboratorio fue el de las horas en `Specialty`. Las pruebas
   fallaban con `expected: 8 but was: null`, y al leer el mapper generado entendí que el
   problema no estaba en la base de datos sino en cómo MapStruct interpretaba los nombres
   `hOpen` y `hClose`.

4. Los datos iniciales de `data.sql` usan identificadores fijos. Por eso las pruebas no
   asumen que un registro nuevo tendrá un ID concreto: crean su propio registro y comparan
   contra ese ID devuelto por el servicio.

5. La tabla `owners` tiene una relación en cascada con `pets`. Si se borrara un propietario
   de los datos iniciales, también se borrarían sus mascotas y se afectarían las pruebas de
   `Pet`. Para evitarlo, las pruebas de propietarios solo eliminan los registros que ellas
   mismas crean.

## Conclusiones

1. Los seis módulos quedaron implementados con la misma estructura de `Pet` y `Vet`, lo que
   facilita leer el proyecto y mantenerlo.

2. La suite completa terminó con `Tests run: 55, Failures: 0, Errors: 0, Skipped: 0` y
   `BUILD SUCCESS`, así que las pruebas de integración funcionan en conjunto y no solo por
   separado.

3. Aprendí a revisar el código que genera MapStruct. Ver el mapper generado fue más rápido
   que adivinar por qué un campo llegaba vacío al DTO.

4. Las pruebas son más confiables cuando crean sus propios datos y los limpian. Depender de
   los registros de ejemplo o del orden de ejecución genera fallos difíciles de explicar.

5. El trabajo por ramas con commits pequeños permitió avanzar módulo por módulo y detectar
   errores antes de integrar. Como el repositorio es un fork y se decidió no usar Pull
   Requests, la integración se hizo con merges locales hacia `develop`, que quedaron
   registrados en el historial y publicados en GitHub.

## Evidencias pendientes de incorporar

Las capturas de pantalla debe tomarlas el estudiante. La lista exacta de comandos y
capturas está en `evidencias/EVIDENCIAS.md`. Hasta el momento no se ha incluido ninguna
imagen en el repositorio.

Quedan pendientes estas capturas:

- Versión de Java y Maven.
- Ejecución de pruebas de cada uno de los seis módulos.
- Resumen de la suite completa.
- Ramas locales y remotas.
- Historial de commits.
- Merges de integración en `develop`.
- Estado final de Git.
- Página del repositorio en GitHub.

## Ruta del repositorio

- Repositorio remoto: https://github.com/VizcardoFabrizzioGabriel/petclinic-testing-lab
- Ruta local: `C:\Users\vizca\Documents\petclinic-testing-lab`
- Rama de integración: `develop`
