<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tennis Scoreboard</title>
    <link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page-index">
<%@ include file="/common/header.jsp" %>

<main>
    <h1>Tennis Scoreboard</h1>

    <div class="player-container">
        <h2>Navigation</h2>
        <ul class="player-items">
            <li class="player-item">
                <a href="${pageContext.request.contextPath}/players.jsp"><span class="player-name">Players</span></a>
            </li>
            <li class="player-item">
                <a href="${pageContext.request.contextPath}/new-match.jsp"><span class="player-name">New Match</span></a>
            </li>
            <li class="player-item">
                <a href="${pageContext.request.contextPath}/current-match.jsp"><span class="player-name">Current Match</span></a>
            </li>
            <li class="player-item">
                <a href="${pageContext.request.contextPath}/completed-matches.jsp"><span class="player-name">Completed Matches</span></a>
            </li>
            <li class="player-item">
                <a href="${pageContext.request.contextPath}/api/hello-servlet"><span class="player-name">Hello Servlet</span></a>
            </li>
        </ul>
    </div>
</main>

<%@ include file="/common/footer.jsp" %>
</body>
</html>