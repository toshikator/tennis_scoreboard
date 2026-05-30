package pro.bukhman.controller;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pro.bukhman.exception.ResourceNotFoundException;
import pro.bukhman.exception.TooManyActiveMatchesException;
import pro.bukhman.matchStorage.OngoingMatchStorage;
import pro.bukhman.service.MatchesService;
import pro.bukhman.service.OngoingMatchesService;
import pro.bukhman.service.PlayerService;
import pro.bukhman.validation.NewMatchValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "NewMatchServlet", urlPatterns = {"/new-match"})
public class NewMatchController extends BasicServlet {

    private static final Logger logger = LogManager.getLogger(NewMatchController.class);

    private NewMatchValidator validator;
    private OngoingMatchStorage ongoingMatchStorage;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        ongoingMatchStorage = (OngoingMatchStorage) context.getAttribute("ongoingMatchStorage");
        validator = new NewMatchValidator();
        logger.info("NewMatchController initialized. OngoingMatchStorage present={} ", ongoingMatchStorage != null);
    }

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        JsonNode json;
//        try {
//            json = objectMapper.readTree(req.getInputStream());
//        } catch (JsonProcessingException e) {
//            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of(
//                    "code", "INVALID_JSON",
//                    "message", "Request body must be valid JSON"
//            ));
//            return;
//        }
//        Long matchId = getLong(json, "matchId");
//        if (matchId == null) {
//            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of(
//                    "code", "INVALID_JSON",
//                    "message", "Request body must contain a valid matchId"
//            ));
//        }
//        try (EntityManager em = emf.createEntityManager()) {
//            MatchesService matchesService = new MatchesService(em);
//
//        }
//    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        logger.info("Received POST /new-match from ip={} contentType={}", req.getRemoteAddr(), req.getContentType());

        sendJson(resp, HttpServletResponse.SC_METHOD_NOT_ALLOWED, Map.of(
                "json-player1Id", req.getParameter("player1Id"),
                "json-player2Id", req.getParameter("player2Id")
        ));

        JsonNode json;
        try {
            json = objectMapper.readTree(req.getInputStream());
            if (logger.isDebugEnabled()) {
                logger.debug("Incoming JSON body parsed successfully");
            }
        } catch (JsonProcessingException e) {
            logger.warn("Invalid JSON in POST /new-match: error={}", e.getOriginalMessage());
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of(
                    "code", "INVALID_JSON",
                    "message", "Request body must be valid JSON",
                    "details", e.getOriginalMessage()

            ));
            return;
        }

        Long player1Id;
        Long player2Id;
        Map<String, String> errors;
        try {
            player1Id = getLong(json, "player1Id");
            player2Id = getLong(json, "player2Id");
            errors = validator.validate(player1Id, player2Id);
        } catch (IllegalArgumentException e) {
            logger.warn("Missing or invalid required fields in JSON: error={}", e.getMessage());
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of(
                    "code", "INVALID_JSON",
                    "message", e.getMessage()
                    // TODO: remove this debugging part

            ));
            return;
        }


        if (!errors.isEmpty()) {
            logger.warn("Validation errors when creating new match: {}", errors);
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of(
                    "code", "VALIDATION_ERROR",
                    "errors", errors,
                    "player1", getString(json, "player1Id"),
                    "player2", getString(json, "player2Id")

            ));
            return;
        }

        try (EntityManager em = emf.createEntityManager()) {
            OngoingMatchesService ongoingMatchesService = new OngoingMatchesService(em, ongoingMatchStorage);
            PlayerService playerService = new PlayerService(em);
            playerService.getPlayerById(player1Id);
            playerService.getPlayerById(player2Id);
            UUID matchId = ongoingMatchesService.createMatch(player1Id, player2Id);
            logger.info("New match created successfully: matchId={}", matchId);
            sendJson(resp, HttpServletResponse.SC_CREATED, Map.of(
                    "matchId", matchId
            ));
        } catch (ResourceNotFoundException e) {
            logger.warn("Player not found when creating match: {}", e.getMessage());
            sendJson(resp, HttpServletResponse.SC_NOT_FOUND, Map.of(
                    "code", "PLAYER_NOT_FOUND",
                    "message", e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            logger.warn("Argument exception when creating match: {}", e.getMessage());
            sendJson(resp, HttpServletResponse.SC_CONFLICT, Map.of(
                    "code", "ARGUMENT_EXCEPTION",
                    "message", e.getMessage()
            ));

        } catch (TooManyActiveMatchesException e) {
            logger.warn("Too many active matches: {}", e.getMessage());
            sendJson(resp, 429, Map.of(
                    "code", "TOO_MANY_ACTIVE_MATCHES",
                    "message", e.getMessage()
            ));

        } catch (Exception e) {
            logger.error("Unexpected error while creating new match", e);
            sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Map.of(
                    "code", "INTERNAL_SERVER_ERROR",
                    "message", "An error occurred while processing the request"
            ));
        }
    }


    private Long getLong(JsonNode json, String fieldName) {
        if (json == null || !json.has(fieldName) || !json.get(fieldName).canConvertToLong()) {
            throw new IllegalArgumentException("Invalid JSON or missing field: " + fieldName);
//            return null;
        }
        return json.get(fieldName).asLong();
    }

    private String getString(JsonNode json, String fieldName) {
        if (json == null || !json.has(fieldName)) {
            return null;
        }
        return json.get(fieldName).asText();
    }
}
