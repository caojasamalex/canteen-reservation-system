package com.djokic.canteenreservationsystem.repository;

import com.djokic.canteenreservationsystem.entity.WorkingHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkingHourRepository extends JpaRepository<WorkingHour, Long> {
}
