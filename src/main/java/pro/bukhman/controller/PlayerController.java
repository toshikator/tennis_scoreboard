package pro.bukhman.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import pro.bukhman.exception.ResourceAlreadyExistsException;
import pro.bukhman.exception.ResourceNotFoundException;
import pro.bukhman.model.dto.PlayerDto;
import pro.bukhman.model.entity.Player;
import pro.bukhman.service.PlayerService;
import pro.bukhman.validation.NewPlayerValidator;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "PlayerController", urlPatterns = {"/player"})
public class PlayerController extends BasicServlet {
    private static final Logger logger = LogManager.getLogger(PlayerController.class);


    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("Initializing PlayerController");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Incoming request: method=GET, uri={}, query={}, remoteIp={}", req.getRequestURI(), req.getQueryString(), req.getRemoteAddr());
        String idParam = req.getParameter("id");
        Long id;
        try {
            id = Long.parseLong(idParam);
        } catch (Exception e) {
            logger.warn("Invalid 'id' parameter for /player: id={}", idParam);
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of(
                    "code", "INVALID_ID",
                    "message", "Query parameter 'id' must be a valid number"
            ));
            return;
        }

        try (EntityManager em = emf.createEntityManager()) {
            PlayerService playerService = new PlayerService(em);
            PlayerDto playerDto = playerService.getPlayerDtoById(id);
            logger.debug("Returning player DTO for id={}", id);
            sendJson(resp, HttpServletResponse.SC_OK, playerDto);
        } catch (ResourceNotFoundException ex) {
            logger.warn("Player not found: id={}", id);
            sendJson(resp, HttpServletResponse.SC_NOT_FOUND, Map.of(
                    "code", "PLAYER_NOT_FOUND",
                    "message", ex.getMessage()
            ));
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Incoming request: method=POST, uri={}, remoteIp={}, contentType={}", req.getRequestURI(), req.getRemoteAddr(), req.getContentType());
        String firstName = req.getParameter("FirstName");
        String lastName = req.getParameter("LastName");
        NewPlayerValidator validator = new NewPlayerValidator();
        try (EntityManager em = emf.createEntityManager()) {
            PlayerService playerService = new PlayerService(em);
            validator.validate(firstName, lastName);
            firstName = firstName.trim();
            lastName = lastName.trim();
            Player player = playerService.createPlayer(firstName, lastName);
            logger.info("Player created: id={}, firstName='{}', lastName='{}'", player.getId(), firstName, lastName);
            sendJson(resp, HttpServletResponse.SC_CREATED, Map.of(
                    "message", "Player created",
                    "playerId", player.getId()
            ));


        } catch (ResourceAlreadyExistsException raee) {
            logger.warn("Duplicate player attempted: firstName='{}', lastName='{}'", firstName, lastName);
            sendJson(resp, HttpServletResponse.SC_CONFLICT, Map.of(
                    "code", "DUPLICATE_PLAYER",
                    "message", "Player with this first name and last name already exists",
                    "firstName", firstName,
                    "lastName", lastName
            ));

        } catch (IllegalArgumentException e) {
            logger.warn("Validation error for POST /player: firstName='{}', lastName='{}', msg={}", firstName, lastName, e.getMessage());
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of(
                    "code", "Validation Issue",
                    "message", e.getMessage()
            ));

        } catch (Exception e) {
            logger.error(
                    "Unexpected error while processing POST /player: firstName='{}', lastName='{}'",
                    firstName,
                    lastName,
                    e
            );

            sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Map.of(
                    "code", "INTERNAL_SERVER_ERROR",
                    "message", "An error occurred while processing the POST request"
            ));
        }


    }
}
