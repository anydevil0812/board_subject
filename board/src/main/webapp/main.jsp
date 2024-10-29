<%@page import="dao.BoardDAO"%>
<%@page import="util.DBUtil"%>
<%@page import="java.sql.Connection"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.BoardDTO" %>

<%

	Connection conn = DBUtil.connect();
	BoardDTO dto = new BoardDTO();
	BoardDAO dao = new BoardDAO();

%>
<html>
<head>
    <title>Subject1-3</title>
    <link rel="stylesheet" href="./css/1.css"/> 
</head>
<body>
    <h2>게시판</h2>
    <div class="board-title">
        <dl>
            <dt id="number">번호</dt>
            <dt id="title">제목</dt>
            <dt id="writer">작성자</dt>
            <dt id="date">작성일</dt>
            <dt id="views">조회수</dt>
        </dl>
    </div>
    <div class="board-list">
    	<c:forEach var="dto" items="${lists}">
	        <dl>
	            <dd>${dto.num}</dd>
	            <dd>
	            	<a href="${articleUrl}&num=${dto.num}">
                    	${dto.subject}
               		</a>
	            </dd>
	            <dd>${dto.name}</dd>
	            <dd>${dto.date}</dd>
	            <dd>${dto.count}</dd>
	        </dl>
        </c:forEach>
    </div>
    <c:forEach var="post" items="${posts}">
        <div class="forward">
            <div class="post-title">${post.title}</div>
            <div>${post.content}</div>
            <div>작성자: ${post.username}</div>
        </div>
    </c:forEach>
    <form action="" method="post" class="form-container">
	   <span>
	       <select name="search-category">
	           <option>제목</option>
	           <option>작성자</option>
	           <option>내용</option>
	       </select>
	   </span>
	   <span>
	       <input type="text" name="search-text">
	   </span>
	   <span>
	       <button name="search-button">검색</button>
	   </span>
	</form>
</body>
</html>
