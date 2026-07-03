package com.nrs1209.travelapp.plan.entity;

import com.nrs1209.travelapp.place.entity.Place;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "plan_place")
public class PlanPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(nullable = false)
    private int day;

    private int sequence; // 방문 순서
    private String memo; // 장소에 대한 간단한 메모

    @Builder
    public PlanPlace(Plan plan, Place place, int day) {
        this.plan = plan;
        this.place = place;
        this.day = day;
    }
}
