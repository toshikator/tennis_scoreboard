package pro.bukhman.model.entity;

public class OngoingMatchSnapshot {
    private int GAMES_TO_WIN_SET;
    private int DEFAULT_POINTS_TO_WIN_GAME;
    private int DEFAULT_GAMES_TO_WIN_SET_DIFFERENCE;
    private int SETS_TO_WIN_MATCH;
    private Player player1;
    private Player player2;
    private int player1Points;
    private int player2Points;
    private int player1Sets;
    private int player2Sets;
    private int player1Games;
    private int player2Games;
    private boolean tieBreak;
    private boolean isFinished;
    private Player winner;
    private int pointsToWinGame;
    private int gamesToWinSetDifference;

    public OngoingMatchSnapshot() {
    }

    public int getGamesToWinSetDifference() {
        return gamesToWinSetDifference;
    }

    public void setGamesToWinSetDifference(int gamesToWinSetDifference) {
        this.gamesToWinSetDifference = gamesToWinSetDifference;
    }

    public int getPointsToWinGame() {
        return pointsToWinGame;
    }

    public void setPointsToWinGame(int pointsToWinGame) {
        this.pointsToWinGame = pointsToWinGame;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean isTieBreak() {
        return tieBreak;
    }

    public void setTieBreak(boolean tieBreak) {
        this.tieBreak = tieBreak;
    }

    public int getPlayer2Games() {
        return player2Games;
    }

    public void setPlayer2Games(int player2Games) {
        this.player2Games = player2Games;
    }

    public int getPlayer1Games() {
        return player1Games;
    }

    public void setPlayer1Games(int player1Games) {
        this.player1Games = player1Games;
    }

    public int getPlayer2Sets() {
        return player2Sets;
    }

    public void setPlayer2Sets(int player2Sets) {
        this.player2Sets = player2Sets;
    }

    public int getPlayer1Sets() {
        return player1Sets;
    }

    public void setPlayer1Sets(int player1Sets) {
        this.player1Sets = player1Sets;
    }

    public int getPlayer2Points() {
        return player2Points;
    }

    public void setPlayer2Points(int player2Points) {
        this.player2Points = player2Points;
    }

    public int getPlayer1Points() {
        return player1Points;
    }

    public void setPlayer1Points(int player1Points) {
        this.player1Points = player1Points;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public int getSETS_TO_WIN_MATCH() {
        return SETS_TO_WIN_MATCH;
    }

    public void setSETS_TO_WIN_MATCH(int SETS_TO_WIN_MATCH) {
        this.SETS_TO_WIN_MATCH = SETS_TO_WIN_MATCH;
    }

    public int getDEFAULT_GAMES_TO_WIN_SET_DIFFERENCE() {
        return DEFAULT_GAMES_TO_WIN_SET_DIFFERENCE;
    }

    public void setDEFAULT_GAMES_TO_WIN_SET_DIFFERENCE(int DEFAULT_GAMES_TO_WIN_SET_DIFFERENCE) {
        this.DEFAULT_GAMES_TO_WIN_SET_DIFFERENCE = DEFAULT_GAMES_TO_WIN_SET_DIFFERENCE;
    }

    public int getDEFAULT_POINTS_TO_WIN_GAME() {
        return DEFAULT_POINTS_TO_WIN_GAME;
    }

    public void setDEFAULT_POINTS_TO_WIN_GAME(int DEFAULT_POINTS_TO_WIN_GAME) {
        this.DEFAULT_POINTS_TO_WIN_GAME = DEFAULT_POINTS_TO_WIN_GAME;
    }

    public int getGAMES_TO_WIN_SET() {
        return GAMES_TO_WIN_SET;
    }

    public void setGAMES_TO_WIN_SET(int GAMES_TO_WIN_SET) {
        this.GAMES_TO_WIN_SET = GAMES_TO_WIN_SET;
    }

    @Override
    public String toString() {
        return "OngoingMatchSnapshot{" + "\n" +
                "--GAMES_TO_WIN_SET=" + GAMES_TO_WIN_SET + "\n" +
                "--DEFAULT_POINTS_TO_WIN_GAME=" + DEFAULT_POINTS_TO_WIN_GAME + "\n" +
                "--DEFAULT_GAMES_TO_WIN_SET_DIFFERENCE=" + DEFAULT_GAMES_TO_WIN_SET_DIFFERENCE + "\n" +
                "--SETS_TO_WIN_MATCH=" + SETS_TO_WIN_MATCH + "\n" +
                "--player1=" + player1 + "\n" +
                "--player2=" + player2 + "\n" +
                "--player1Points=" + player1Points + "\n" +
                "--player2Points=" + player2Points + "\n" +
                "--player1Sets=" + player1Sets + "\n" +
                "--player2Sets=" + player2Sets + "\n" +
                "--player1Games=" + player1Games + "\n" +
                "--player2Games=" + player2Games + "\n" +
                "--tieBreak=" + tieBreak + "\n" +
                "--isFinished=" + isFinished + "\n" +
                "--winner=" + winner + "\n" +
                "--pointsToWinGame=" + pointsToWinGame + "\n" +
                "--gamesToWinSetDifference=" + gamesToWinSetDifference + "\n" +
                '}';
    }
}
