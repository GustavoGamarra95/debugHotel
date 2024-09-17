package com.HotelTremvago.HotelTremvago.services;

import com.HotelTremvago.HotelTremvago.entities.UsuarioEntity;
import com.HotelTremvago.HotelTremvago.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testSave() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuario);

        UsuarioEntity result = usuarioService.save(usuario);

        assertNotNull(result);
        assertEquals(usuario.getId(), result.getId());
    }

    @Test
    public void testSaveException() {
        UsuarioEntity usuario = new UsuarioEntity();
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenThrow(new RuntimeException("Erro"));

        UsuarioEntity result = usuarioService.save(usuario);

        assertNotNull(result);
        assertEquals(UsuarioEntity.class, result.getClass());
    }

//    @Test
//    public void testSaveWithNull() {
//        UsuarioEntity result = usuarioService.save(null);
//
//        assertNotNull(result);
//        assertEquals(UsuarioEntity.class, result.getClass());
//    }



    @Test
    public void testDelete() {
        doNothing().when(usuarioRepository).deleteById(anyLong());

        String result = usuarioService.delete(1L);

        assertEquals("Usuario deletado", result);
    }

    @Test
    public void testDeleteException() {
        doThrow(new EmptyResultDataAccessException(1)).when(usuarioRepository).deleteById(anyLong());

        String result = usuarioService.delete(1L);

        assertEquals("Nao foi possivel deletar usuario", result);
    }

    @Test
    public void testDeleteNotFound() {
        // Simula a exceção que ocorre quando se tenta deletar um ID não encontrado
        doThrow(new EmptyResultDataAccessException(1)).when(usuarioRepository).deleteById(anyLong());

        String result = usuarioService.delete(1L);

        // Espera-se uma mensagem de erro indicando que não foi possível deletar o usuário
        assertEquals("Nao foi possivel deletar usuario", result);
    }

    // Testes para o método update

    @Test
    public void testUpdate() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuario);

        UsuarioEntity result = usuarioService.update(usuario, 1L);

        assertNotNull(result);
        assertEquals(usuario.getId(), result.getId());
    }

    @Test
    public void testUpdateException() {
        UsuarioEntity usuario = new UsuarioEntity();
        when(usuarioRepository.findById(anyLong())).thenThrow(new RuntimeException("Erro"));

        UsuarioEntity result = usuarioService.update(usuario, 1L);

        assertNull(result);
    }

    @Test
    public void testUpdateWithNullEntity() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(new UsuarioEntity()));

        UsuarioEntity result = usuarioService.update(null, 1L);

        assertNull(result);
    }

    // Testes para o método findById

    @Test
    public void testFindById() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));

        UsuarioEntity result = usuarioService.findById(1L);

        assertNotNull(result);
        assertEquals(usuario.getId(), result.getId());
    }

    @Test
    public void testFindByIdException() {
        when(usuarioRepository.findById(anyLong())).thenThrow(new RuntimeException("Erro"));

        UsuarioEntity result = usuarioService.findById(1L);

        assertNotNull(result);
        assertEquals(UsuarioEntity.class, result.getClass());
    }

    @Test
    public void testFindByIdNotFound() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        UsuarioEntity result = usuarioService.findById(1L);

        assertNotNull(result);
        assertEquals(UsuarioEntity.class, result.getClass());
    }

    // Testes para o método findAll

    @Test
    public void testFindAll() {
        UsuarioEntity usuario1 = new UsuarioEntity();
        UsuarioEntity usuario2 = new UsuarioEntity();
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));

        List<UsuarioEntity> result = usuarioService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindAllException() {
        when(usuarioRepository.findAll()).thenThrow(new RuntimeException("Erro"));

        List<UsuarioEntity> result = usuarioService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindAllEmpty() {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        List<UsuarioEntity> result = usuarioService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
