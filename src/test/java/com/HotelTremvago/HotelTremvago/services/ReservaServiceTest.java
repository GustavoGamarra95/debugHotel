package com.HotelTremvago.HotelTremvago.services;

import com.HotelTremvago.HotelTremvago.entities.*;
import com.HotelTremvago.HotelTremvago.repositories.HospedeRepository;
import com.HotelTremvago.HotelTremvago.repositories.QuartoRepository;
import com.HotelTremvago.HotelTremvago.repositories.ReservaRepository;
import com.HotelTremvago.HotelTremvago.repositories.TipoQuartoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @InjectMocks
    private ReservaService reservaService;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private HospedeRepository hospedeRepository;

    @Mock
    private TipoQuartoRepository tipoQuartoRepository;

    @Mock
    private QuartoRepository quartoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalcularDiaria() {
        QuartoEntity quarto = new QuartoEntity();
        TipoQuartoEntity tipoQuarto = new TipoQuartoEntity();
        tipoQuarto.setValor(100.0);
        quarto.setTipoQuarto(tipoQuarto);

        ReservaEntity reserva = new ReservaEntity();
        reserva.setQuarto(quarto);
        reserva.setDataInicio(Date.from(LocalDate.of(2024, 9, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reserva.setDataFinal(Date.from(LocalDate.of(2024, 9, 5).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        double diaria = reservaService.calcularDiaria(reserva);

        assertEquals(400.0, diaria);
    }

    @Test
    void testDatasLivres() {
        Long tipoQuartoId = 1L;
        int capacidade = 2;
        int mes = 10;
        int ano = 2024;

        QuartoEntity quarto = new QuartoEntity();
        quarto.setId(1L);
        quarto.setCapacidade(capacidade);
        quarto.setNome("Quarto Deluxe");
        TipoQuartoEntity tipoQuarto = new TipoQuartoEntity();
        tipoQuarto.setId(tipoQuartoId);
        tipoQuarto.setNome("Tipo A");
        tipoQuarto.setValor(200.0);
        quarto.setTipoQuarto(tipoQuarto);

        List<QuartoEntity> quartosDisponiveis = Collections.singletonList(quarto);
        when(quartoRepository.findByTipoQuartoECapacidade(tipoQuartoId, capacidade)).thenReturn(quartosDisponiveis);

        LocalDate inicioReserva = LocalDate.of(ano, mes, 5);
        LocalDate fimReserva = LocalDate.of(ano, mes, 10);
        ReservaEntity reserva = new ReservaEntity();
        reserva.setId(1L);
        reserva.setQuarto(quarto);
        reserva.setDataInicio(Date.from(inicioReserva.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reserva.setDataFinal(Date.from(fimReserva.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reserva.setStatus(ReservaStatus.OCUPADO);

        List<ReservaEntity> reservasFiltradas = Collections.singletonList(reserva);
        when(reservaRepository.findByTipoQuartoCapacidadeStatusData(
                eq(tipoQuartoId),
                eq(capacidade),
                any(java.sql.Date.class),
                any(java.sql.Date.class)))
                .thenReturn(reservasFiltradas);

        List<Integer> expectedDiasLivres = List.of(1, 2, 3, 4, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31);

        List<Integer> datasLivres = reservaService.datasLivres(tipoQuartoId, capacidade, mes, ano);

        assertNotNull(datasLivres);
        assertEquals(expectedDiasLivres, datasLivres);
        assertTrue(quartosDisponiveis.stream()
                .anyMatch(q -> "Tipo A".equals(q.getTipoQuarto().getNome())));
    }

    @Test
    void testSave() {
        ReservaEntity reserva = new ReservaEntity();
        reserva.setId(1L);
        reserva.setTotal(200.0);

        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(reserva);

        ReservaEntity savedReserva = reservaService.save(reserva);

        assertNotNull(savedReserva);
        assertEquals(1L, savedReserva.getId());
    }

    @Test
    void testDelete() {
        Long reservaId = 1L;

        doNothing().when(reservaRepository).deleteById(reservaId);

        String result = reservaService.delete(reservaId);

        assertEquals("Reserva deletado", result);
    }

    @Test
    void testUpdate() {
        ReservaEntity reserva = new ReservaEntity();
        reserva.setId(1L);

        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(reserva);

        ReservaEntity updatedReserva = reservaService.update(reserva, 1L);

        assertNotNull(updatedReserva);
        assertEquals(1L, updatedReserva.getId());
    }

    @Test
    void testFindById() {
        Long reservaId = 1L;
        ReservaEntity reserva = new ReservaEntity();
        reserva.setId(reservaId);

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));

        ReservaEntity foundReserva = reservaService.findById(reservaId);

        assertNotNull(foundReserva);
        assertEquals(reservaId, foundReserva.getId());
    }

    @Test
    void testFindAll() {
        List<ReservaEntity> reservas = Arrays.asList(new ReservaEntity(), new ReservaEntity());

        when(reservaRepository.findAll()).thenReturn(reservas);

        List<ReservaEntity> allReservas = reservaService.findAll();

        assertNotNull(allReservas);
        assertEquals(2, allReservas.size());
    }

    @Test
    void testAddHospedeToReserva() {
        Long reservaId = 1L;
        Long hospedeId = 2L;

        ReservaEntity reserva = new ReservaEntity();
        HospedeEntity hospede = new HospedeEntity();

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));
        when(hospedeRepository.findById(hospedeId)).thenReturn(Optional.of(hospede));
        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(reserva);
        when(hospedeRepository.save(any(HospedeEntity.class))).thenReturn(hospede);

        ReservaEntity updatedReserva = reservaService.addHospedeToReserva(reservaId, hospedeId);

        assertNotNull(updatedReserva);
    }

    @Test
    void testRemoveHospedeFromReserva() {
        Long reservaId = 1L;
        Long hospedeId = 2L;

        ReservaEntity reserva = new ReservaEntity();
        HospedeEntity hospede = new HospedeEntity();

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));
        when(hospedeRepository.findById(hospedeId)).thenReturn(Optional.of(hospede));
        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(reserva);
        when(hospedeRepository.save(any(HospedeEntity.class))).thenReturn(hospede);

        ReservaEntity updatedReserva = reservaService.removeHospedeFromReserva(reservaId, hospedeId);

        assertNotNull(updatedReserva);
    }

    @Test
    void testRealizarCheckIn() {
        Long reservaId = 1L;
        ReservaEntity reserva = new ReservaEntity();
        reserva.setStatus(ReservaStatus.RESERVADO);

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(reserva);

        ReservaEntity updatedReserva = reservaService.realizarCheckIn(reservaId);

        assertNotNull(updatedReserva);
        assertEquals(ReservaStatus.OCUPADO, updatedReserva.getStatus());
    }

    @Test
    void testRealizarCheckOut() {
        Long reservaId = 1L;
        ReservaEntity reserva = new ReservaEntity();
        reserva.setStatus(ReservaStatus.OCUPADO);

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(reserva);

        ReservaEntity updatedReserva = reservaService.realizarCheckOut(reservaId);

        assertNotNull(updatedReserva);
        assertEquals(ReservaStatus.OCUPADO, updatedReserva.getStatus());
    }
}
