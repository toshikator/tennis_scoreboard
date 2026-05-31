package pro.bukhman.controller;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.bukhman.exception.InvalidUUIDException;
import pro.bukhman.exception.MatchIsAlreadyFinishedException;
import pro.bukhman.exception.MatchNotFoundException;
import pro.bukhman.exception.ResourceNotFoundException;
import pro.bukhman.model.OngoingMatch;
import pro.bukhman.ongoingMatchStorage.OngoingMatchStorage;
import pro.bukhman.model.dto.OngoingMatchDto;
import pro.bukhman.service.MatchesService;
import pro.bukhman.service.OngoingMatchesService;
import pro.bukhman.service.PlayerService;
import pro.bukhman.validation.UuidValidator;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "MatchScoreController", urlPatterns = {"/match-score"})
public class MatchScoreController extends BasicServlet {
    private static final Logger logger = LogManager.getLogger(MatchScoreController.class);

    private OngoingMatchStorage ongoingMatchStorage;
    private UuidValidator uuidValidator;
//    private OngoingMatchesService ongoingMatchesService;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        ongoingMatchStorage = (OngoingMatchStorage) context.getAttribute("ongoingMatchStorage");
        uuidValidator = new UuidValidator();

        logger.info("NewMatchController initialized. OngoingMatchStorage present={} ", ongoingMatchStorage != null);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String matchIdParam = req.getParameter("match_id");
        logger.info("Received GET /match-score with match_id={}", matchIdParam);
        try (EntityManager em = emf.createEntityManager()) {
            uuidValidator.validate(matchIdParam);
            UUID matchId = UUID.fromString(matchIdParam);
            OngoingMatchDto dto = ongoingMatchStorage.getDtoByUUID(matchId);
            logger.debug("Returning DTO for match_id={}", matchId);
            sendJson(resp, HttpServletResponse.SC_OK, dto);
        } catch (InvalidUUIDException e) {
            logger.warn("Invalid UUID received GET method: match_id={}, error={}", matchIdParam, e.getMessage());
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("message: ", "Invalid UUID", "error: ", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error processing GET /match-score: match_id={}, cause=", matchIdParam, e);
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("message: ", "Bad request", "error: ", e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("Received POST /match-score");
        String matchId = req.getParameter("match_id");
        try (EntityManager em = emf.createEntityManager()) {
            UUID matchUUID;
            uuidValidator.validate(req.getParameter("match_id"));
            logger.info("POST /match-score: match_id={}", matchId);
            matchUUID = UUID.fromString(matchId);

            Long playerForScoreId;
            playerForScoreId = Long.parseLong(req.getParameter("player_for_score_id"));


            OngoingMatchesService ongoingMatchesService = new OngoingMatchesService(em, ongoingMatchStorage);
            ongoingMatchesService.addPoint(matchUUID, playerForScoreId);


            OngoingMatchDto dto = ongoingMatchStorage.getDtoByUUID(matchUUID);
            logger.info("POST /match-score: match_id={}, player_for_score_id={}", matchId, playerForScoreId);
            sendJson(resp, HttpServletResponse.SC_OK, dto);
        } catch (ResourceNotFoundException e) {
            logger.warn("Player not found on current ongoingMatch: {}", e.getMessage());
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of(
                    "code", "PLAYER_NOT_FOUND",
                    "message", "wrong player ID, for adding score player should participate the match"
            ));
        } catch (InvalidUUIDException e) {
            logger.warn("Invalid UUID received POST method: match_id={}, error={}", matchId, e.getMessage());
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("message: ", "Invalid Match UUID", "error: ", e.getMessage()));
        } catch (NumberFormatException e) {
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("message: ", "Invalid player_for_score_id", "error: ", e.getMessage()));
        } catch (MatchIsAlreadyFinishedException e) {
            logger.warn("Match is already finished: match_id={}", matchId);
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("message: ", "Match is already finished", "error: ", e.getMessage()));
        } catch (MatchNotFoundException e) {
            logger.warn("Match not found: match_id={}", matchId);
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("message: ", "Match not found", "error: ", e.getMessage()));
        }

    }


}
