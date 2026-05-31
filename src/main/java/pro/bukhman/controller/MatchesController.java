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
    private static final Logger logger = LogManager.getLogger(MatchesController.class);

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("Initializing MatchesController");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Incoming request: method=GET, uri={}, query={}, remoteIp={}", req.getRequestURI(), req.getQueryString(), req.getRemoteAddr());
        if (req.getParameter("id") != null) {
            String idParam = req.getParameter("id");
            logger.info("/matches by id: id={}", idParam);
            try (EntityManager em = emf.createEntityManager()) {
                Long id = Long.parseLong(idParam);
                MatchesService matchesService = new MatchesService(em);
                Match match = matchesService.getMatchById(id);
                MatchDto matchDto = matchesService.getMatchDtoById(id);
                sendJson(resp, HttpServletResponse.SC_OK, matchDto);
            } catch (Exception e) {
                logger.error("Error getting match by id: id={}, cause=", idParam, e);
                sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Map.of("message", "Internal server error: " + e.getMessage()));
            }
        } else if (req.getParameter("firstName") != null && req.getParameter("lastName") != null) {
            try (EntityManager em = emf.createEntityManager()) {
                String firstName = req.getParameter("firstName");
                String lastName = req.getParameter("lastName");
                logger.info("/matches by firstName and lastName: firstName={}, lastName={}", firstName, lastName);
                MatchesService matchesService = new MatchesService(em);
                List<MatchDto> matchDtos = matchesService.getMatchesByPlayerFullNameDto(lastName, firstName);
                sendJson(resp, HttpServletResponse.SC_OK, matchDtos);
            }
        } else if (req.getParameter("firstName") != null) {
            try (EntityManager em = emf.createEntityManager()) {
                String firstName = req.getParameter("firstName");
                logger.info("/matches by firstName: firstName={}", firstName);
                MatchesService matchesService = new MatchesService(em);
                List<MatchDto> matchDtos = matchesService.getMatchesByPlayerFirstnameDto(firstName);
                sendJson(resp, HttpServletResponse.SC_OK, matchDtos);

            } catch (Exception e) {
                logger.error("Error getting matches by firstName, cause=", e);
                sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        Map.of("message", "Internal server error: " + e.getMessage(),
                                "stackTrace", e.getStackTrace().toString().replace("\n", "")));
            }
        } else if (req.getParameter("lastName") != null) {
            try (EntityManager em = emf.createEntityManager()) {
                String lastName = req.getParameter("lastName");
                logger.info("/matches by lastName: lastName={}", lastName);
                MatchesService matchesService = new MatchesService(em);
                List<MatchDto> matchDtos = matchesService.getMatchesByPlayerLastnameDto(lastName);
                sendJson(resp, HttpServletResponse.SC_OK, matchDtos);

            } catch (Exception e) {
                logger.error("Error getting matches by lastName, cause=", e);
                sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        Map.of("message", "Internal server error: " + e.getMessage()));
            }
        } else if (req.getParameter("limit") != null && req.getParameter("offset") != null) {
            try (EntityManager em = emf.createEntityManager()) {
                Integer limit = Integer.parseInt(req.getParameter("limit"));
                Integer offset = Integer.parseInt(req.getParameter("offset"));
                logger.info("/matches pagination: limit={}, offset={}", limit, offset);
                MatchesService matchesService = new MatchesService(em);
                ResponsePaginationDto<MatchDto> matches = matchesService.getMatchesPagination(limit, offset);
                sendJson(resp, HttpServletResponse.SC_OK, matches);

            } catch (Exception e) {
                logger.error("Error getting matches with pagination, cause=", e);
                sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Map.of("message", "Internal server error: " + e.getMessage()));
            }
        } else {
            logger.info("/matches list all");
            try (EntityManager em = emf.createEntityManager()) {
                MatchesService matchesService = new MatchesService(em);
                List<MatchDto> matches = matchesService.getAllMatchesDto();
                sendJson(resp, HttpServletResponse.SC_OK, matches);
            }
        }
    }
}
