package com.HotelTremvago.HotelTremvago.services;

import com.HotelTremvago.HotelTremvago.entities.CidadeEntity;
import com.HotelTremvago.HotelTremvago.entities.HotelEntity;
import com.HotelTremvago.HotelTremvago.repositories.CidadeRepository;
import com.HotelTremvago.HotelTremvago.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class HotelService {
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private CidadeRepository cidadeRepository;

    public HotelEntity save(HotelEntity hotelEntity){
        try{
            return hotelRepository.save(hotelEntity);
        } catch(Exception e){
            System.out.println("Nao foi possivel salvar hotel: " + e.getMessage());
            return new HotelEntity();
        }
    }

    public String delete(Long id){
        try {
            hotelRepository.deleteById(id);
            return "Hotel deletado";
        } catch (Exception e){
            return "Nao foi possivel deletar hotel";
        }
    }

    public List<HotelEntity> saveAll(List<HotelEntity> hotelEntities) {
        return hotelRepository.saveAll(hotelEntities);
    }

    public HotelEntity update(HotelEntity updatedHotel, Long id) {
        HotelEntity hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id " + id));

        hotel.setNomeFantasia(updatedHotel.getNomeFantasia());
        hotel.setNomeJuridico(updatedHotel.getNomeJuridico());
        hotel.setCnpj(updatedHotel.getCnpj());
        hotel.setCep(updatedHotel.getCep());
        hotel.setEmail(updatedHotel.getEmail());
        hotel.setTelefone(updatedHotel.getTelefone());

        if (updatedHotel.getCidade() != null && updatedHotel.getCidade().getId() != null) {
            CidadeEntity cidade = cidadeRepository.findById(updatedHotel.getCidade().getId())
                    .orElseThrow(() -> new RuntimeException("Cidade not found with id " + updatedHotel.getCidade().getId()));
            hotel.setCidade(cidade);
        }

        return hotelRepository.save(hotel);
    }

    public HotelEntity findById(Long id){
        try{
            return hotelRepository.findById(id).orElseThrow();
        }catch(Exception e){
            System.out.println("Nao foi possivel encontrar um hotel com este ID: " + e.getMessage());
            return new HotelEntity();
        }
    }

    public List<HotelEntity> findAll() {
        try{
            return hotelRepository.findAll();
        } catch(Exception e) {
            System.out.println("Erro ao encontrar lista de hotel: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
