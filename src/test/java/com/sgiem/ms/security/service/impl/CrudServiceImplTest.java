package com.sgiem.ms.security.service.impl;

import com.sgiem.ms.security.models.entity.UserCredential;
import com.sgiem.ms.security.repository.GenericRepositories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CrudServiceImplTest {

    // Creamos una implementación concreta para la clase abstracta CrudServiceImpl
    private static class TestCrudServiceImpl<T, ID> extends CrudServiceImpl<T, ID> {
        private final GenericRepositories<T, ID> repo;

        public TestCrudServiceImpl(GenericRepositories<T, ID> repo) {
            this.repo = repo;
        }

        @Override
        protected GenericRepositories<T, ID> getRepo() {
            return repo;
        }
    }

    @InjectMocks
    private TestCrudServiceImpl<UserCredential, Integer> crudService;

    @Mock
    private GenericRepositories<UserCredential, Integer> repositories;

    @Test
    void testSave() {
        // Arrange
        UserCredential entity = new UserCredential(); // Crea una instancia de YourEntity para la prueba

        // Act
        crudService.save(entity);

        // Assert
        Mockito.verify(repositories, Mockito.times(1)).save(entity); // Verifica que el método save en el repositorio se llame una vez
    }

    @Test
    void testUpdate() throws Exception {
        // Arrange
        UserCredential entity = new UserCredential(); // Crea una instancia de UserCredential para la prueba
        entity.setIdUser(1); // Establece un ID para la entidad, ya que es probable que el método update requiera un ID existente

        // Configura el comportamiento del repositorio
        Mockito.when(repositories.save(entity)).thenReturn(entity);

        // Act
        UserCredential result = crudService.update(entity);

        // Assert
        Mockito.verify(repositories, Mockito.times(1)).save(entity); // Verifica que el método save en el repositorio se llame una vez
        assertEquals(entity, result); // Verifica que el resultado sea igual a la entidad proporcionada
    }

    @Test
    void testFindAll() {
        // Arrange
        List<UserCredential> entities = new ArrayList<>();
        // Agrega elementos a la lista de entidades para simular el resultado del repositorio
        entities.add(new UserCredential());
        entities.add(new UserCredential());

        Mockito.when(repositories.findAll()).thenReturn(entities);

        // Act
        List<UserCredential> result = crudService.findAll();

        // Assert
        Mockito.verify(repositories, Mockito.times(1)).findAll(); // Verifica que el método findAll en el repositorio se llame una vez
        assertEquals(entities, result); // Verifica que el resultado sea igual a la lista de entidades simulada
    }

    @Test
    void testFindById() throws Exception {
        // Arrange
        Integer id = 1;
        UserCredential entity = new UserCredential(); // Crea una instancia de YourEntity para simular el resultado del repositorio

        Mockito.when(repositories.findById(id)).thenReturn(Optional.of(entity));

        // Act
        UserCredential result = crudService.findById(id);

        // Assert
        Mockito.verify(repositories, Mockito.times(1)).findById(id); // Verifica que el método findById en el repositorio se llame una vez
        assertEquals(entity, result); // Verifica que el resultado sea igual a la entidad simulada
    }

    @Test
    void testDelete() throws Exception {
        // Arrange
        Integer id = 1;

        // Act
        crudService.delete(id);

        // Assert
        Mockito.verify(repositories, Mockito.times(1)).deleteById(id); // Verifica que el método deleteById en el repositorio se llame una vez
    }
}