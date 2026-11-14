# Evidencias del laboratorio

Este documento indica qué capturas debes tomar tú. No contiene imágenes ni resultados
inventados: todo lo que aparece aquí fue ejecutado realmente en el repositorio local el
13 de noviembre de 2026.

## 1. Requisito previo

Ejecuta los comandos desde la carpeta del proyecto en Windows:

```bat
cd C:\Users\vizca\Documents\petclinic-testing-lab
```

El proyecto necesita Java 21. Verifica que tu `JAVA_HOME` apunte a un JDK 21 antes de
empezar y luego confirma la versión:

```bat
java -version
.\mvnw.cmd -v
```

Captura 1: consola mostrando Java 21 y Maven 3.9.x.

## 2. Pruebas por módulo

Ejecuta cada comando por separado y captura la línea `Tests run:` junto con
`BUILD SUCCESS`.

```bat
.\mvnw.cmd test "-Dspring.profiles.active=h2" "-Dtest=VetServiceTest"
.\mvnw.cmd test "-Dspring.profiles.active=h2" "-Dtest=OwnerServiceTest"
.\mvnw.cmd test "-Dspring.profiles.active=h2" "-Dtest=SpecialtyServiceTest"
.\mvnw.cmd test "-Dspring.profiles.active=h2" "-Dtest=VisitServiceTest"
.\mvnw.cmd test "-Dspring.profiles.active=h2" "-Dtest=TypeServiceTest"
.\mvnw.cmd test "-Dspring.profiles.active=h2" "-Dtest=VetSpecialtyServiceTest"
```

Resultados obtenidos en la ejecución real:

| Módulo | Pruebas | Fallos | Errores |
|---|---|---|---|
| Vet | 5 | 0 | 0 |
| Owner | 7 | 0 | 0 |
| Specialty | 7 | 0 | 0 |
| Visit | 7 | 0 | 0 |
| Type | 7 | 0 | 0 |
| VetSpecialty | 7 | 0 | 0 |

Capturas 2 a 7: una por cada módulo.

## 3. Suite completa

```bat
.\mvnw.cmd test "-Dspring.profiles.active=h2"
```

Resultado real: `Tests run: 55, Failures: 0, Errors: 0, Skipped: 0` y `BUILD SUCCESS`.

Captura 8: resumen final de Maven con el total de pruebas y `BUILD SUCCESS`.

## 4. Ramas

```bat
git branch -a
git branch -vv
git ls-remote --heads origin
```

Ramas creadas y publicadas:

- `feature/vet-service-tests`
- `feature/owner-service-tests`
- `feature/specialty-service-tests`
- `feature/visit-service-tests`
- `feature/type-service-tests`
- `feature/vet-specialty-service-tests`
- `docs/laboratorio`

Captura 9: listado de ramas locales y remotas.
Captura 10: página de ramas del repositorio en GitHub.

## 5. Historial de commits

```bat
git log --oneline --graph --all --decorate
git log --oneline --no-merges 04e591c..develop
```

El historial tiene 49 commits de implementación (incluidos los 8 de Vet), 2 de
documentación y 7 commits de integración hacia `develop`.

Captura 11: grafo del historial.
Captura 12: lista de commits sin merges.

## 6. Integración de ramas

```bat
git log --merges --oneline develop
```

Deben aparecer seis integraciones de módulos (vet, owner, specialty, visit, type y
vet-specialty) y una de documentación.

Captura 13: commits de tipo merge en `develop`.

> Nota: el repositorio es un fork y se decidió no abrir Pull Requests. La integración se
> hizo con `git merge --no-ff` hacia `develop`, tal como quedó registrado en el historial.

## 7. Estado final

```bat
git status
git log --oneline -1
```

Captura 14: `git status` sin cambios pendientes y el último commit de `develop`.

## 8. Repositorio

Captura 15: página principal del repositorio en GitHub mostrando la rama `develop`.

https://github.com/VizcardoFabrizzioGabriel/petclinic-testing-lab
