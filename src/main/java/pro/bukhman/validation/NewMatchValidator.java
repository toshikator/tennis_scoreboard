package pro.bukhman.validation;

import java.util.LinkedHashMap;
import java.util.Map;

public class NewMatchValidator {

    public Map<String, String> validate(Long player1Id, Long player2Id) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (player1Id == null) {
            errors.put("player1Id", "First player is required");
        }

        if (player2Id == null) {
            errors.put("player2Id", "Second player is required");
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        if (player1Id <= 0) {
            errors.put("player1Id", "First player id must be positive");
        }

        if (player2Id <= 0) {
            errors.put("player2Id", "Second player id must be positive");
        }

        if (player1Id.equals(player2Id)) {
            errors.put("player2Id", "Players must be different");
        }

        return errors;
    }
}