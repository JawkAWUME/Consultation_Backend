package sn.project.consultation.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void envoyerEmail(String to, String sujet, String contenu) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(sujet);
        message.setText(contenu);
        mailSender.send(message);
    }

    public void envoyerEmail(String to, String sujet, String contenu, byte[] pdf, String nomFichier) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(sujet);
            helper.setText(contenu);
            helper.addAttachment(nomFichier, new ByteArrayResource(pdf));

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email avec pièce jointe", e);
        }

    }

    // ✅ Envoi avec pièce jointe depuis un chemin local
    public void envoyerEmail(String to, String sujet, String contenu, String cheminFichier) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(sujet);
            helper.setText(contenu);

            FileSystemResource file = new FileSystemResource(new File(cheminFichier));
            if (!file.exists()) {
                throw new RuntimeException("Fichier non trouvé : " + cheminFichier);
            }

            helper.addAttachment(file.getFilename(), file);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email avec fichier joint", e);
        }
    }
}
