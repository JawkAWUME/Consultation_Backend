package sn.project.consultation.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

@Service
public class CloudStorageService {

    private static final String BASE_DIR = "uploads/"; // Peut être changé en /opt/... ou cloud bucket simulé
    private static final String BASE_URL = "http://localhost:8080/files/"; // Simule une URL publique

    /**
     * 📤 Simule un "upload cloud" (enregistrement local avec URL simulée)
     */
    public String upload(byte[] contenu, String cheminRelatif) {
        try {
            File fichier = new File(BASE_DIR + cheminRelatif);
            File dossierParent = fichier.getParentFile();

            if (!dossierParent.exists()) {
                dossierParent.mkdirs();
            }

            try (FileOutputStream fos = new FileOutputStream(fichier)) {
                fos.write(contenu);
            }

            // 💡 Retourne une URL simulée d'accès public (à adapter si CDN ou cloud réel)
            return BASE_URL + cheminRelatif.replace("\\", "/");

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'upload du fichier", e);
        }
    }

    /**
     * 📥 Lecture d'un fichier
     */
    public byte[] lireFichier(String cheminAbsolu) {
        try {
            return Files.readAllBytes(new File(cheminAbsolu).toPath());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier", e);
        }
    }

    /**
     * Pour récupérer le chemin absolu depuis un chemin relatif
     */
    public String getCheminComplet(String cheminRelatif) {
        return BASE_DIR + cheminRelatif;
    }
}
