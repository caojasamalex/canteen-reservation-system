package com.djokic.canteenreservationsystem.controller;

import com.djokic.canteenreservationsystem.dto.reservation.CreateReservationRequest;
import com.djokic.canteenreservationsystem.dto.reservation.ReservationResponse;
import com.djokic.canteenreservationsystem.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody CreateReservationRequest createReservationRequest){
        ReservationResponse reservationResponse = reservationService.createReservation(createReservationRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationResponse> cancelReservation(
            @RequestHeader("studentId") Long studentId,
            @PathVariable Long id){
        ReservationResponse reservationResponse = reservationService.cancelReservation(studentId, id);
        return ResponseEntity.ok(reservationResponse);
    }
}
