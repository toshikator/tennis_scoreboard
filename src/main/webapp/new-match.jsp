<%--
  Created by IntelliJ IDEA.
  User: toshik
  Date: 5/3/2026
  Time: 23:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>New Match</title>
    <link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page-new-match">
<%@ include file="/common/header.jsp" %>

<main>
    <h1>New Match</h1>

    <div class="form-card">
        <form action="${pageContext.request.contextPath}/api/new-match" method="post" class="form-styled">
            <div class="form-grid">
                <div class="form-field">
                    <label for="player1">Player 1</label>
                    <input type="text" id="player1" name="player1" placeholder="Enter first player name" required>
                </div>
                <div class="form-field">
                    <label for="player2">Player 2</label>
                    <input type="text" id="player2" name="player2" placeholder="Enter second player name" required>
                </div>
            </div>

            <button type="submit" class="btn-primary">Start match</button>
        </form>
    </div>
</main>

<%@ include file="/common/footer.jsp" %>
</body>
</html>
