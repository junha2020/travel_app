package com.nrs1209.travelapp.domain.backpack.repository;

import com.nrs1209.travelapp.domain.backpack.entity.Backpack;
import com.nrs1209.travelapp.domain.place.entity.Place;
import com.nrs1209.travelapp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BackpackRepository extends JpaRepository<Backpack, Long> {
    List<Backpack> findByUserId(Long userId);
    Optional<Backpack> findByUserAndPlace(User user, Place place);
    boolean existsByUserAndPlace(User user, Place place);
}
