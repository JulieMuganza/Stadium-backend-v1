package com.amahoro.amahoro_stadium_ticketing.repository;


import com.amahoro.amahoro_stadium_ticketing.model.AmahoroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmahoroRepository extends JpaRepository<AmahoroEntity, Long> {

    // Method to find by clientName or email
    List<AmahoroEntity> findByClientNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String clientName, String email);
    List<AmahoroEntity> findByClientNameContainingIgnoreCase(String clientName);


}
