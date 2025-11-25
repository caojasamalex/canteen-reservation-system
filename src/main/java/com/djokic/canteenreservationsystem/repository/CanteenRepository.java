package com.djokic.canteenreservationsystem.repository;

import com.djokic.canteenreservationsystem.entity.Canteen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CanteenRepository extends JpaRepository<Canteen, Long> {
}
