package pro.bukhman.controller;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.bukhman.model.dto.PaginationDto;
import pro.bukhman.model.dto.PlayerDto;
import pro.bukhman.model.dto.ResponsePaginationDto;
import pro.bukhman.service.PlayerService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "PlayersController", urlPatterns = {"/players"})
public class PlayersController extends BasicServlet {
    private static final Logger logger = LogManager.getLogger(PlayersController.class);


    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("Initializing PlayersController");
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("Incoming request: method=GET, uri={}, query={}, remoteIp={}", req.getRequestURI(), req.getQueryString(), req.getRemoteAddr());

        if (req.getParameter("firstName") == null
                && req.getParameter("lastName") == null
                && req.getParameter("limit") == null
                && req.getParameter("offset") == null) {
            try (EntityManager em = emf.createEntityManager()) {
                PlayerService playerService = new PlayerService(em);
                logger.info("/players list all");
                List<PlayerDto> players = playerService.getAllPlayersDto();
                sendJson(resp, HttpServletResponse.SC_OK, players);
            } catch (Exception e) {
                logger.error("Error fetching all players, cause=", e);
                sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        Map.of("message", "Internal server error: " + e.getMessage(),
                                "stackTrace", e.getStackTrace().toString().replace("\n", "")));
            }
        } else if (req.getParameter("firstName") != null && req.getParameter("lastName") == null) {
            String firstName = req.getParameter("firstName");
            logger.info("/players by firstName: firstName={}", firstName);
            try (EntityManager em = emf.createEntityManager()) {
                PlayerService playerService = new PlayerService(em);
                List<PlayerDto> players = playerService.getPlayersByFirstname(firstName);
                sendJson(resp, HttpServletResponse.SC_OK, players);
            } catch (Exception e) {
                logger.error("Error fetching players by firstName, cause=", e);
                sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        Map.of("message", "Internal server error: " + e.getMessage(),
                                "stackTrace", e.getStackTrace().toString().replace("\n", "")));
            }

        } else if (req.getParameter("firstName") == null && req.getParameter("lastName") != null) {
            String lastName = req.getParameter("lastName");
            logger.info("/players by lastName: lastName={}", lastName);
            try (EntityManager em = emf.createEntityManager()) {
                PlayerService playerService = new PlayerService(em);
                List<PlayerDto> players = playerService.getPlayersByLastname(lastName);
                sendJson(resp, HttpServletResponse.SC_OK, players);
            } catch (Exception e) {
                logger.error("Error fetching players by lastName, cause=", e);
                sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        Map.of("message", "Internal server error: " + e.getMessage(),
                                "stackTrace", e.getStackTrace().toString().replace("\n", "")));
            }
        } else if (req.getParameter("limit") != null && req.getParameter("offset") != null) {
            Integer limit = Integer.parseInt(req.getParameter("limit"));
            Integer offset = Integer.parseInt(req.getParameter("offset"));
            logger.info("/players pagination: limit={}, offset={}", limit, offset);
            try (EntityManager em = emf.createEntityManager()) {
                PlayerService playerService = new PlayerService(em);
                ResponsePaginationDto<PlayerDto> players = playerService.getPlayersPagination(limit, offset);
                sendJson(resp, HttpServletResponse.SC_OK, players);
            } catch (Exception e) {
                logger.error("Error fetching players with pagination, cause=", e);
                sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        Map.of("message", "Internal server error: " + e.getMessage(),
                                "stackTrace", e.getStackTrace().toString().replace("\n", "")));
            }
        }
    }
}
