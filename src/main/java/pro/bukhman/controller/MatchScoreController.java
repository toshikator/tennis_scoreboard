package pro.bukhman.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pro.bukhman.exception.InvalidUUIDException;
import pro.bukhman.matchStorage.OngoingMatchStorage;
import pro.bukhman.validation.UuidValidator;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "MatchScoreController", urlPatterns = {"/match-score"})
public class MatchScoreController extends BasicServlet {
    OngoingMatchStorage ongoingMatchStorage;
    private UuidValidator uuidValidator;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ongoingMatchStorage = (OngoingMatchStorage) getServletContext().getAttribute("ongoingMatchStorage");
        uuidValidator = new UuidValidator();
        try (EntityManager em = emf.createEntityManager()) {
            uuidValidator.validate(req.getParameter("match_id"));
            UUID matchId = UUID.fromString(req.getParameter("match_id"));
            sendJson(resp, HttpServletResponse.SC_OK, ongoingMatchStorage.getDtoByUUID(matchId));
        } catch (InvalidUUIDException e) {
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("message: ", "Invalid UUID", "error: ", e.getMessage()));
        } catch (Exception e) {
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("message: ", "Bad request", "error: ", e.getMessage()));
        }


    }


}
