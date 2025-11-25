package com.djokic.canteenreservationsystem.service;

import com.djokic.canteenreservationsystem.dto.reservation.CreateReservationRequest;
import com.djokic.canteenreservationsystem.dto.reservation.ReservationResponse;
import com.djokic.canteenreservationsystem.entity.Canteen;
import com.djokic.canteenreservationsystem.entity.Reservation;
import com.djokic.canteenreservationsystem.entity.Student;
import com.djokic.canteenreservationsystem.enumeration.ReservationStatusEnum;
import com.djokic.canteenreservationsystem.repository.CanteenRepository;
import com.djokic.canteenreservationsystem.repository.ReservationRepository;
import com.djokic.canteenreservationsystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StudentRepository studentRepository;
    private final CanteenRepository canteenRepository;

    public ReservationResponse createReservation(CreateReservationRequest createReservationRequest){
        Student student = studentRepository.findById(createReservationRequest.studentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with provided ID not found."));

        Canteen canteen = canteenRepository.findById(createReservationRequest.canteenId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Canteen with provided ID not found."));

        LocalDate date = createReservationRequest.date();
        LocalTime time = createReservationRequest.time();
        int duration = createReservationRequest.duration();

        if(date.isBefore(LocalDate.now())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation date cannot be in the past.");
        }

        if(duration != 30 && duration != 60){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation duration must be either 30 or 60 minutes.");
        }

        int minute = time.getMinute();
        if(minute != 0 && minute != 30){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation time must be either 0 or 30 minutes.");
        }

        boolean insideWorkingHours = canteen.getWorkingHours().stream()
                .anyMatch(wh -> {
                    LocalTime reservationEnd = time.plusMinutes(duration);
                    return !time.isBefore(wh.getFrom()) && !reservationEnd.isAfter(wh.getTo());
                });

        if(!insideWorkingHours){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation time must be inside working hours.");
        }

        List<Reservation> studentReservations = reservationRepository.findAllByStudent_IdAndDateAndStatus(
                student.getId(), date, ReservationStatusEnum.ACTIVE
        );

        boolean overlapsWithStudent = studentReservations.stream()
                .anyMatch(r -> isOverlapping(time, duration, r.getTime(), r.getDuration()));

        if(overlapsWithStudent){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation overlaps with another reservation.");
        }

        List<Reservation> canteenReservations = reservationRepository.findAllByCanteen_IdAndDateAndStatus(
                canteen.getId(), date, ReservationStatusEnum.ACTIVE
        );

        long overlappingForCanteen = canteenReservations.stream()
                .filter(r -> isOverlapping(time, duration, r.getTime(), r.getDuration()))
                .count();

        if(overlappingForCanteen >= canteen.getCapacity()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation capacity reached.");
        }

        Reservation reservation = Reservation.builder()
                .status(ReservationStatusEnum.ACTIVE)
                .student(student)
                .canteen(canteen)
                .date(date)
                .time(time)
                .duration(duration)
                .build();

        Reservation saved = reservationRepository.save(reservation);

        return mapToResponse(saved);
    }

    public ReservationResponse cancelReservation(Long studentId, Long reservationId){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found."));

        if(!reservation.getStudent().getId().equals(studentId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot cancel this reservation.");
        }

        reservation.setStatus(ReservationStatusEnum.CANCELLED);
        Reservation updated = reservationRepository.save(reservation);

        return mapToResponse(updated);
    }

    private ReservationResponse mapToResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getStatus(),
                reservation.getStudent().getId(),
                reservation.getCanteen().getId(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getDuration()
        );
    }

    private boolean isOverlapping(LocalTime start1, int duration1,
                                  LocalTime start2, int duration2) {
        LocalTime end1 = start1.plusMinutes(duration1);
        LocalTime end2 = start2.plusMinutes(duration2);

        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}
