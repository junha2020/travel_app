package com.nrs1209.travelapp.backpack.repository;

import com.nrs1209.travelapp.backpack.entity.Backpack;
import com.nrs1209.travelapp.place.entity.Place;
import com.nrs1209.travelapp.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BackpackRepository extends JpaRepository<Backpack, Long> {
    List<Backpack> findByUserId(Long userId);
    Optional<Backpack> findByUserAndPlace(User user, Place place);
    boolean existsByUserAndPlace(User user, Place place);
}
