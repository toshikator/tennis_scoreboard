package pro.bukhman.controller;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pro.bukhman.exception.ResourceNotFoundException;
import pro.bukhman.exception.TooManyActiveMatchesException;
import pro.bukhman.ongoingMatchStorage.OngoingMatchStorage;
import pro.bukhman.service.OngoingMatchesService;
import pro.bukhman.service.PlayerService;
import pro.bukhman.validation.NewMatchValidator;
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


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        logger.info("Received POST /new-match from ip={} contentType={}", req.getRemoteAddr(), req.getContentType());
        logger.info("req-player1Id={}, req-player2Id={}", req.getParameter("player1Id"), req.getParameter("player2Id"));


        Long player1Id;
        Long player2Id;
        Map<String, String> errors;
        try {
            errors = validator.validate(req.getParameter("player1Id"), req.getParameter("player2Id"));
            if (!errors.isEmpty()) {
                logger.warn("Validation error for POST /new-match: errors={}", errors);
                throw new IllegalArgumentException(errors.toString());
            }
            player1Id = Long.parseLong(req.getParameter("player1Id"));
            player2Id = Long.parseLong(req.getParameter("player2Id"));

        } catch (IllegalArgumentException e) {
            logger.warn("Missing or invalid required fields in JSON: error={}", e.getMessage());
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of(
                    "code", "INVALID_REQUEST",
                    "errors:", e.getMessage()
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


}
