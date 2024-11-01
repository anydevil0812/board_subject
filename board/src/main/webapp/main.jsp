<%@page import="java.util.ArrayList"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="dao.BoardDAO"%>
<%@page import="util.DBUtil"%>
<%@page import="java.sql.Connection"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.BoardDTO" %>


<html>
<head>
    <title>게시판 목록</title>
    <link rel="stylesheet" href="./css/main.css"/> 
</head>
<body>
    <h2>게시판</h2>
    <div class="board-list">
    	<table class="board-table">
		    <tr class="board-head"> 
		    	<th id="number">번호</th>
	            <th id="title">제목</th>
	            <th id="writer">작성자</th>
	            <th id="date">작성일</th>
	            <th id="views">조회수</th>
		    </tr>
	    	<c:forEach var="dto" items="${lists}" varStatus="status">
		    	<tr class="board-content">
		            <td>${dto.num}</td>
		            <td>
		            	<a href="main?action=post&num=${dto.num}">
	                    	${dto.title}
	               		</a>
		            </td>
		            <td>${dto.name}</td>
		            <td>${time[status.index]}</td>
		            <td>${dto.views}</td>
	            </tr>
        	</c:forEach>
        </table>
    </div>
    <div class="pagination">
	    <c:if test="${startPage > 1}">
	        <a href="main?action=main&page=${startPage - 1}">이전</a>
	    </c:if>
	    <c:forEach begin="${startPage}" end="${endPage}" var="i">
	        <a href="main?action=main&page=${i}" class="${i == currentPage ? 'active' : ''}">${i}</a>
	    </c:forEach>
	    <c:if test="${endPage < totalPages}">
	        <a href="main?action=main&page=${endPage + 1}">다음</a>
	    </c:if>
	</div>
    <form action="main" method="get" class="form-container2">
	    <input type="hidden" name="action" value="write">
	    <button name="post-content" id="post-button">글쓰기</button>
	</form>
    <form action="main" method="get" class="form-container">
       <input type="hidden" name="action" value="search">
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
