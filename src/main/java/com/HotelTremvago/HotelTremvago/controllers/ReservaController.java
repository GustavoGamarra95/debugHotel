package com.HotelTremvago.HotelTremvago.controllers;

import com.HotelTremvago.HotelTremvago.entities.QuartoEntity;
import com.HotelTremvago.HotelTremvago.entities.ReservaEntity;
import com.HotelTremvago.HotelTremvago.entities.ReservaStatus;
import com.HotelTremvago.HotelTremvago.services.QuartoService;
import com.HotelTremvago.HotelTremvago.services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reserva")
public class ReservaController {
    @Autowired
    private ReservaService reservaService;
    @Autowired
    private QuartoService quartoService;

    @PostMapping("/criarReserva/{tipoQuartoId}/{capacidade}")
    public ResponseEntity<ReservaEntity> save(@PathVariable long tipoQuartoId,
                                              @PathVariable int capacidade,
                                              @RequestBody ReservaEntity reservaEntity) {
        try {
            List<QuartoEntity> quartosDisponiveis = quartoService.quartosDisponiveis(
                    tipoQuartoId,
                    capacidade,
                    reservaEntity.getDataInicio(),
                    reservaEntity.getDataFinal());

            if (quartosDisponiveis.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            QuartoEntity quartoEntity = quartosDisponiveis.get(0);
            reservaEntity.setQuarto(quartoEntity);

            ReservaEntity reserva = reservaService.save(reservaEntity);
            return new ResponseEntity<>(reserva, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/consultarReserva/{tipoQuartoId}/{capacidade}/{mes}/{ano}")
    public ResponseEntity<List<Integer>> listaDiasDisponiveisPorMes(
            @PathVariable Long tipoQuartoId,
            @PathVariable int capacidade,
            @PathVariable int mes,
            @PathVariable int ano) {
        try {
            List<Integer> datasDisponiveis = reservaService.datasLivres(tipoQuartoId, capacidade, mes, ano);
            return new ResponseEntity<>(datasDisponiveis, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/cancela/{id}")
    public ResponseEntity<ReservaEntity> cancela(@PathVariable Long id) {
        try {
            ReservaEntity reserva = reservaService.findById(id);
            if (reserva != null) {
                reserva.setStatus(ReservaStatus.CANCELADO);
                reservaService.update(reserva, id);
                return new ResponseEntity<>(reserva, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
