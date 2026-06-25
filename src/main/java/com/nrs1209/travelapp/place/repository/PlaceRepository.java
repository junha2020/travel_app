package com.nrs1209.travelapp.place.repository;

import com.nrs1209.travelapp.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByNameContainingIgnoreCase(String name);
}
