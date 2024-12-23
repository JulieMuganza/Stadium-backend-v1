package com.amahoro.amahoro_stadium_ticketing.service;

import com.amahoro.amahoro_stadium_ticketing.model.AmahoroEntity;
import com.amahoro.amahoro_stadium_ticketing.repository.AmahoroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AmahoroService {


    @Autowired
    AmahoroRepository arenaRepository;

    public void save(AmahoroEntity booking) {
        arenaRepository.save(booking);
    }

    public List<AmahoroEntity> listAll() {
        return arenaRepository.findAll();
    }


    public Optional<AmahoroEntity> findClientById(Long id) {
        return arenaRepository.findById(id);
    }

    public List<AmahoroEntity> searchBookings(String keyword) {
        return arenaRepository.findByClientNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
    }

    public List<AmahoroEntity> findByClientName(String clientName) {
        return arenaRepository.findByClientNameContainingIgnoreCase(clientName);
    }

    // Method to get clients sorted by client name in ascending order
    public List<AmahoroEntity> listAllSortedByName() {
        return arenaRepository.findAll(Sort.by(Sort.Direction.ASC, "clientName"));
    }

    // Method to retrieve booking by ID
    public Optional<AmahoroEntity> getBookingById(Long id) {
        return arenaRepository.findById(id);
    }

}
