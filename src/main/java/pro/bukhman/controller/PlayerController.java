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
        logger.info("Received GET request to /player");
        String idParam = req.getParameter("id");
        Long id;
        try {
            id = Long.parseLong(idParam);
        } catch (Exception e) {
            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of(
                    "code", "INVALID_ID",
                    "message", "Query parameter 'id' must be a valid number"
            ));
            return;
        }

        try (EntityManager em = emf.createEntityManager()) {
            PlayerService playerService = new PlayerService(em);
            Player player = playerService.getPlayerById(id);
            sendJson(resp, HttpServletResponse.SC_OK, player);
        } catch (ResourceNotFoundException ex) {
            sendJson(resp, HttpServletResponse.SC_NOT_FOUND, Map.of(
                    "code", "PLAYER_NOT_FOUND",
                    "message", ex.getMessage()
            ));
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Received POST request to /player");
        String firstName = req.getParameter("FirstName");
        String lastName = req.getParameter("LastName");
        NewPlayerValidator validator = new NewPlayerValidator();
        try (EntityManager em = emf.createEntityManager()) {
            PlayerService playerService = new PlayerService(em);
            validator.validate(firstName, lastName);
            firstName = firstName.trim();
            lastName = lastName.trim();
            Player player = playerService.createPlayer(firstName, lastName);

            sendJson(resp, HttpServletResponse.SC_CREATED, Map.of(
                    "message", "Player created",
                    "playerId", player.getId()
            ));


        } catch (ResourceAlreadyExistsException raee) {
            logger.error("Error while processing POST /player: firstName='{}', lastName='{}',cause='{}'", firstName, lastName, raee.getCause());
            if (raee.getCause() instanceof ConstraintViolationException) {
                sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of(
                        "code", "DUPLICATE_PLAYER",
                        "message", "Player with this first name and last name already exists",
                        "firstName", firstName,
                        "lastName", lastName
                ));
            }
        } catch (IllegalArgumentException e) {
            logger.error(
                    "Error while processing POST /player: firstName='{}', lastName='{}'",
                    firstName,
                    lastName,
                    e
            );

            sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of(
                    "code", "Validation Issue",
                    "message", e.getMessage()
            ));

        } catch (Exception e) {
            logger.error(
                    "Error while processing POST /player: firstName='{}', lastName='{}'",
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
