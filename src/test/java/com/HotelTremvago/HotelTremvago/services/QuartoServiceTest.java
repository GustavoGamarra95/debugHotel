package com.HotelTremvago.HotelTremvago.services;

import com.HotelTremvago.HotelTremvago.entities.QuartoEntity;
import com.HotelTremvago.HotelTremvago.entities.TipoQuartoEntity;
import com.HotelTremvago.HotelTremvago.entities.ReservaEntity;
import com.HotelTremvago.HotelTremvago.repositories.QuartoRepository;
import com.HotelTremvago.HotelTremvago.repositories.TipoQuartoRepository;
import com.HotelTremvago.HotelTremvago.repositories.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class QuartoServiceTest {

    @Mock
    private QuartoRepository quartoRepository;

    @Mock
    private TipoQuartoRepository tipoQuartoRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private QuartoService quartoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCriarQuartoHandlesException() {
        QuartoEntity quarto = new QuartoEntity();
        TipoQuartoEntity tipoQuarto = new TipoQuartoEntity();
        quarto.setTipoQuarto(tipoQuarto);

        when(tipoQuartoRepository.findByNome(anyString())).thenThrow(new RuntimeException("Erro no banco de dados"));

        QuartoEntity resultado = quartoService.criarQuarto(quarto);

        assertNull(resultado, "O resultado deve ser nulo em caso de erro");
        verify(tipoQuartoRepository).findByNome(tipoQuarto.getNome());
    }

    @Test
    public void testQuartosDisponiveisComDatasIncorretas() {
        Long tipoQuartoId = 1L;
        int capacidade = 2;
        Date dataInicio = Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dataFinal = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<QuartoEntity> quartos = Collections.singletonList(new QuartoEntity());
        when(quartoRepository.findByTipoQuartoECapacidade(tipoQuartoId, capacidade)).thenReturn(quartos);
        when(reservaRepository.findByTipoQuartoCapacidadeStatusData(anyLong(), anyInt(), any(), any())).thenReturn(Collections.emptyList());

        List<QuartoEntity> resultado = quartoService.quartosDisponiveis(tipoQuartoId, capacidade, dataInicio, dataFinal);

        assertFalse(resultado.isEmpty(), "A lista de quartos disponíveis não deve estar vazia");
        verify(quartoRepository).findByTipoQuartoECapacidade(tipoQuartoId, capacidade);
        verify(reservaRepository).findByTipoQuartoCapacidadeStatusData(tipoQuartoId, capacidade, dataInicio, dataFinal);
    }

    @Test
    public void testQuartosDisponiveisComReservasMultiplicadas() {
        Long tipoQuartoId = 1L;
        int capacidade = 2;
        Date dataInicio = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dataFinal = Date.from(LocalDate.now().plusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());

        QuartoEntity quarto = new QuartoEntity();
        quarto.setId(1L);

        ReservaEntity reserva1 = new ReservaEntity();
        reserva1.setQuarto(quarto);
        reserva1.setDataInicio(dataInicio);
        reserva1.setDataFinal(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        ReservaEntity reserva2 = new ReservaEntity();
        reserva2.setQuarto(quarto);
        reserva2.setDataInicio(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reserva2.setDataFinal(dataFinal);

        List<QuartoEntity> quartos = Collections.singletonList(quarto);
        List<ReservaEntity> reservas = Arrays.asList(reserva1, reserva2);

        when(quartoRepository.findByTipoQuartoECapacidade(tipoQuartoId, capacidade)).thenReturn(quartos);
        when(reservaRepository.findByTipoQuartoCapacidadeStatusData(tipoQuartoId, capacidade, dataInicio, dataFinal)).thenReturn(reservas);

        List<QuartoEntity> resultado = quartoService.quartosDisponiveis(tipoQuartoId, capacidade, dataInicio, dataFinal);

        assertTrue(resultado.isEmpty(), "A lista de quartos disponíveis deve estar vazia quando todos estão ocupados");
        verify(quartoRepository).findByTipoQuartoECapacidade(tipoQuartoId, capacidade);
        verify(reservaRepository).findByTipoQuartoCapacidadeStatusData(tipoQuartoId, capacidade, dataInicio, dataFinal);
    }

    @Test
    public void testUpdateHandlesException() {
        QuartoEntity quartoAtualizado = new QuartoEntity();
        quartoAtualizado.setTipoQuarto(new TipoQuartoEntity());

        when(quartoRepository.findById(anyLong())).thenThrow(new RuntimeException("Quarto não encontrado com o ID 1"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            quartoService.update(quartoAtualizado, 1L);
        });

        assertEquals("Quarto não encontrado com o ID 1", exception.getMessage());
        verify(quartoRepository).findById(1L);
    }

    @Test
    public void testFindAllHandlesException() {
        when(quartoRepository.findAll()).thenThrow(new RuntimeException("Erro no banco de dados"));

        List<QuartoEntity> resultado = quartoService.findAll();

        assertTrue(resultado.isEmpty(), "A lista de quartos deve estar vazia em caso de erro");
        verify(quartoRepository).findAll();
    }
}
