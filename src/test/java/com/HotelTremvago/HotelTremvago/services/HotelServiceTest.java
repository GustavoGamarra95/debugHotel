package com.HotelTremvago.HotelTremvago.services;
import com.HotelTremvago.HotelTremvago.entities.CidadeEntity;
import com.HotelTremvago.HotelTremvago.entities.HotelEntity;
import com.HotelTremvago.HotelTremvago.repositories.CidadeRepository;
import com.HotelTremvago.HotelTremvago.repositories.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private CidadeRepository cidadeRepository;

    @InjectMocks
    private HotelService hotelService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        HotelEntity hotel = new HotelEntity();
        when(hotelRepository.save(any(HotelEntity.class))).thenReturn(hotel);

        HotelEntity result = hotelService.save(hotel);

        assertNotNull(result);
        verify(hotelRepository).save(hotel);
    }

    @Test
    public void testDelete() {
        doNothing().when(hotelRepository).deleteById(anyLong());

        String result = hotelService.delete(1L);

        assertEquals("Hotel deletado", result);
        verify(hotelRepository).deleteById(1L);
    }

    @Test
    public void testSaveAll() {
        List<HotelEntity> hotels = List.of(new HotelEntity(), new HotelEntity());
        when(hotelRepository.saveAll(any(List.class))).thenReturn(hotels);

        List<HotelEntity> result = hotelService.saveAll(hotels);

        assertEquals(hotels.size(), result.size());
        verify(hotelRepository).saveAll(hotels);
    }

    @Test
    public void testUpdate() {
        // Configuración del hotel existente
        HotelEntity existingHotel = new HotelEntity();
        existingHotel.setId(1L);


        CidadeEntity cidade = new CidadeEntity();
        cidade.setId(2L);
        HotelEntity updatedHotel = new HotelEntity();
        updatedHotel.setCidade(cidade);


        when(hotelRepository.findById(anyLong())).thenReturn(Optional.of(existingHotel));
        when(cidadeRepository.findById(anyLong())).thenReturn(Optional.of(cidade));
        when(hotelRepository.save(any(HotelEntity.class))).thenReturn(existingHotel);


        HotelEntity result = hotelService.update(updatedHotel, 1L);


        assertNotNull(result);
        verify(hotelRepository).findById(1L);
        verify(cidadeRepository).findById(2L);
        verify(hotelRepository).save(existingHotel);
    }


    @Test
    public void testFindById() {
        HotelEntity hotel = new HotelEntity();
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.of(hotel));

        HotelEntity result = hotelService.findById(1L);

        assertNotNull(result);
        verify(hotelRepository).findById(1L);
    }

    @Test
    public void testFindAll() {
        List<HotelEntity> hotels = List.of(new HotelEntity(), new HotelEntity());
        when(hotelRepository.findAll()).thenReturn(hotels);

        List<HotelEntity> result = hotelService.findAll();

        assertEquals(hotels.size(), result.size());
        verify(hotelRepository).findAll();
    }
}
