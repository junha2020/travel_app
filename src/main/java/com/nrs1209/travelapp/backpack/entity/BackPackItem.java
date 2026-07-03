package com.nrs1209.travelapp.backpack.entity;

import com.nrs1209.travelapp.plan.entity.Plan;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "backpack_item")
public class BackPackItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "backpack_item_id")
    private Long id;

    // 지연 로딩 필수
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_checked", nullable = false)
    private boolean isChecked;

    // 필수품, 전자기기, 의류 등 분류용 카테고리

}
