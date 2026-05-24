<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Players</title>
    <link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page-players">

<%@ include file="/common/header.jsp" %>

<main>
    <h1>Players</h1>

    <div class="player-container">
        <h2>Players</h2>
        <div class="player-list">
            <%@ include file="/player-list.jsp" %>
        </div>
    </div>
</main>

<%@ include file="/common/footer.jsp" %>

</body>
</html>