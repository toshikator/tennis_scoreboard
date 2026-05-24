package pro.bukhman.controller;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import pro.bukhman.matchStorage.OngoingMatchStorage;
import pro.bukhman.service.OngoingMatchesService;
import pro.bukhman.validation.NewMatchValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;


import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "NewMatchServlet", urlPatterns = {"/new-match"})
public class NewMatchServlet extends BasicServlet {

    private NewMatchValidator validator;
    private OngoingMatchStorage ongoingMatchStorage;
    private OngoingMatchesService ongoingMatchesService;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        ongoingMatchStorage = (OngoingMatchStorage) context.getAttribute("ongoingMatchStorage");
        validator = new NewMatchValidator();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        JsonNode json;
        try {
            json = objectMapper.readTree(req.getInputStream());
        } catch (JsonProcessingException e) {
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of(
                    "code", "INVALID_JSON",
                    "message", "Request body must be valid JSON"
            ));
            return;
        }

        Long player1Id = getLong(json, "player1Id");
        Long player2Id = getLong(json, "player2Id");
        Map<String, String> errors = validator.validate(player1Id, player2Id);

        if (!errors.isEmpty()) {
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of(
                    "code", "VALIDATION_ERROR",
                    "errors", errors
            ));
            return;
        }

        try (EntityManager em = emf.createEntityManager()) {
            ongoingMatchesService = new OngoingMatchesService(em, ongoingMatchStorage);
            UUID matchId = ongoingMatchesService.createMatch(player1Id, player2Id);

            sendJson(resp, HttpServletResponse.SC_CREATED, Map.of(
                    "matchId", matchId
            ));
        } catch (Exception e) {
            sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Map.of(
                    "code", "INTERNAL_SERVER_ERROR",
                    "message", "An error occurred while processing the request"
            ));

        }
    }


    private Long getLong(JsonNode json, String fieldName) {
        if (json == null || !json.has(fieldName) || !json.get(fieldName).canConvertToLong()) {
            return null;
        }

        return json.get(fieldName).asLong();
    }
}
