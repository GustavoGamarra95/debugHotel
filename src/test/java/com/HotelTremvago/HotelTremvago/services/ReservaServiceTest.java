package com.HotelTremvago.HotelTremvago.services;

import com.HotelTremvago.HotelTremvago.entities.*;
import com.HotelTremvago.HotelTremvago.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservaServiceTest {

    @InjectMocks
    ReservaService reservaService;

    @MockBean
    ReservaRepository reservaRepository;

    @MockBean
    HospedeRepository hospedeRepository;

    @MockBean
    QuartoRepository quartoRepository;

    @MockBean
    TipoQuartoRepository tipoQuartoRepository;


//    @Test
//    void testCalcularDiaria() {
//        ReservaEntity reserva = new ReservaEntity();
//        QuartoEntity quarto = new QuartoEntity();
//        TipoQuartoEntity tipoQuarto = new TipoQuartoEntity();
//
//        quarto.setId(1L);
//        quarto.setTipoQuarto(tipoQuarto);
//        tipoQuarto.setValor(100.0);
//        reserva.setQuarto(quarto);
//
//        LocalDate dataInicio = LocalDate.now();
//        LocalDate dataFinal = dataInicio.plusDays(3);
//        reserva.setDataInicio(Date.from(dataInicio.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        reserva.setDataFinal(Date.from(dataFinal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//
//        Mockito.(quartoRepository.findById(1L)).orElseThrow(Optional.of(quarto));
//
//        Double diaria = reservaService.calcularDiaria(reserva);
//
//        assertEquals(300.0, diaria);
//    }

//    @Test
//    void testSave() {
//        ReservaEntity reserva = new ReservaEntity();
//        QuartoEntity quarto = new QuartoEntity();
//        TipoQuartoEntity tipoQuarto = new TipoQuartoEntity();
//        quarto.setId(1L);
//        quarto.setTipoQuarto(tipoQuarto);
//        tipoQuarto.setValor(100.0);
//        reserva.setQuarto(quarto);
//
//        LocalDate dataInicio = LocalDate.now();
//        LocalDate dataFinal = dataInicio.plusDays(3);
//        reserva.setDataInicio(Date.from(dataInicio.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        reserva.setDataFinal(Date.from(dataFinal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//
//        // Mock para calcularDiaria
//        when(quartoRepository.findById(1L)).thenReturn(Optional.of(quarto));
//
//        // Mock para save
//        when(reservaRepository.save(any(ReservaEntity.class))).thenAnswer(invocation -> {
//            ReservaEntity savedReserva = invocation.getArgument(0);
//            savedReserva.setId(1L); // Simula a atribuição de um ID pelo banco de dados
//            return savedReserva;
//        });
//
//        // Execução
//        ReservaEntity result = reservaService.save(reserva);
//
//        // Verificação
//        assertNotNull(result);
//        assertEquals(300.0, result.getTotal(), 0.01);
//        assertNotNull(result.getId());
//        verify(reservaRepository, times(1)).save(any(ReservaEntity.class));
//    }


    @Test
    void testDelete() {
        Long id = 1L;
        doNothing().when(reservaRepository).deleteById(id);

        String result = reservaService.delete(id);

        assertEquals("Reserva deletado", result);
        verify(reservaRepository, times(1)).deleteById(id);
    }

    @Test
    void testRealizarCheckIn() {
        Long id = 1L;
        ReservaEntity reserva = new ReservaEntity();
        reserva.setStatus(ReservaStatus.RESERVADO);

        when(reservaRepository.findById(id)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(reserva);

        ReservaEntity result = reservaService.realizarCheckIn(id);

        assertEquals(ReservaStatus.OCUPADO, result.getStatus());
        assertNotNull(result.getDataCheckIn());
        verify(reservaRepository, times(1)).save(any(ReservaEntity.class));
    }

    @Test
    void testRealizarCheckOut() {
        Long id = 1L;
        ReservaEntity reserva = new ReservaEntity();
        reserva.setStatus(ReservaStatus.OCUPADO);

        when(reservaRepository.findById(id)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(reserva);

        ReservaEntity result = reservaService.realizarCheckOut(id);

        assertEquals(ReservaStatus.RESERVADO, result.getStatus());
        assertNotNull(result.getDataCheckOut());
        verify(reservaRepository, times(1)).save(any(ReservaEntity.class));
    }

    @Test
    void testAddHospedeToReserva() {
        Long reservaId = 1L;
        Long hospedeId = 1L;
        ReservaEntity reserva = new ReservaEntity();
        reserva.setId(reservaId);
        reserva.setHospedes(new ArrayList<>());

        HospedeEntity hospede = new HospedeEntity();
        hospede.setId(hospedeId);
        hospede.setReservas(new ArrayList<>());

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));
        when(hospedeRepository.findById(hospedeId)).thenReturn(Optional.of(hospede));

        when(reservaRepository.save(any(ReservaEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(hospedeRepository.save(any(HospedeEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        ReservaEntity result = reservaService.addHospedeToReserva(reservaId, hospedeId);

        assertNotNull(result);
        assertTrue(result.getHospedes().contains(hospede));
        assertTrue(hospede.getReservas().contains(reserva));
        assertEquals(1, result.getHospedes().size());
        assertEquals(1, hospede.getReservas().size());

        verify(reservaRepository, times(1)).findById(reservaId);
        verify(hospedeRepository, times(1)).findById(hospedeId);
        verify(reservaRepository, times(1)).save(any(ReservaEntity.class));
        verify(hospedeRepository, times(1)).save(any(HospedeEntity.class));
    }


    @Test
    void testUpdate() {
        Long id = 1L;
        ReservaEntity reserva = new ReservaEntity();
        reserva.setId(id);

        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(reserva);

        ReservaEntity result = reservaService.update(reserva, id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(reservaRepository, times(1)).save(any(ReservaEntity.class));
    }

    @Test
    void testFindById() {
        Long id = 1L;
        ReservaEntity reserva = new ReservaEntity();
        reserva.setId(id);

        when(reservaRepository.findById(id)).thenReturn(Optional.of(reserva));

        ReservaEntity result = reservaService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testFindAll() {
        List<ReservaEntity> reservas = Arrays.asList(new ReservaEntity(), new ReservaEntity());

        when(reservaRepository.findAll()).thenReturn(reservas);

        List<ReservaEntity> result = reservaService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testRemoveHospedeFromReserva() {
        Long reservaId = 1L;
        Long hospedeId = 1L;
        ReservaEntity reserva = new ReservaEntity();
        HospedeEntity hospede = new HospedeEntity();

        List<HospedeEntity> hospedes = new ArrayList<>(Collections.singletonList(hospede));
        List<ReservaEntity> reservas = new ArrayList<>(Collections.singletonList(reserva));

        reserva.setHospedes(hospedes);
        hospede.setReservas(reservas);

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));
        when(hospedeRepository.findById(hospedeId)).thenReturn(Optional.of(hospede));
        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(reserva);

        ReservaEntity result = reservaService.removeHospedeFromReserva(reservaId, hospedeId);

        assertNotNull(result);
        assertFalse(result.getHospedes().contains(hospede));
        verify(reservaRepository, times(1)).save(any(ReservaEntity.class));
        verify(hospedeRepository, times(1)).save(any(HospedeEntity.class));
    }


    @Test
    void testDatasLivres() {
        Long tipoQuartoId = 1L;
        int capacidade = 2;
        int mes = 10;
        int ano = 2024;

        List<QuartoEntity> quartosDisponiveis = new ArrayList<>();
        QuartoEntity quarto = new QuartoEntity();
        quarto.setId(1L);
        quarto.setNome("Quarto Deluxe");
        quarto.setCapacidade(2);
        TipoQuartoEntity tipoQuarto = new TipoQuartoEntity();
        tipoQuarto.setId(1L);
        tipoQuarto.setNome("Tipo A");
        tipoQuarto.setValor(200.0);
        quarto.setTipoQuarto(tipoQuarto);
        List<QuartoEntity> quartos = new ArrayList<>();
        quartos.add(quarto);
        tipoQuarto.setQuartos(quartos);
        quartosDisponiveis.add(quarto);

        LocalDate inicioMes = LocalDate.of(ano, mes, 1);
        LocalDate fimMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

        List<ReservaEntity> reservasFiltradas = new ArrayList<>();
        ReservaEntity reserva1 = new ReservaEntity();
        reserva1.setId(1L);
        reserva1.setDataInicio(Date.from(LocalDate.of(ano, mes, 5).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reserva1.setDataFinal(Date.from(LocalDate.of(ano, mes, 10).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reserva1.setQuarto(quarto);
        reserva1.setStatus(ReservaStatus.valueOf("OCUPADO"));
        List<ReservaEntity> reservas = new ArrayList<>();
        reservas.add(reserva1);
        quarto.setReservas(reservas);
        //reservasFiltradas.add(reserva1);
        Mockito.when(quartoRepository.findByTipoQuartoECapacidade(tipoQuartoId, capacidade)).thenReturn(quartosDisponiveis);

        Mockito.when(this.reservaRepository.findByTipoQuartoCapacidadeStatusData(tipoQuartoId, capacidade,
                java.sql.Date.valueOf(inicioMes), java.sql.Date.valueOf(fimMes))).thenReturn(reservasFiltradas);


        List<Integer> expectedDiasLivres = List.of(1, 2, 3, 4, 11, 12, 13 ,14, 15 ,16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31);
        var dataTest = reservaService.datasLivres(1L, 2, 10, 2024);
        assertEquals(expectedDiasLivres, dataTest);
    }

//    @Test
//    void testDatasLivres() {
//        Long tipoQuartoId = 1L;
//        int capacidade = 2;
//        int mes = 6;
//        int ano = 2023;
//
//        LocalDate inicioMes = LocalDate.of(ano, mes, 1);
//        LocalDate fimMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());
//
//        List<ReservaEntity> reservasFiltradas = new ArrayList<>();
//        ReservaEntity reserva1 = new ReservaEntity();
//        reserva1.setDataInicio(Date.from(LocalDate.of(ano, mes, 5).atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        reserva1.setDataFinal(Date.from(LocalDate.of(ano, mes, 10).atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        QuartoEntity quartoReservado = new QuartoEntity();
//        quartoReservado.setId(1L);
//        reserva1.setQuarto(quartoReservado);
//        reservasFiltradas.add(reserva1);
//
//        List<QuartoEntity> quartosDisponiveis = new ArrayList<>();
//        QuartoEntity quarto = new QuartoEntity();
//        quarto.setId(2L); // Set the ID here
//        quarto.setTipoQuarto(new TipoQuartoEntity());
//        quartosDisponiveis.add(quarto);
//
//        when(reservaRepository.findByTipoQuartoCapacidadeStatusData(eq(tipoQuartoId), eq(capacidade),
//                any(java.sql.Date.class), any(java.sql.Date.class))).thenReturn(reservasFiltradas);
//        when(quartoRepository.findByTipoQuartoECapacidade(tipoQuartoId, capacidade)).thenReturn(quartosDisponiveis);
//
//        List<Integer> result = reservaService.datasLivres(tipoQuartoId, capacidade, mes, ano);
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//        assertTrue(result.contains(1));
//        assertFalse(result.contains(5));
//        assertTrue(result.contains(11));
//    }


    @Test
    void testRealizarCheckInReservaCancelada() {
        Long id = 1L;
        ReservaEntity reserva = new ReservaEntity();
        reserva.setStatus(ReservaStatus.CANCELADO);

        when(reservaRepository.findById(id)).thenReturn(Optional.of(reserva));

        assertThrows(IllegalStateException.class, () -> reservaService.realizarCheckIn(id));
    }

    @Test
    void testRealizarCheckOutReservaNaoOcupada() {
        Long id = 1L;
        ReservaEntity reserva = new ReservaEntity();
        reserva.setStatus(ReservaStatus.RESERVADO);

        when(reservaRepository.findById(id)).thenReturn(Optional.of(reserva));

        assertThrows(IllegalStateException.class, () -> reservaService.realizarCheckOut(id));
    }

    @Test
    void testSaveComExcecao() {
        ReservaEntity reserva = new ReservaEntity();

        when(reservaRepository.save(any(ReservaEntity.class))).thenThrow(new RuntimeException("Erro ao salvar"));

        ReservaEntity result = reservaService.save(reserva);

        assertNotNull(result);
        assertNull(result.getId());
    }


    @Test
    void testDeleteComExcecao() {
        Long id = 1L;

        doThrow(new RuntimeException("Erro ao deletar")).when(reservaRepository).deleteById(id);

        String result = reservaService.delete(id);

        assertEquals("Nao foi possivel deletar reserva", result);
    }




}