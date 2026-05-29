package pro.bukhman.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import pro.bukhman.model.OngoingMatch;

import java.time.LocalDate;

@Entity
@Table(name = "matches")
public class Match extends BasicEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player1_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_matches_player1"))
    private Player player1;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player2_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_matches_player2"))
    private Player player2;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "winner_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_matches_winner"))
    private Player winner;

    @CreationTimestamp
    @Column(name = "played_at", nullable = false, updatable = false)
    private LocalDate playedAt;

    public Match() {
    }

    public Match(Player player1, Player player2, Player winner) {
        this.player1 = player1;
        this.player2 = player2;
        this.winner = winner;
    }

//    public Match(OngoingMatch ongoingMatch) {
//        player1 = ongoingMatch.getPlayer1();
//        player2 = ongoingMatch.getPlayer2();
//        winner = ongoingMatch.getWinner();
//    }


    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public LocalDate getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(LocalDate playedAt) {
        this.playedAt = playedAt;
    }
}