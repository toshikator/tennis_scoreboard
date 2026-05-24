package pro.bukhman.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pro.bukhman.exception.MatchIsAlreadyFinishedException;
import pro.bukhman.exception.MatchIsNotFinishedYetException;
import pro.bukhman.model.entity.OngoingMatchSnapshot;
import pro.bukhman.model.entity.Player;

import static org.junit.jupiter.api.Assertions.*;

class OngoingMatchTest {
    static OngoingMatch ongoingMatch;
    OngoingMatchSnapshot ongoingMatchSnapshot;

    @Test
    void getSnapshot() {
        assertEquals("Sergey", ongoingMatchSnapshot.getPlayer1().getFirstName());
        assertEquals("Conan", ongoingMatchSnapshot.getPlayer2().getFirstName());
        assertEquals("Zhukov", ongoingMatchSnapshot.getPlayer1().getLastName());
        assertEquals("Barbarian", ongoingMatchSnapshot.getPlayer2().getLastName());
    }

    @Test
    void addPointPlayer1() {
        addPointToPlayer(ongoingMatchSnapshot.getPlayer1());
        ongoingMatchSnapshot = ongoingMatch.getSnapshot();
        assertEquals(1, ongoingMatchSnapshot.getPlayer1Points());
    }

    void addPointToPlayer(Player player) {
        ongoingMatch.addPoint(player);
    }

    @Test
    void addPointPlayer2() {
        addPointToPlayer(ongoingMatchSnapshot.getPlayer2());
        ongoingMatchSnapshot = ongoingMatch.getSnapshot();
        assertEquals(1, ongoingMatchSnapshot.getPlayer2Points());
    }

    @Test
    void addGamePlayer1() {
        setUp();
        addGameToPlayer(ongoingMatchSnapshot.getPlayer1());
        setUpEach();
        assertEquals(1, ongoingMatchSnapshot.getPlayer1Games());
    }

    @BeforeAll
    static void setUp() {
        Player player1 = new Player();
        player1.setId(1L);
        player1.setFirstName("Sergey");
        player1.setLastName("Zhukov");
        Player player2 = new Player();
        player2.setId(2L);
        player2.setFirstName("Conan");
        player2.setLastName("Barbarian");
        ongoingMatch = new OngoingMatch(player1, player2);
    }

    void addGameToPlayer(Player player) {
        for (int i = 0; i < 4; i++) {
            addPointToPlayer(player);
        }
    }

    @BeforeEach
    void setUpEach() {
        ongoingMatchSnapshot = ongoingMatch.getSnapshot();
    }

    @Test
    void addGamePlayer2() {
        setUp();
        addGameToPlayer(ongoingMatchSnapshot.getPlayer2());
        setUpEach();
        assertEquals(1, ongoingMatchSnapshot.getPlayer2Games());
    }

    @Test
    void addSetPlayer1() {
        setUp();
        addSetToPlayer(ongoingMatchSnapshot.getPlayer1());
        setUpEach();
        assertEquals(0, ongoingMatchSnapshot.getPlayer1Points());
        assertEquals(0, ongoingMatchSnapshot.getPlayer1Games());
        assertEquals(1, ongoingMatchSnapshot.getPlayer1Sets());

    }

    void addSetToPlayer(Player player) {
        for (int i = 0; i < 6; i++) {
            addGameToPlayer(player);
        }
    }

    @Test
    void addSetPlayer2() {
        setUp();
        addSetToPlayer(ongoingMatchSnapshot.getPlayer2());
        setUpEach();
        assertEquals(0, ongoingMatchSnapshot.getPlayer2Points());
        assertEquals(0, ongoingMatchSnapshot.getPlayer2Games());
        assertEquals(1, ongoingMatchSnapshot.getPlayer2Sets());

    }

    @Test
    void checkTieBreakStart() {
        setUp();
        for (int i = 0; i < 6; i++) {
            addGameToPlayer(ongoingMatchSnapshot.getPlayer1());

            addGameToPlayer(ongoingMatchSnapshot.getPlayer2());
        }
        setUpEach();
        assertTrue(ongoingMatchSnapshot.isTieBreak());
    }

    @Test
    void checkTieBreakMechanicSameScore() {
        int limit = 8;
        setUp();
        for (int i = 0; i < 6; i++) {
            addGameToPlayer(ongoingMatchSnapshot.getPlayer1());
            addGameToPlayer(ongoingMatchSnapshot.getPlayer2());
        }
        setUpEach();
        assertTrue(ongoingMatchSnapshot.isTieBreak());
        for (int i = 0; i < limit; i++) {
            addPointToPlayer(ongoingMatchSnapshot.getPlayer1());
            addPointToPlayer(ongoingMatchSnapshot.getPlayer2());
        }
        setUpEach();
        assertTrue(ongoingMatchSnapshot.isTieBreak());
        assertEquals(limit, ongoingMatchSnapshot.getPlayer2Points());
        assertEquals(ongoingMatchSnapshot.getPlayer2Points(), ongoingMatchSnapshot.getPlayer1Points());
    }

    @Test
    void checkTieBreakMechanicPlayer1WinSet() {
        int limit = 8;
        setUp();
        for (int i = 0; i < 6; i++) {
            addGameToPlayer(ongoingMatchSnapshot.getPlayer1());
            addGameToPlayer(ongoingMatchSnapshot.getPlayer2());
        }
        setUpEach();
        assertTrue(ongoingMatchSnapshot.isTieBreak());
        for (int i = 0; i < limit; i++) {
            addPointToPlayer(ongoingMatchSnapshot.getPlayer1());
            addPointToPlayer(ongoingMatchSnapshot.getPlayer2());
        }
        addPointToPlayer(ongoingMatchSnapshot.getPlayer1());
        addPointToPlayer(ongoingMatchSnapshot.getPlayer1());
        setUpEach();
        assertFalse(ongoingMatchSnapshot.isTieBreak());
        //        System.out.println(ongoingMatchSnapshot);
    }

