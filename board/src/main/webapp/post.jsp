<%@page import="java.time.LocalDateTime"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="dao.BoardDAO"%>
<%@page import="dto.BoardDTO"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%

	String numParam = request.getParameter("num"); 
	int num = Integer.parseInt(numParam); 
	
	BoardDAO dao = new BoardDAO();
	dao.plusViewCount(num);
	
	BoardDTO dto = dao.getReadData(num);
	LocalDateTime time = dto.getDate();
	String t = time.toString();
	t = t.replace("T", " ");
	
	request.setAttribute("dto", dto);
	request.setAttribute("time", t);
	
%>
<!DOCTYPE html>
<html>
<head>
	<title>게시글 조회</title>
	<link rel="stylesheet" href="./css/post.css"/> 
</head>
<body>
	<div class="table-container">
		<table class="post-table">
			<tr>
				<th colspan="4"><c:out value="${dto.title}"/></th>				
			</tr>
			<tr>
				<td class="narrow1">순번</td>
				<td class="narrow2"><c:out value="${dto.num}"/></td>
				<td class="narrow1">등록일</td>
				<td class="narrow2"><c:out value="${time}"/></td>
			</tr>
			<tr>
				<td>작성자</td>
				<td><c:out value="${dto.name}"/></td>
				<td>조회수</td>
				<td><c:out value="${dto.views}"/></td>
			</tr>
			<tr id="content-area">
				<td colspan="4"><c:out value="${dto.content}"/></td>
			</tr>
			<tr>
				<td colspan="4">
					<span>
						<button onclick="location.href='edit.jsp?num=${dto.num}'" >수정</button>	
					</span>
					<span>
						<button onclick="location.href='delete.jsp?num=${dto.num}'">삭제</button>	
					</span>
					<span>
						<button onclick="location.href='main.jsp'">메인으로</button>	
					</span>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>