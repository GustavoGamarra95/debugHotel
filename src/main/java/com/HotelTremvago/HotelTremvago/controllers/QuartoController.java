package com.HotelTremvago.HotelTremvago.controllers;

import com.HotelTremvago.HotelTremvago.entities.QuartoEntity;
import com.HotelTremvago.HotelTremvago.entities.TipoQuartoEntity;
import com.HotelTremvago.HotelTremvago.services.QuartoService;
import com.HotelTremvago.HotelTremvago.services.TipoQuartoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/quarto")
public class QuartoController {
    @Autowired
    private QuartoService quartoService;
    @Autowired
    private TipoQuartoService tipoQuartoService;

    @PostMapping("/criarQuarto")
    public ResponseEntity<QuartoEntity> criarQuarto(@RequestBody QuartoEntity quartoEntity) {
        try {
            // Primeiro, criar o novo quarto
            QuartoEntity novoQuarto = quartoService.criarQuarto(quartoEntity);
            Long tipoQuartoId = novoQuarto.getTipoQuarto().getId();

            // Verificar se o tipo de quarto existe
            Optional<TipoQuartoEntity> tipoQuartoOpt = tipoQuartoService.findById(tipoQuartoId);

            // Se o tipo de quarto existir, definir e retornar o novo quarto
            if (tipoQuartoOpt.isPresent()) {
                novoQuarto.setTipoQuarto(tipoQuartoOpt.get());
                return new ResponseEntity<>(novoQuarto, HttpStatus.OK);
            } else {
                // Se o tipo de quarto não existir, retornar BAD_REQUEST
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            // Tratar exceções e retornar BAD_REQUEST
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            String quarto = quartoService.delete(id);
            return new ResponseEntity<>(quarto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<QuartoEntity> update(@RequestBody QuartoEntity quartoEntity, @PathVariable Long id) {
        try {
            QuartoEntity quarto = quartoService.update(quartoEntity, id);
            return new ResponseEntity<>(quarto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<QuartoEntity> findById(@PathVariable Long id) {
        try {
            QuartoEntity quarto = quartoService.findById(id);
            return new ResponseEntity<>(quarto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<QuartoEntity>> findAll() {
        try {
            List<QuartoEntity> quarto = quartoService.findAll();
            return new ResponseEntity<>(quarto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
