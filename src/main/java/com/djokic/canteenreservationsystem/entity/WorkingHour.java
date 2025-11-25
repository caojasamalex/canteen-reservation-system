package com.djokic.canteenreservationsystem.entity;

import com.djokic.canteenreservationsystem.enumeration.MealTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkingHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MealTypeEnum meal;

    @Column(name = "from_time", nullable = false)
    private LocalTime from;

    @Column(name = "to_time", nullable = false)
    private LocalTime to;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canteen_id", nullable = false)
    private Canteen canteen;
}
