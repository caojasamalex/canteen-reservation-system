package com.djokic.canteenreservationsystem.repository;

import com.djokic.canteenreservationsystem.entity.Reservation;
import com.djokic.canteenreservationsystem.enumeration.ReservationStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByCanteen_IdAndDateAndStatus(Long canteenId, LocalDate date, ReservationStatusEnum status);

    List<Reservation> findAllByStudent_IdAndDateAndStatus(Long studentId, LocalDate date, ReservationStatusEnum status);

    List<Reservation> findAllByCanteen_IdAndStatus(Long canteenId, ReservationStatusEnum status);
}
