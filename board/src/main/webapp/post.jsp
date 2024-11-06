<%@page import="java.util.List"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="dao.BoardDAO"%>
<%@page import="dto.BoardDTO"%>
<%@page import="dto.BoardFileDTO"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%

	String numParam = request.getParameter("post_num"); 
	int postNum = Integer.parseInt(numParam); 
	
	BoardDAO dao = new BoardDAO();
	dao.plusViewCount(postNum);
	
	BoardDTO dto = dao.getReadData(postNum);
	LocalDateTime time = dto.getDate();
	String t = time.toString();
	t = t.replace("T", " ");
	
	List<BoardFileDTO> files = dao.getReadAllFile(postNum);
	
	request.setAttribute("dto", dto);
	request.setAttribute("time", t);
	request.setAttribute("files", files);
	
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
				<td class="narrow2"><c:out value="${dto.postNum}"/></td>
				<td class="narrow1">등록일</td>
				<td class="narrow2"><c:out value="${time}"/></td>
			</tr>
			<tr>
				<td>작성자</td>
				<td><c:out value="${dto.name}"/></td>
				<td>조회수</td>
				<td><c:out value="${dto.views}"/></td>
			</tr>
			 <tr>
			 	<td>첨부파일</td>
                <td colspan="3">
                    <c:if test="${not empty files}">
                    	<c:forEach var="file" items="${files}">
                    		<a href="download.jsp?file_id=${file.fileId}">${file.fileName}</a>
                    	</c:forEach>
                    </c:if>
                </td>
            </tr>
			<tr id="content-area">
				<td colspan="4"><c:out value="${dto.content}"/></td>
			</tr>
			<tr>
				<td colspan="4">
					<span>
						<button onclick="location.href='main?action=edit&post_num=${dto.postNum}'" >수정</button>	
					</span>
					<span>
						<button onclick="location.href='main?action=delete&post_num=${dto.postNum}'">삭제</button>	
					</span>
					<span>
						<button onclick="location.href='main'">메인으로</button>	
					</span>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>