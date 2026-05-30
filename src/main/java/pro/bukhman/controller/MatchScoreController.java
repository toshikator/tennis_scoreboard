package pro.bukhman.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.bukhman.exception.InvalidUUIDException;
import pro.bukhman.matchStorage.OngoingMatchStorage;
import pro.bukhman.validation.UuidValidator;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "MatchScoreController", urlPatterns = {"/match-score"})
public class MatchScoreController extends BasicServlet {
    private static final Logger logger = LogManager.getLogger(MatchScoreController.class);

    OngoingMatchStorage ongoingMatchStorage;
    private UuidValidator uuidValidator;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ongoingMatchStorage = (OngoingMatchStorage) getServletContext().getAttribute("ongoingMatchStorage");
        uuidValidator = new UuidValidator();
        String matchIdParam = req.getParameter("match_id");
        logger.info("Received GET /match-score with match_id={}", matchIdParam);
        try (EntityManager em = emf.createEntityManager()) {
            uuidValidator.validate(matchIdParam);
            UUID matchId = UUID.fromString(matchIdParam);
            Object dto = ongoingMatchStorage.getDtoByUUID(matchId);
            logger.debug("Returning DTO for match_id={}", matchId);
            sendJson(resp, HttpServletResponse.SC_OK, dto);
        } catch (InvalidUUIDException e) {
            logger.warn("Invalid UUID received: match_id={}, error={}", matchIdParam, e.getMessage());
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("message: ", "Invalid UUID", "error: ", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error processing GET /match-score: match_id={}, cause=", matchIdParam, e);
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("message: ", "Bad request", "error: ", e.getMessage()));
        }


    }


}
