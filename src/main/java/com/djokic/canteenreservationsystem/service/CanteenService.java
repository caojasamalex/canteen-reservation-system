package com.djokic.canteenreservationsystem.service;

import com.djokic.canteenreservationsystem.dto.canteen.CanteenResponse;
import com.djokic.canteenreservationsystem.dto.canteen.CreateCanteenRequest;
import com.djokic.canteenreservationsystem.dto.canteen.UpdateCanteenRequest;
import com.djokic.canteenreservationsystem.dto.canteen.WorkingHourResponse;
import com.djokic.canteenreservationsystem.dto.status.CanteenStatusResponse;
import com.djokic.canteenreservationsystem.dto.status.SlotStatusResponse;
import com.djokic.canteenreservationsystem.entity.Canteen;
import com.djokic.canteenreservationsystem.entity.Reservation;
import com.djokic.canteenreservationsystem.entity.WorkingHour;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CanteenService {
    private final CanteenRepository canteenRepository;
    private final ReservationRepository reservationRepository;
    private final StudentRepository studentRepository;

    public CanteenResponse createCanteen(Long studentId, CreateCanteenRequest createCanteenRequest){
        validateAdminOrThrow(studentId);

        Canteen canteen = new Canteen();
        canteen.setName(createCanteenRequest.name());
        canteen.setLocation(createCanteenRequest.location());
        canteen.setCapacity(createCanteenRequest.capacity());

        List<WorkingHour> workingHours = createCanteenRequest.workingHours()
                .stream()
                .map(dto -> {
                    WorkingHour wh = new WorkingHour();
                    wh.setMeal(dto.meal());
                    wh.setFrom(dto.from());
                    wh.setTo(dto.to());
                    wh.setCanteen(canteen);
                    return wh;
                }).collect(Collectors.toList());

        canteen.setWorkingHours(workingHours);

        Canteen saved = canteenRepository.save(canteen);

        return mapToResponse(saved);
    }

    public List<CanteenResponse> getAllCanteens(){
        return canteenRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CanteenResponse getById(Long id){
        return canteenRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Canteen not found."));
    }

    public CanteenResponse updateCanteen(Long studentId, Long id, UpdateCanteenRequest updateCanteenRequest){
        validateAdminOrThrow(studentId);

        Canteen canteen = canteenRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Canteen not found."));

        if(updateCanteenRequest.name() != null){canteen.setName(updateCanteenRequest.name());}
        if(updateCanteenRequest.location() != null){canteen.setLocation(updateCanteenRequest.location());}
        if(updateCanteenRequest.capacity() != null){canteen.setCapacity(updateCanteenRequest.capacity());}

        Canteen updated = canteenRepository.save(canteen);

        return mapToResponse(updated);
    }

    public void deleteCanteen(Long studentId, Long id){
        validateAdminOrThrow(studentId);

        if(!canteenRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Canteen not found.");
        }

        canteenRepository.deleteById(id);
    }

    public List<CanteenStatusResponse> getStatusForAll(LocalDate startDate,
                                                       LocalDate endDate,
                                                       LocalTime startTime,
                                                       LocalTime endTime,
                                                       int duration){
        validateStatusRequest(startDate,endDate,startTime,endTime, duration);
        
        return canteenRepository.findAll()
                .stream()
                .map(c -> computeStatusForCanteen(c.getId(), startDate, endDate, startTime, endTime, duration))
                .collect(Collectors.toList());
    }

    public CanteenStatusResponse getStatusForCanteen(Long canteenId,
                                                     LocalDate startDate,
                                                     LocalDate endDate,
                                                     LocalTime startTime,
                                                     LocalTime endTime,
                                                     int duration){
        validateStatusRequest(startDate, endDate, startTime, endTime, duration);

        return computeStatusForCanteen(canteenId, startDate, endDate, startTime, endTime, duration);
    }

    private CanteenStatusResponse computeStatusForCanteen(Long canteenId,
                                           LocalDate startDate,
                                           LocalDate endDate,
                                           LocalTime startTime,
                                           LocalTime endTime,
                                           int duration) {

        Canteen canteen = canteenRepository.findById(canteenId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Canteen not found."));

        List<SlotStatusResponse> slots = new ArrayList<>();

        LocalDate currentDate = startDate;

        while(!currentDate.isAfter(endDate)){
            List<Reservation> reservations = reservationRepository.findAllByCanteen_IdAndDateAndStatus(
                    canteenId,
                    currentDate,
                    ReservationStatusEnum.ACTIVE
            );

            for(WorkingHour wh : canteen.getWorkingHours()){
                LocalTime slotStart = maxTime(startTime, wh.getFrom());
                LocalTime slotEndLimit = minTime(endTime, wh.getTo());

                while(!slotStart.plusMinutes(duration).isAfter(slotEndLimit)){
                    LocalTime slotEnd = slotStart.plusMinutes(duration);

                    LocalTime finalSlotStart = slotStart;
                    long overlapping = reservations.stream()
                            .filter(r -> isOverlapping(finalSlotStart, duration, r.getTime(), r.getDuration()))
                            .count();

                    int remaining = canteen.getCapacity() - (int) overlapping;

                    if(remaining > 0){
                        slots.add(new SlotStatusResponse(
                                currentDate,
                                wh.getMeal(),
                                slotStart,
                                remaining
                        ));
                    }

                    slotStart = slotStart.plusMinutes(duration);
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        return new CanteenStatusResponse(canteenId, slots);
    }

    private void validateStatusRequest(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int duration) {
        if(endDate.isBefore(startDate)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date cannot be before start date.");
        }

        if(endTime.isBefore(startTime)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End time cannot be before start time.");
        }

        if(duration != 30 && duration != 60) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation duration must be either 30 or 60 minutes.");
        }
    }

    private boolean isOverlapping(LocalTime start1, int duration1,
                                  LocalTime start2, int duration2) {
        LocalTime end1 = start1.plusMinutes(duration1);
        LocalTime end2 = start2.plusMinutes(duration2);

        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private LocalTime maxTime(LocalTime a, LocalTime b) {
        return a.isAfter(b) ? a : b;
    }

    private LocalTime minTime(LocalTime a, LocalTime b) {
        return a.isBefore(b) ? a : b;
    }

    private CanteenResponse mapToResponse(Canteen canteen) {
        return new CanteenResponse(
                canteen.getId(),
                canteen.getName(),
                canteen.getLocation(),
                canteen.getCapacity(),
                canteen.getWorkingHours().stream().map(
                        wh -> new WorkingHourResponse(
                                wh.getMeal(),
                                wh.getFrom(),
                                wh.getTo()
                        )
                ).collect(Collectors.toList())
        );
    }

    private void validateAdminOrThrow(Long studentId) {
        if(!studentRepository.existsById(studentId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found.");
        }

        if(!studentRepository.findById(studentId).get().isAdmin()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admin can create canteen.");
        }
    }
}
