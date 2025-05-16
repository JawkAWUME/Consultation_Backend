package sn.project.consultation.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.repositories.PatientRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sn.project.consultation.api.dto.PatientDTO;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin("*")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllPatientsAsDTO() {
        List<Patient> patients = patientRepository.findAll();

        List<PatientDTO> dtoList = patients.stream()
                .map(PatientDTO::fromEntity)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("data", dtoList);

        return ResponseEntity.ok(response);
    }
}

