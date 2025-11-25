package com.djokic.canteenreservationsystem.service;

import com.djokic.canteenreservationsystem.dto.student.CreateStudentRequest;
import com.djokic.canteenreservationsystem.dto.student.StudentResponse;
import com.djokic.canteenreservationsystem.entity.Student;
import com.djokic.canteenreservationsystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@RequiredArgsConstructor
@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentResponse createStudent(CreateStudentRequest createStudentRequest){
        String regexPattern = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
                + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$";
        if(!createStudentRequest.email().matches(regexPattern)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format.");
        }

        if(studentRepository.existsByEmail(createStudentRequest.email().toLowerCase())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student with this email already exists.");
        }

        Student student = Student.builder()
                .email(createStudentRequest.email().toLowerCase())
                .name(createStudentRequest.name())
                .isAdmin(createStudentRequest.isAdmin())
                .build();

        Student saved = studentRepository.save(student);

        return new StudentResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.isAdmin()
        );
    }

    public StudentResponse getStudentById(Long id){
        return studentRepository.findById(id)
                .map(s -> new StudentResponse(s.getId(), s.getName(), s.getEmail(), s.isAdmin()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found."));
    }
}
