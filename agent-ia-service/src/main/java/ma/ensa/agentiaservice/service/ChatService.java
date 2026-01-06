package ma.ensa.agentiaservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChatService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String chat(String message) {

        String lower = message.toLowerCase();

        try {
            // 🔹 STOCK
            if (lower.contains("stock")) {




                Long productId = extractProductId(message);

                if (productId == null) {
                    return "⚠️ Veuillez préciser l'id du produit.";
                }


                Object response = restTemplate.getForObject(
                        "http://localhost:9092/api/stocks/{productId}",
                        Object.class,
                        productId
                );

                if (response == null) {
                    return " Aucun stock trouvé pour le produit " + productId;
                }

                return "📦 Stock du produit " + productId + " : " + response;
            }

            // 🔹 PRODUITS
            if (lower.contains("produit")) {

                Object products = restTemplate.getForObject(
                        "http://localhost:9091/api/products",
                        Object.class
                );

                return "📦 Liste des produits : " + products;
            }

            return "🤖 Je peux te renseigner sur les produits ou le stock.";

        } catch (Exception e) {
            return "❌ Erreur Agent IA : " + e.getMessage();
        }
    }

    // 🔎 Extraction de l'ID depuis le message
    private Long extractProductId(String message) {
        Pattern pattern = Pattern.compile("(id|product)\\s*(=|:)?\\s*(\\d+)");
        Matcher matcher = pattern.matcher(message.toLowerCase());

        if (matcher.find()) {
            return Long.parseLong(matcher.group(3));
        }
        return null;
    }
}