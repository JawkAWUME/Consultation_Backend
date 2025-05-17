package sn.project.consultation.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sn.project.consultation.api.dto.ProSanteDTO;
import sn.project.consultation.api.dto.RechercheProDTO;
import sn.project.consultation.api.dto.RendezVousDTO;
import sn.project.consultation.api.dto.TourneeOptimiseeDTO;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.entities.RendezVous;
import sn.project.consultation.data.repositories.PatientRepository;
import sn.project.consultation.data.repositories.ProSanteRepository;
import sn.project.consultation.data.repositories.RendezVousRepository;
import sn.project.consultation.services.EmailService;
import sn.project.consultation.services.RendezVousService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RendezVousServiceImpl implements RendezVousService {
    @Autowired
    private RendezVousRepository repo;
    @Autowired private PatientRepository patientRepo;
    @Autowired private ProSanteRepository proRepo;

    @Autowired private SmsService smsService;
    @Autowired private EmailService emailService;

    @Scheduled(cron = "0 0 8 * * *")
    public void envoyerRappels() {
        LocalDateTime demain = LocalDateTime.now().plusDays(1);
        List<RendezVous> rdvs = repo.findByDateHeureBetween(demain.withHour(0), demain.withHour(23));
        for (RendezVous rdv : rdvs) {
            String email = rdv.getPatient().getEmail();
            String phone = rdv.getPatient().getNumeroTelephone();

            String sujet = "Rappel de votre rendez-vous";
            String contenu = "Bonjour " + rdv.getPatient().getNom() + ", votre rendez-vous est prévu le " + rdv.getDateHeure();

            emailService.envoyerEmail(email, sujet, contenu);
            smsService.envoyerSms(phone, contenu);
        }
    }
    public RendezVousDTO creerRendezVous(RendezVousDTO dto) {
        RendezVous rdv = new RendezVous();
        rdv.setDateHeure(dto.getDateHeure());
        rdv.setStatut("EN_ATTENTE");
        rdv.setPatient(patientRepo.findById(dto.getPatientId()).orElseThrow());
        rdv.setProsante(proRepo.findById(dto.getProfessionnelId()).orElseThrow());
        repo.save(rdv);
        dto.setId(rdv.getId());
        return dto;
    }

    public void annulerRendezVous(Long id) {
        RendezVous rdv = repo.findById(id).orElseThrow();
        rdv.setStatut("ANNULÉ");
        repo.save(rdv);
    }

    public List<RendezVousDTO> listerRendezVousParPatient(Long patientId) {
        return repo.findByPatientId(patientId).stream().map(rdv -> {
            RendezVousDTO dto = new RendezVousDTO();
            dto.setId(rdv.getId());
            dto.setDateHeure(rdv.getDateHeure());
            dto.setStatut(rdv.getStatut());
            dto.setPatientId(rdv.getPatient().getId());
            dto.setProfessionnelId(rdv.getProsante().getId());
            return dto;
        }).collect(Collectors.toList());
    }

     public List<ProSanteDTO> rechercherProfessionnels(RechercheProDTO criteres) {
        List<ProSante> all = proRepo.findAll();
        return all.stream()
            .filter(p -> criteres.getSpecialite() == null || p.getSpecialite().toLowerCase().contains(criteres.getSpecialite().toLowerCase()))
            .filter(p -> criteres.getTarifMax() == null || p.getTarif() <= criteres.getTarifMax())
            .map(p -> {
                double distance = calculerDistance(criteres.getLatitude(), criteres.getLongitude(), p.getLatitude(), p.getLongitude());
                if (criteres.getRayonKm() != null && distance > criteres.getRayonKm()) return null;
                ProSanteDTO dto = new ProSanteDTO();
                dto.setId(p.getId());
                dto.setNom(p.getNom());
                dto.setSpecialite(p.getSpecialite());
                dto.setTarif(p.getTarif());
                dto.setLatitude(p.getLatitude());
                dto.setLongitude(p.getLongitude());
                dto.setDistanceKm(distance);
                return dto;
            })
            .filter(Objects::nonNull)
            .sorted(Comparator.comparingDouble(ProSanteDTO::getDistanceKm))
            .collect(Collectors.toList());
    }

    public RendezVousDTO modifierRendezVous(Long id, RendezVousDTO dto) {
        RendezVous rdv = repo.findById(id).orElseThrow();
        rdv.setDateHeure(dto.getDateHeure());
        repo.save(rdv);
        return dto;
    }

     public TourneeOptimiseeDTO optimiserTournee(Long professionnelId) {
        List<RendezVous> rdvs = repo.findByProsanteIdAndDateHeureAfterOrderByDateHeureAsc(professionnelId, LocalDateTime.now());
        ProSante pro = proRepo.findById(professionnelId).orElseThrow();

        List<RendezVousDTO> ordonne = rdvs.stream()
            .sorted(Comparator.comparing(r -> calculerDistance(pro.getLatitude(), pro.getLongitude(), r.getPatient().getLatitude(), r.getPatient().getLongitude())))
            .map(r -> {
                RendezVousDTO dto = new RendezVousDTO();
                dto.setId(r.getId());
                dto.setPatientId(r.getPatient().getId());
                dto.setProfessionnelId(r.getProsante().getId());
                dto.setDateHeure(r.getDateHeure());
                dto.setStatut(r.getStatut());
                return dto;
            })
            .collect(Collectors.toList());

        TourneeOptimiseeDTO tournee = new TourneeOptimiseeDTO();
        tournee.setDate(LocalDateTime.now());
        tournee.setOrdreVisites(ordonne);
        return tournee;
    }

    private double calculerDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
