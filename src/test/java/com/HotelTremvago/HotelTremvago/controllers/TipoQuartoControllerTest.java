package com.HotelTremvago.HotelTremvago.controllers;

import com.HotelTremvago.HotelTremvago.entities.TipoQuartoEntity;
import com.HotelTremvago.HotelTremvago.services.TipoQuartoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TipoQuartoControllerTest {

    @InjectMocks
    private TipoQuartoController tipoQuartoController;

    @Mock
    private TipoQuartoService tipoQuartoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveTipoQuarto_Success() {
        TipoQuartoEntity tipoQuartoEntity = new TipoQuartoEntity();
        when(tipoQuartoService.save(any(TipoQuartoEntity.class))).thenReturn(tipoQuartoEntity);

        ResponseEntity<TipoQuartoEntity> response = tipoQuartoController.save(tipoQuartoEntity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tipoQuartoEntity, response.getBody());
        verify(tipoQuartoService).save(any(TipoQuartoEntity.class));
    }

    @Test
    public void testSaveTipoQuarto_Failure() {
        TipoQuartoEntity tipoQuartoEntity = new TipoQuartoEntity();
        when(tipoQuartoService.save(any(TipoQuartoEntity.class))).thenThrow(new RuntimeException());

        ResponseEntity<TipoQuartoEntity> response = tipoQuartoController.save(tipoQuartoEntity);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(tipoQuartoService).save(any(TipoQuartoEntity.class));
    }

    @Test
    public void testDeleteTipoQuarto_Success() {
        when(tipoQuartoService.delete(anyLong())).thenReturn("Deletado com sucesso");

        ResponseEntity<String> response = tipoQuartoController.delete(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deletado com sucesso", response.getBody());
        verify(tipoQuartoService).delete(anyLong());
    }

    @Test
    public void testDeleteTipoQuarto_Failure() {
        when(tipoQuartoService.delete(anyLong())).thenThrow(new RuntimeException());

        ResponseEntity<String> response = tipoQuartoController.delete(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Não foi possível deletar tipo de quarto", response.getBody());
        verify(tipoQuartoService).delete(anyLong());
    }

    @Test
    public void testUpdateTipoQuarto_Success() {
        TipoQuartoEntity tipoQuartoEntity = new TipoQuartoEntity();
        TipoQuartoEntity updatedTipoQuarto = new TipoQuartoEntity();
        when(tipoQuartoService.update(any(TipoQuartoEntity.class), anyLong())).thenReturn(updatedTipoQuarto);

        ResponseEntity<TipoQuartoEntity> response = tipoQuartoController.update(tipoQuartoEntity, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTipoQuarto, response.getBody());
        verify(tipoQuartoService).update(any(TipoQuartoEntity.class), anyLong());
    }

    @Test
    public void testUpdateTipoQuarto_Failure() {
        TipoQuartoEntity tipoQuartoEntity = new TipoQuartoEntity();
        when(tipoQuartoService.update(any(TipoQuartoEntity.class), anyLong())).thenThrow(new RuntimeException());

        ResponseEntity<TipoQuartoEntity> response = tipoQuartoController.update(tipoQuartoEntity, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(tipoQuartoService).update(any(TipoQuartoEntity.class), anyLong());
    }

    @Test
    public void testFindById_Success() {
        TipoQuartoEntity tipoQuartoEntity = new TipoQuartoEntity();
        when(tipoQuartoService.findById(anyLong())).thenReturn(Optional.of(tipoQuartoEntity));

        ResponseEntity<Optional<TipoQuartoEntity>> response = tipoQuartoController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Optional.of(tipoQuartoEntity), response.getBody());
        verify(tipoQuartoService).findById(anyLong());
    }

    @Test
    public void testFindById_Failure() {
        when(tipoQuartoService.findById(anyLong())).thenThrow(new RuntimeException());

        ResponseEntity<Optional<TipoQuartoEntity>> response = tipoQuartoController.findById(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(tipoQuartoService).findById(anyLong());
    }

    @Test
    public void testFindAll_Success() {
        List<TipoQuartoEntity> tipoQuartoEntities = new ArrayList<>();
        tipoQuartoEntities.add(new TipoQuartoEntity());
        when(tipoQuartoService.findAll()).thenReturn(tipoQuartoEntities);

        ResponseEntity<List<TipoQuartoEntity>> response = tipoQuartoController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tipoQuartoEntities, response.getBody());
        verify(tipoQuartoService).findAll();
    }

    @Test
    public void testFindAll_Failure() {
        when(tipoQuartoService.findAll()).thenThrow(new RuntimeException());

        ResponseEntity<List<TipoQuartoEntity>> response = tipoQuartoController.findAll();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(tipoQuartoService).findAll();
    }
}
