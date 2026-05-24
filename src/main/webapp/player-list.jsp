<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%
    List<String> players = new ArrayList<>(Arrays.asList(
        "Novak Djokovic", "Carlos Alcaraz", "Jannik Sinner", "Daniil Medvedev",
        "Rafael Nadal", "Roger Federer", "Andy Murray", "Stan Wawrinka",
        "Alexander Zverev", "Stefanos Tsitsipas", "Holger Rune", "Andrey Rublev",
        "Casper Ruud", "Dominic Thiem", "Nick Kyrgios", "Hubert Hurkacz",
        "Grigor Dimitrov", "Gael Monfils", "Frances Tiafoe", "Felix Auger-Aliassime"
    ));
    Collections.shuffle(players);
    List<String> randomSix = players.subList(0, Math.min(6, players.size()));
%>
<ul class="player-items">
<% for (String name : randomSix) { %>
    <li class="player-item"><span class="player-name"><%= name %></span></li>
<% } %>
</ul>
