<%@page import="dao.BoardDAO"%>
<%@page import="dto.BoardDTO"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    int postNum = (int) request.getAttribute("post_num");
    
    BoardDAO dao = new BoardDAO();
    int result = dao.deleteData(postNum);
%>

<html>
<head>
    <title>게시글 삭제</title>
</head>
<body>
    <script>
        <% if (result > 0) { %>
            alert("게시글 삭제 완료");
            window.location.href = "main"; 
        <% } else { %>
            alert("게시글 삭제에 실패했습니다. 다시 시도해 주세요.");
            history.back();
        <% } %>
    </script>
</body>
</html>
