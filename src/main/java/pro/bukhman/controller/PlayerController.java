package pro.bukhman.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.bukhman.model.entity.Player;
import pro.bukhman.service.PlayerService;

import java.io.IOException;

@WebServlet(name = "PlayerController", urlPatterns = {"/player"})
public class PlayerController extends BasicServlet {
    private static final Logger logger = LogManager.getLogger(PlayerController.class);
    private PlayerService playerService;


    @Override
    public void init() {
        super.init();
        logger.info("Initializing PlayerController");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Received GET request to /player");

        playerService = new PlayerService(emf.createEntityManager());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Received POST request to /player");
        try {
            playerService = new PlayerService(emf.createEntityManager());
            Player player = playerService.createPlayer(req.getParameter("FirstName"), req.getParameter("LastName"));
            resp.sendRedirect("/player");
        } catch (Exception e) {
            logger.error("Error while processing POST /player: firstName='{}', lastName='{}'", req.getParameter("FirstName"), req.getParameter("LastName"), e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the POST request");
        }
    }
}
