<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String successMessage = (String) request.getAttribute("successMessage");
	request.setAttribute("successMessage", successMessage);
%>

<html>
<head>
    <title>작업 성공 페이지</title>
</head>
<body>
    <script>
            alert("${successMessage}");
            window.location.href = "main";
    </script>
</body>
</html>
