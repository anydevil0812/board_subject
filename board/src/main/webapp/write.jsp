<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.List" %>
<%
	
%>
<html>
<head>
    <title>게시글 작성</title>
    <link rel="stylesheet" href="./css/write.css"/> 
</head>
<body>
	<h2>글쓰기</h2>
    <div class="write-container">
       <form action="insert.jsp" method="post" enctype="multipart/form-data">
	       <table>
	       	<tr class="post-title">
	       		<td class="tag"><label>제목</label></td>
	       		<td><input type="text" class="title" name="title" required /></td>
	       	</tr>
			<tr class="post-writer">
				<td class="tag"><label>작성자</label></td>
	       		<td><input type="text" class="username" name="username" required /></td>
			</tr>	       	 	
	        <tr class="post-content">
	        	<td class="tag"><label>내용</label></td>
	       		<td><textarea class="content" name="content" placeholder="내용을 입력하세요." required></textarea></td>
	        </tr>
	        <tr class="attach-file">
	        	<td class="tag"><label>파일 첨부</label></td>
	        	<td><input type="file" name="uploadFile" /></td>
	        </tr>
	       </table>
	       <div class="button-group">
	       	<span id="add"><input type="submit" value="게시글 추가" id="post-button"/></span>
           	<span><button onclick="location.href='main.jsp'">메인으로</button></span>
	       </div>
       </form>
    </div>
</body>
</html>
