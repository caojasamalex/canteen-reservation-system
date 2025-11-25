package com.djokic.canteenreservationsystem;

import com.djokic.canteenreservationsystem.dto.student.CreateStudentRequest;
import com.djokic.canteenreservationsystem.dto.student.StudentResponse;
import com.djokic.canteenreservationsystem.entity.Student;
import com.djokic.canteenreservationsystem.repository.StudentRepository;
import com.djokic.canteenreservationsystem.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void successCreateStudentAndReturnResponse(){
        CreateStudentRequest createStudentRequest = new CreateStudentRequest(
                "Miodrag Jankovic",
                "miodrag.jankovic@etf.com",
                false
        );

        Student saved = Student.builder()
                .id(1L)
                .name(createStudentRequest.name())
                .email(createStudentRequest.email())
                .isAdmin(createStudentRequest.isAdmin())
                .build();

        when(studentRepository.save(any(Student.class))).thenReturn(saved);

        StudentResponse response = studentService.createStudent(createStudentRequest);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(createStudentRequest.name(), response.name());
        assertEquals(createStudentRequest.email(), response.email());
        assertEquals(createStudentRequest.isAdmin(), response.isAdmin());
    }

    @Test
    void createStudentShouldThrowConflictExceptionWhenEmailAlreadyExists(){
        CreateStudentRequest createStudentRequest = new CreateStudentRequest(
                "Petar Petrovic",
                "petar.petrovic@ftn.rs",
                true
        );

        when(studentRepository.existsByEmail(createStudentRequest.email())).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> studentService.createStudent(createStudentRequest));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertEquals("Student with this email already exists.", ex.getReason());
    }

    @Test
    void createStudentShouldThrowBadRequestExceptionWhenEmailIsInvalid(){
        CreateStudentRequest createStudentRequest = new CreateStudentRequest(
                "Miodrag Jankovic",
                "miodrag.jankovic",
                false
        );

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> studentService.createStudent(createStudentRequest));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Invalid email format.", ex.getReason());
    }

    @Test
    void getStudentByIdShouldReturnStudentResponseWhenExists(){
        Long id = 5L;

        Student s = Student.builder()
                .id(id)
                .name("Nikola Petrovic")
                .email("nikola.petrovic@fink.rs")
                .isAdmin(true)
                .build();

        when(studentRepository.findById(id)).thenReturn(Optional.of(s));

        StudentResponse response = studentService.getStudentById(id);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals(s.getName(), response.name());
        assertEquals(s.getEmail(), response.email());
        assertEquals(s.isAdmin(), response.isAdmin());
    }

    @Test
    void getStudentByIdShouldThrowNotFoundExceptionWhenStudentDoesNotExist(){
        Long id = 232L;

        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> studentService.getStudentById(id));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Student not found.", ex.getReason());
    }
}
