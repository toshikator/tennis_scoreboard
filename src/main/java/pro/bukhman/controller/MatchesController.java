package pro.bukhman.controller;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.bukhman.model.dto.MatchDto;
import pro.bukhman.model.dto.ResponsePaginationDto;
import pro.bukhman.model.entity.Match;
import pro.bukhman.service.MatchesService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "MatchesController", urlPatterns = {"/matches"})
public class MatchesController extends BasicServlet {
    private static final Logger logger = LogManager.getLogger(PlayersController.class);

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("Initializing PlayersController");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("id") != null) {
            logger.info("Received GET request to /matches with id parameter: id={}", req.getParameter("id"));
            try (EntityManager em = emf.createEntityManager()) {
                Long id = Long.parseLong(req.getParameter("id"));
                MatchesService matchesService = new MatchesService(em);
                Match match = matchesService.getMatchById(id);
                MatchDto matchDto = matchesService.getMatchDtoById(id);
                sendJson(resp, HttpServletResponse.SC_OK, matchDto);
            } catch (Exception e) {
                sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Map.of("message", "Internal server error: " + e.getMessage(),
                        "stackTrace", e.getStackTrace().toString().replace("\n", "")));
            }

        } else if (req.getParameter("firstName") != null) {
            try (EntityManager em = emf.createEntityManager()) {
                String firstName = req.getParameter("firstName");
                logger.info("Received GET request to /matches with firstName parameter: firstName={}", req.getParameter(firstName));
                MatchesService matchesService = new MatchesService(em);
//                List<Match> matchList = matchesService.getMatchesByPlayerFirstname(firstName);
                List<MatchDto> matchDtos = matchesService.getMatchesByPlayerFirstnameDto(firstName);

            } catch (Exception e) {
                sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        Map.of("message", "Internal server error: " + e.getMessage(),
                                "stackTrace", e.getStackTrace().toString().replace("\n", "")));
            }
        } else if (req.getParameter("lastName") != null) {
            try (EntityManager em = emf.createEntityManager()) {
                String lastName = req.getParameter("lastName");
                logger.info("Received GET request to /matches with lastName parameter: lastName={}", req.getParameter(lastName));
                MatchesService matchesService = new MatchesService(em);
//                List<Match> matchList = matchesService.getMatchesByPlayerFirstname(firstName);
                List<MatchDto> matchDtos = matchesService.getMatchesByPlayerLastnameDto(lastName);

            } catch (Exception e) {
                sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        Map.of("message", "Internal server error: " + e.getMessage(),
                                "stackTrace", e.getStackTrace().toString().replace("\n", "")));
            }
        } else if (req.getParameter("limit") != null && req.getParameter("offset") != null) {
            try (EntityManager em = emf.createEntityManager()) {
                Integer limit = Integer.parseInt(req.getParameter("limit"));
                Integer offset = Integer.parseInt(req.getParameter("offset"));
                logger.info("Received GET request to /matches with limit and offset parameters: limit={}, offset={}", limit, offset);
                MatchesService matchesService = new MatchesService(em);
                ResponsePaginationDto<MatchDto> matches = matchesService.getMatchesPagination(limit, offset);
                sendJson(resp, HttpServletResponse.SC_OK, matches);

            } catch (Exception e) {
                sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Map.of("message", "Internal server error: " + e.getMessage(),
                        "stackTrace", e.getStackTrace().toString().replace("\n", "")));
            }
        } else {
            logger.info("Received GET request to /matches without parameters");
            try (EntityManager em = emf.createEntityManager()) {
                MatchesService matchesService = new MatchesService(em);
                List<MatchDto> matches = matchesService.getAllMatchesDto();
                sendJson(resp, HttpServletResponse.SC_OK, matches);
            }
        }
    }
}
