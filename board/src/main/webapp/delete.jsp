<%@page import="dao.BoardDAO"%>
<%@page import="dto.BoardDTO"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    int num = (int) request.getAttribute("num");
    
    BoardDAO dao = new BoardDAO();
    int result = dao.deleteData(num);
%>

<html>
<head>
    <title>게시글 삭제</title>
</head>
<body>
    <script>
        <% if (result > 0) { %>
            alert("게시글 삭제 완료");
            window.location.href = "main"; // 성공 시 메인 페이지로 이동
        <% } else { %>
            alert("게시글 삭제에 실패했습니다. 다시 시도해 주세요.");
            history.back(); // 실패 시 이전 페이지로 이동
        <% } %>
    </script>
</body>
</html>
