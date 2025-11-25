package com.djokic.canteenreservationsystem.service;

import com.djokic.canteenreservationsystem.dto.canteen.CanteenResponse;
import com.djokic.canteenreservationsystem.dto.canteen.CreateCanteenRequest;
import com.djokic.canteenreservationsystem.dto.canteen.UpdateCanteenRequest;
import com.djokic.canteenreservationsystem.dto.canteen.WorkingHourResponse;
import com.djokic.canteenreservationsystem.entity.Canteen;
import com.djokic.canteenreservationsystem.entity.WorkingHour;
import com.djokic.canteenreservationsystem.repository.CanteenRepository;
import com.djokic.canteenreservationsystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CanteenService {
    private final CanteenRepository canteenRepository;
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
