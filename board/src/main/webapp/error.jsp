<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String errorMessage = (String) request.getAttribute("errorMessage");
	request.setAttribute("errorMessage", errorMessage);
%>

<html>
<head>
    <title>작업 실패 페이지</title>
</head>
<body>
    <script>
            alert("${errorMessage}");
            window.location.href = "main"; 
    </script>
</body>
</html>
