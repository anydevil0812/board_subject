<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.List" %>
<%
	
%>
<html>
<head>
    <title>Subject1-3</title>
    <link rel="stylesheet" href="./css/1.css"/> 
</head>
<body>
    <div class="write-container">
       <h3>글쓰기</h3>
       <form action="upload.jsp" method="post" enctype="multipart/form-data">
       	 <div class="post-title">
       	 	<label>제목</label>
         	<label><input type="text" class="title" name="title" required /></label>
       	 </div>
       	 <div class="post-writer">
         	<label>작성자</label>
         	<label><input type="text" class="username" name="username" required /></label>
         </div>
       	 <div class="post-content">
       	 	<label>내용</label>
         	<label><textarea class="content" name="content" placeholder="내용을 입력하세요." required></textarea></label>
       	 </div>
       	 <div>
       	 	<input type="file" name="uploadFile" />
       	 	<input type="submit" value="파일 업로드" />
       	 </div>
         <input type="submit" value="게시글 추가" id="post-button"/>
       </form>
    </div>
</body>
</html>