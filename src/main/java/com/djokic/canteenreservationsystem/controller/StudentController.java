package com.djokic.canteenreservationsystem.controller;

import com.djokic.canteenreservationsystem.dto.student.CreateStudentRequest;
import com.djokic.canteenreservationsystem.dto.student.StudentResponse;
import com.djokic.canteenreservationsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@RequestBody CreateStudentRequest createStudentRequest){
        StudentResponse studentResponse = studentService.createStudent(createStudentRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(studentResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id){
        return ResponseEntity.ok(studentService.getStudentById(id));
    }
}
