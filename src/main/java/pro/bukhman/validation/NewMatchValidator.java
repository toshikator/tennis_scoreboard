package pro.bukhman.validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

public class NewMatchValidator {

    private static final Logger logger = LogManager.getLogger(NewMatchValidator.class);

    public Map<String, String> validate(String player1Id, String player2Id) {
        logger.debug("Validating new match: player1Id={}, player2Id={}", player1Id, player2Id);
        Map<String, String> errors = new LinkedHashMap<>();

        if (player1Id == null) {
            errors.put("player1Id", "First player is required");
        }

        if (player2Id == null) {
            errors.put("player2Id", "Second player is required");
        }

        if (!errors.isEmpty()) {
            logger.warn("Validation failed: {}", errors);
            return errors;
        }

        Long player1IdLong = Long.parseLong(player1Id);
        Long player2IdLong = Long.parseLong(player2Id);

        if (player1IdLong <= 0) {
            errors.put("player1Id", "First player id must be positive");
        }

        if (player2IdLong <= 0) {
            errors.put("player2Id", "Second player id must be positive");
        }

        if (player1Id.equals(player2Id)) {
            errors.put("player2Id", "Players must be different");
        }

        if (!errors.isEmpty()) {
            logger.warn("Validation failed: {}", errors);
        } else {
            logger.debug("Validation passed for player1Id={}, player2Id={}", player1Id, player2Id);
        }
        return errors;
    }
}