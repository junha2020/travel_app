package com.example.demo.domain.placePlan.entitiy;

import com.example.demo.domain.place.entity.Place;
import com.example.demo.domain.travelPlan.entity.TravelPlan;
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
    private TravelPlan travelPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(nullable = false)
    private int day;

    private int sequence; // 방문 순서
    private String memo; // 장소에 대한 간단한 메모

    @Builder
    public PlanPlace(TravelPlan travelPlan, Place place, int day) {
        this.travelPlan = travelPlan;
        this.place = place;
        this.day = day;
    }
}
