package sn.project.consultation.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.project.consultation.data.entities.Paiement;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaytechService {

    @Value("${paytech.api.key}")
    private String apiKey;

    @Value("${paytech.api.url}")
    private String apiUrl;

//    @Value("${paytech.api.callback-url}")
    private String callbackUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String initierPaiement(Double montant, String methode, Long paiementId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("amount", montant);
        payload.put("currency", "XOF");
        payload.put("paymentMethod", methode); // "WAVE" ou "ORANGE"
        payload.put("transactionId", paiementId.toString());


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl + "/payments", request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = response.getBody();
            return (String) body.get("paymentUrl"); // URL de redirection vers le paiement
        } else {
            throw new RuntimeException("Erreur lors de la création du paiement");
        }
    }
}

