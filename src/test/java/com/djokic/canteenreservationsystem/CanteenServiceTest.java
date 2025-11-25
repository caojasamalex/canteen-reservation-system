package com.djokic.canteenreservationsystem;

import com.djokic.canteenreservationsystem.dto.canteen.CanteenResponse;
import com.djokic.canteenreservationsystem.dto.canteen.CreateCanteenRequest;
import com.djokic.canteenreservationsystem.entity.Canteen;
import com.djokic.canteenreservationsystem.entity.Student;
import com.djokic.canteenreservationsystem.repository.CanteenRepository;
import com.djokic.canteenreservationsystem.repository.ReservationRepository;
import com.djokic.canteenreservationsystem.repository.StudentRepository;
import com.djokic.canteenreservationsystem.service.CanteenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
public class CanteenServiceTest {

    @Mock
    private CanteenRepository canteenRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private CanteenService canteenService;

    @Test
    void createCanteenShouldThrowWhenStudentIsNotAdmin(){
        Student student = Student
                .builder()
                .id(1L)
                .name("Petar Petrovic")
                .email("petar.petrovic@etf.rs")
                .isAdmin(false)
                .build();

        when(studentRepository.existsById(1L)).thenReturn(true);
        when(studentRepository.findById(1L)).thenReturn(java.util.Optional.of(student));

        CreateCanteenRequest request = new CreateCanteenRequest(
                "Menza Test 1",
                "Lokacija Test 1",
                50,
                Collections.emptyList()
        );

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> canteenService.createCanteen(1L, request));

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }

    @Test
    void createCanteenShouldThrowWhenStudentDoesNotExist(){
        when(studentRepository.existsById(1L)).thenReturn(false);

        CreateCanteenRequest request = new CreateCanteenRequest(
                "Menza Test 1",
                "Lokacija Test 1",
                50,
                Collections.emptyList()
        );

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> canteenService.createCanteen(1L, request));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void createCanteenReturnsCanteenResponse(){
        Student s = Student
                .builder()
                .id(1L)
                .name("Petar Petrovic")
                .email("petar.petrovic@ftn.rs")
                .isAdmin(true)
                .build();

        when(studentRepository.existsById(1L)).thenReturn(true);
        when(studentRepository.findById(1L)).thenReturn(java.util.Optional.of(s));

        CreateCanteenRequest request = new CreateCanteenRequest(
                "Menza Test 1",
                "Lokacija Test 1",
                50,
                Collections.emptyList()
        );

        Canteen canteen = Canteen
                .builder()
                .id(1L)
                .name("Menza Test 1")
                .location("Lokacija Test 1")
                .capacity(50)
                .workingHours(Collections.emptyList())
                .build();


        when(canteenRepository.save(any(Canteen.class))).thenReturn(canteen);

        CanteenResponse response = canteenService.createCanteen(1L, request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Menza Test 1", response.name());
        assertEquals("Lokacija Test 1", response.location());
        assertEquals(50, response.capacity());
    }
}
