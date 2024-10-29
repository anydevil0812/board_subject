<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="dao.BoardDAO"%>
<%@page import="dto.BoardDTO"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%

	String numParam = request.getParameter("num"); 
	int num = Integer.parseInt(numParam); 
	
	BoardDAO dao = new BoardDAO();
	BoardDTO dto = dao.getReadData(num);
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

%>
<!DOCTYPE html>
<html>
<head>
	<title>게시글 조회</title>
</head>
<body>
	<h3>게시판</h3>
	<div>
		<div><c:out value="${dto.title}"/></div>
		<div>
			<table>
				<tr>
					<td>순번</td>
					<td>등록일</td>
				</tr>
				<tr>
					<td><c:out value="${dto.num}"/></td>
					<td><c:out value="${dto.date.format(formatter)}"/></td>
				</tr>
				<tr>
					<td>작성자</td>
					<td>조회수</td>
				</tr>
				<tr>
					<td><c:out value="${dto.name}"/></td>
					<td><c:out value="${dto.views}"/></td>
				</tr>
			</table>
		</div>
		<div>
			<c:out value="${dto.content}"/>
		</div>
	</div>
	<span>
		<button value="수정" onclick="location.href='edit.jsp?num=${dto.num}'" ></button>	
	</span>
	<span>
		<button value="삭제" onclick="location.href='delete.jsp?num=${dto.num}'"></button>	
	</span>
	<span>
		<button value="목록" onclick="location.href='main.jsp'"></button>	
	</span>
</body>
</html>