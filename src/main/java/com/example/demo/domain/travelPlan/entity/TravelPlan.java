package com.example.demo.domain.travelPlan.entity;

import com.example.demo.domain.placePlan.entitiy.PlanPlace;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "plan")
public class TravelPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate startDate; // 여행 시작일

    @Column(nullable = false)
    private LocalDate endDate; // 여행 종료일

    @Column(nullable = false)
    private Long userId; // 이 계획 생성한 유저 ID

    @Column(nullable = false)
    private boolean isPublic; // 공개 또는 비공개 설정

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성 시간

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt; // 최종 수정 시간

    @OneToMany(mappedBy = "travelPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanPlace> planPlace = new ArrayList<>();

    @Builder
    public TravelPlan(String title, LocalDate startDate, LocalDate endDate, Long userId, boolean isPublic) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.isPublic = isPublic;
    }

    public void update(String title, LocalDate startDate, LocalDate endDate, boolean isPublic) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPublic = isPublic;
    }

    public void addPlanPlace(PlanPlace planPlace) {
        planPlace.add(planPlace);
        planPlace.setTravelPlan(this);
    }

    public void removePlanPlace(PlanPlace planPlace) {
        planPlace.remove(planPlace);
        planPlace.setTravelPlan(null);
    }
}
