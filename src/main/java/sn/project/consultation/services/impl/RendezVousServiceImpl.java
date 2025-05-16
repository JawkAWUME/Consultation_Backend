package sn.project.consultation.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sn.project.consultation.api.dto.*;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.entities.RendezVous;
import sn.project.consultation.data.repositories.PatientRepository;
import sn.project.consultation.data.repositories.ProSanteRepository;
import sn.project.consultation.data.repositories.RendezVousRepository;
import sn.project.consultation.services.EmailService;
import sn.project.consultation.services.RendezVousService;
import smile.clustering.KMeans;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class RendezVousServiceImpl implements RendezVousService {
    @Autowired
    private RendezVousRepository repo;
    @Autowired private PatientRepository patientRepo;
    @Autowired private ProSanteRepository proRepo;

    @Autowired private SmsService smsService;
    @Autowired private EmailService emailService;

    @Scheduled(fixedRate = 60000) // Exécuté toutes les minutes
    public void envoyerRappels() {
        LocalDateTime maintenant = LocalDateTime.now();
        LocalDateTime dansTroisHeures = maintenant.plusHours(3);

        List<RendezVous> rdvs = repo.findByDateHeureBetween(maintenant, dansTroisHeures);

        for (RendezVous rdv : rdvs) {
            if (!rappelDejaEnvoye(rdv)) {
                String email = rdv.getPatient().getEmail();
                String phone = rdv.getPatient().getNumeroTelephone();
                String nom = rdv.getPatient().getNom();
                String date = rdv.getDateHeure().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm"));

                String contenu = "Bonjour " + nom + ", ceci est un rappel pour votre rendez-vous prévu le " + date;

                emailService.envoyerEmail(email, "Rappel de rendez-vous", contenu);
                smsService.envoyerSms(phone, contenu);

                // Marquer comme rappelé si tu veux éviter les doublons (ex : en base de données ou via Redis)
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *") // Tous les jours à minuit
    public void mettreAJourStatuts() {
        LocalDateTime maintenant = LocalDateTime.now();
        List<RendezVous> anciens = repo.findByDateHeureBefore(maintenant);

        for (RendezVous rdv : anciens) {
            if ("EN_ATTENTE".equals(rdv.getStatut())) {
                rdv.setStatut("NON_HONORE"); // ou "PASSÉ" selon ta logique métier
                repo.save(rdv);
            }
        }
    }


    private boolean rappelDejaEnvoye(RendezVous rdv) {
        // Implémentation de logique pour éviter de renvoyer le rappel (ex: champ boolean "rappelEnvoye" ou cache Redis)
        return false;
    }

    public RendezVousDTO creerRendezVous(RendezVousDTO dto) {
        RendezVous rdv = new RendezVous();
        rdv.setDateHeure(dto.getDateHeure());
        rdv.setStatut("EN_ATTENTE");
        rdv.setPatient(PatientDTO.toEntity(dto.getPatient()));
        rdv.setProsante(ProSanteDTO.toEntity(dto.getPro_sante()));
        repo.save(rdv);
        dto.setId(rdv.getId());

        // ✅ Si le rendez-vous est dans les 24h => envoyer une alerte au Pro
        if (dto.getDateHeure().isBefore(LocalDateTime.now().plusHours(24))) {
            String msg = "🔴 Nouveau rendez-vous urgent de " + dto.getPatient().getNom()
                    + " prévu à " + dto.getDateHeure();
            emailService.envoyerEmail(rdv.getProsante().getEmail(), "Rendez-vous urgent", msg);
            smsService.envoyerSms(rdv.getProsante().getNumeroTelephone(), msg);
        }
        return dto;
    }

    public List<LocalDateTime> getCreneauxDisponibles(Long proId, LocalDate date) {
        LocalDateTime start = date.atTime(9, 0);
        LocalDateTime end = date.atTime(17, 0);
        List<RendezVous> existants = repo.findByProsanteIdAndDateHeureAfterOrderByDateHeureAsc(proId, start);

        List<LocalDateTime> allSlots = new ArrayList<>();
        for (LocalDateTime t = start; !t.isAfter(end.minusMinutes(30)); t = t.plusMinutes(30)) {
            allSlots.add(t);
        }

        Set<LocalDateTime> pris = existants.stream()
                .filter(r -> r.getDateHeure().toLocalDate().equals(date))
                .map(RendezVous::getDateHeure)
                .collect(Collectors.toSet());

        return allSlots.stream()
                .filter(slot -> !pris.contains(slot))
                .collect(Collectors.toList());
    }

    public Map<String, Object> statistiquesHebdo(Long professionnelId) {
        LocalDateTime start = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime end = start.plusDays(7);

        List<RendezVous> rdvs = repo.findByProsanteIdAndDateHeureAfterOrderByDateHeureAsc(professionnelId, start);
        long total = rdvs.stream().filter(r -> r.getDateHeure().isBefore(end)).count();
        long annules = rdvs.stream().filter(r -> "ANNULÉ".equalsIgnoreCase(r.getStatut())).count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRendezVous", total);
        stats.put("annulations", annules);
        stats.put("tauxAnnulation", total > 0 ? (annules * 100.0 / total) : 0);
        return stats;
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
            dto.setPatient(PatientDTO.fromEntity(rdv.getPatient()));
            dto.setPro_sante(ProSanteDTO.fromEntity(rdv.getProsante()));
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

        if (rdvs.isEmpty()) return new TourneeOptimiseeDTO(LocalDateTime.now(), new ArrayList<>());

        // ✅ Étape 1 : Préparer les données de clustering
        double[][] coords = rdvs.stream()
                .map(r -> new double[]{r.getPatient().getLatitude(), r.getPatient().getLongitude()})
                .toArray(double[][]::new);

        // ✅ Étape 2 : Appliquer KMeans
        int k = Math.min(3, rdvs.size()); // Choix du nombre de clusters (peut être paramétrable)
        KMeans km = KMeans.fit(coords, k);

        // ✅ Étape 3 : Associer les clusters aux rendez-vous
        List<RendezVousDTO> ordonnes = new ArrayList<>();
        IntStream.range(0, rdvs.size())
                .boxed()
                .sorted(Comparator.comparingInt(i -> km.y[i])) // Tri par cluster
                .forEach(i -> {
                    RendezVous r = rdvs.get(i);
                    RendezVousDTO dto = new RendezVousDTO();
                    dto.setId(r.getId());
                    dto.setPatient(PatientDTO.fromEntity(r.getPatient()));
                    dto.setPro_sante(ProSanteDTO.fromEntity(r.getProsante()));
                    dto.setDateHeure(r.getDateHeure());
                    dto.setStatut(r.getStatut());
                    ordonnes.add(dto);
                });

        // ✅ Étape 4 : Retour
        return new TourneeOptimiseeDTO(LocalDateTime.now(), ordonnes);
    }

    public List<Map<String, Object>> getCartePatients(Long proId) {
        List<RendezVous> rdvs = repo.findByProsanteIdAndDateHeureAfterOrderByDateHeureAsc(proId, LocalDateTime.now());

        return rdvs.stream()
                .map(r -> {
                    Map<String, Object> p = new HashMap<>();
                    p.put("nom", r.getPatient().getNom() + " " + r.getPatient().getPrenom());
                    p.put("lat", r.getPatient().getLatitude());
                    p.put("lon", r.getPatient().getLongitude());
                    p.put("rdv", r.getDateHeure());
                    return p;
                }).collect(Collectors.toList());
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
