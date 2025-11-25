package com.djokic.canteenreservationsystem.controller;

import com.djokic.canteenreservationsystem.dto.canteen.CanteenResponse;
import com.djokic.canteenreservationsystem.dto.canteen.CreateCanteenRequest;
import com.djokic.canteenreservationsystem.dto.canteen.UpdateCanteenRequest;
import com.djokic.canteenreservationsystem.service.CanteenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/canteens")
@RequiredArgsConstructor
public class CanteenController {

    private final CanteenService canteenService;

    @PostMapping
    public ResponseEntity<CanteenResponse> createCanteen(
            @RequestHeader("studentId") Long studentId,
            @RequestBody CreateCanteenRequest createCanteenRequest){
        return  ResponseEntity.status(HttpStatus.CREATED)
                .body(canteenService.createCanteen(studentId, createCanteenRequest));
    }

    @GetMapping
    public List<CanteenResponse> getAll(){
        return canteenService.getAllCanteens();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CanteenResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(canteenService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CanteenResponse> update(
            @RequestHeader("studentId") Long studentId,
            @PathVariable Long id,
            @RequestBody UpdateCanteenRequest updateCanteenRequest){
        return ResponseEntity.ok(canteenService.updateCanteen(studentId, id, updateCanteenRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("studentId") Long studentId,
            @PathVariable Long id){
        canteenService.deleteCanteen(studentId, id);
        return ResponseEntity.noContent().build();
    }
}
