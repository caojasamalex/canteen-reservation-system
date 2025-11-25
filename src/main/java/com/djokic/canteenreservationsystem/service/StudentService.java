package com.djokic.canteenreservationsystem.service;

import com.djokic.canteenreservationsystem.dto.student.CreateStudentRequest;
import com.djokic.canteenreservationsystem.dto.student.StudentResponse;
import com.djokic.canteenreservationsystem.entity.Student;
import com.djokic.canteenreservationsystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentResponse createStudent(CreateStudentRequest createStudentRequest){
        if(studentRepository.existsByEmail(createStudentRequest.email())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with this email already exists.");
        }

        Student student = Student.builder()
                .email(createStudentRequest.email())
                .name(createStudentRequest.name())
                .isAdmin(createStudentRequest.isAdmin())
                .build();

        Student saved = studentRepository.save(student);

        return new StudentResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getName(),
                saved.isAdmin()
        );
    }

    public StudentResponse getStudentById(Long id){
        return studentRepository.findById(id)
                .map(s -> new StudentResponse(s.getId(), s.getName(), s.getEmail(), s.isAdmin()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found."));
    }
}