    @Test
    void checkTieBreakMechanicPlayer1WinMatch() {
        int limit = 8;
        setUp();
        for (int i = 0; i < 6; i++) {
            addGameToPlayer(ongoingMatchSnapshot.getPlayer1());
            addGameToPlayer(ongoingMatchSnapshot.getPlayer2());
        }
        setUpEach();
        assertTrue(ongoingMatchSnapshot.isTieBreak());
        for (int i = 0; i < limit; i++) {
            addPointToPlayer(ongoingMatchSnapshot.getPlayer1());
            addPointToPlayer(ongoingMatchSnapshot.getPlayer2());
        }
        addPointToPlayer(ongoingMatchSnapshot.getPlayer1());
        addPointToPlayer(ongoingMatchSnapshot.getPlayer1());
        setUpEach();
        assertFalse(ongoingMatchSnapshot.isTieBreak());
        for (int i = 0; i < 6; i++) {
            addGameToPlayer(ongoingMatchSnapshot.getPlayer1());
            addGameToPlayer(ongoingMatchSnapshot.getPlayer2());
        }
        setUpEach();
        assertTrue(ongoingMatchSnapshot.isTieBreak());
        for (int i = 0; i < limit; i++) {
            addPointToPlayer(ongoingMatchSnapshot.getPlayer1());
            addPointToPlayer(ongoingMatchSnapshot.getPlayer2());
        }
        addPointToPlayer(ongoingMatchSnapshot.getPlayer1());
        addPointToPlayer(ongoingMatchSnapshot.getPlayer1());
        setUpEach();

        //        System.out.println(ongoingMatchSnapshot);
    }

    @Test
    void checkTieBreakMechanicPlayer2WinMatch() {
        int limit = 8;
        setUp();
        for (int i = 0; i < 6; i++) {
            addGameToPlayer(ongoingMatchSnapshot.getPlayer1());
            addGameToPlayer(ongoingMatchSnapshot.getPlayer2());
        }
        setUpEach();
        assertTrue(ongoingMatchSnapshot.isTieBreak());
        for (int i = 0; i < limit; i++) {
            addPointToPlayer(ongoingMatchSnapshot.getPlayer1());
            addPointToPlayer(ongoingMatchSnapshot.getPlayer2());
        }
        addPointToPlayer(ongoingMatchSnapshot.getPlayer2());
        addPointToPlayer(ongoingMatchSnapshot.getPlayer2());
        setUpEach();
        assertFalse(ongoingMatchSnapshot.isTieBreak());
        for (int i = 0; i < 6; i++) {
            addGameToPlayer(ongoingMatchSnapshot.getPlayer1());
            addGameToPlayer(ongoingMatchSnapshot.getPlayer2());
        }
        setUpEach();
        assertTrue(ongoingMatchSnapshot.isTieBreak());
        for (int i = 0; i < limit; i++) {
            addPointToPlayer(ongoingMatchSnapshot.getPlayer1());
            addPointToPlayer(ongoingMatchSnapshot.getPlayer2());
        }
        addPointToPlayer(ongoingMatchSnapshot.getPlayer2());
        addPointToPlayer(ongoingMatchSnapshot.getPlayer2());
        setUpEach();

        //        System.out.println(ongoingMatchSnapshot);
    }

    @Test
    void checkMechanicPlayer1WinMatch() {
        setUp();
        for (int i = 0; i < 6; i++) {
            addGameToPlayer(ongoingMatchSnapshot.getPlayer1());
            //            addGameToPlayer(ongoingMatchSnapshot.getPlayer2());
        }
        setUpEach();
        //        assertTrue(ongoingMatchSnapshot.isTieBreak());
        for (int i = 0; i < 6; i++) {
            addGameToPlayer(ongoingMatchSnapshot.getPlayer1());
            //            addPointToPlayer(ongoingMatchSnapshot.getPlayer2());
        }
        setUpEach();
        System.out.println(ongoingMatchSnapshot);
    }

    @Test
    void shouldThrowExceptionWhenAddingPointAfterMatchFinished() {
        setUp();
        addSetToPlayer(ongoingMatchSnapshot.getPlayer1());
        addSetToPlayer(ongoingMatchSnapshot.getPlayer1());

        assertThrows(
                MatchIsAlreadyFinishedException.class,
                () -> ongoingMatch.addPoint(ongoingMatchSnapshot.getPlayer1())
        );
    }

    @Test
    void shouldThrowExceptionWhenGettingWinnerBeforeMatchFinished() {
        setUp();
        assertThrows(
                MatchIsNotFinishedYetException.class,
                () -> ongoingMatch.getWinner()
        );
    }

    @Test
    void newMatchShouldStartWithZeroScore() {
        setUp();
        setUpEach();
        assertEquals(0, ongoingMatchSnapshot.getPlayer1Points());
        assertEquals(0, ongoingMatchSnapshot.getPlayer2Points());
        assertEquals(0, ongoingMatchSnapshot.getPlayer1Games());
        assertEquals(0, ongoingMatchSnapshot.getPlayer2Games());
        assertEquals(0, ongoingMatchSnapshot.getPlayer1Sets());
        assertEquals(0, ongoingMatchSnapshot.getPlayer2Sets());
        assertFalse(ongoingMatchSnapshot.isTieBreak());
        assertFalse(ongoingMatchSnapshot.isFinished());
        assertNull(ongoingMatchSnapshot.getWinner());
    }
}