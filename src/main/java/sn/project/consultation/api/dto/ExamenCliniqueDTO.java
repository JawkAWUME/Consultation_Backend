package sn.project.consultation.api.dto;

import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.entities.ExamenClinique;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ExamenCliniqueDTO {
    private Double poids;
    private Double taille;
    private String tensionArterielle;
    private Double temperature;
    private Integer frequenceCardiaque;
    private Integer saturationOxygene;
    private Map<String, String> bilanPhysique;
    private List<String> observations;

    public static ExamenCliniqueDTO toDTO(ExamenClinique entity) {
        if (entity == null) return null;
        ExamenCliniqueDTO dto = new ExamenCliniqueDTO();
        dto.setPoids(entity.getPoids());
        dto.setTaille(entity.getTaille());
        dto.setTensionArterielle(entity.getTensionArterielle());
        dto.setTemperature(entity.getTemperature());
        dto.setFrequenceCardiaque(entity.getFrequenceCardiaque());
        dto.setSaturationOxygene(entity.getSaturationOxygene());
        dto.setBilanPhysique(entity.getBilanPhysique());
        dto.setObservations(entity.getObservations());
        return dto;
    }

    public static ExamenClinique toEntity(ExamenCliniqueDTO dto) {
        if (dto == null) return null;
        ExamenClinique entity = new ExamenClinique();
        entity.setPoids(dto.getPoids());
        entity.setTaille(dto.getTaille());
        entity.setTensionArterielle(dto.getTensionArterielle());
        entity.setTemperature(dto.getTemperature());
        entity.setFrequenceCardiaque(dto.getFrequenceCardiaque());
        entity.setSaturationOxygene(dto.getSaturationOxygene());
        entity.setBilanPhysique(dto.getBilanPhysique());
        entity.setObservations(dto.getObservations());
        return entity;
    }

}
