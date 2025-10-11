package com.example.demo.domain.travelPlan.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "plan")
public class TravelPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id", unique = true, nullable = false)
    private Long id;

    @Column(length = 30, nullable = false)
    private String planName;
}
