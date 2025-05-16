package sn.project.consultation.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.project.consultation.api.dto.ProSanteDTO;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.repositories.ProSanteRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pros")
@CrossOrigin("*")
public class ProSanteController {

    @Autowired
    private ProSanteRepository proRepository;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getProsAsDto() {
        List<ProSante> pros = proRepository.findAll();

        List<ProSanteDTO> dtos = pros.stream().map(pro -> {
            ProSanteDTO dto = new ProSanteDTO();
            dto.setId(pro.getId());
            dto.setNom(pro.getNom());
            dto.setSpecialite(pro.getSpecialite());
            dto.setTarif(pro.getTarif());
            dto.setLatitude(pro.getLatitude());
            dto.setLongitude(pro.getLongitude());
            return dto;
        }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("data", dtos);
        return ResponseEntity.ok(response);
    }

}

