package com.nrs1209.travelapp.backpack.entity;

import com.nrs1209.travelapp.place.entity.Place;
import com.nrs1209.travelapp.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "backpack")
public class Backpack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "backpack_id")
    private Long id;

    // 지연 로딩 필수
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Backpack(User user, Place place) {
        this.user = user;
        this.place = place;
        this.createdAt = LocalDateTime.now();
    }
}
