<%@page import="dao.BoardDAO"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.List" %>
<%
	
	String parentNum = request.getParameter("parent_num");
	String title = request.getParameter("title");

	BoardDAO dao = new BoardDAO();
	int postNum = dao.getNextPostNum();
    
    request.setAttribute("postNum", postNum);
    request.setAttribute("title", title);
    request.setAttribute("parentNum", parentNum);
	
%>
<html>
<head>
    <title>${parentNum}번 게시글 답글 작성</title>
    <link rel="stylesheet" href="./css/write.css"/> 
</head>
<body>
	<h2>글쓰기</h2>
    <div class="write-container">
       <form action="main" method="post" enctype="multipart/form-data">
       	   <input type="hidden" name="action" value="insert">
       	   <input type="hidden" name="title" value="RE: ${title}">
       	   <input type="hidden" name="post_num" value="${postNum}">
       	   <input type="hidden" name="parent_num" value="${parentNum}">
	       <table>
	       	<tr class="post-title">
	       		<td class="tag"><label>제목</label></td>
	       		<td>RE: ${title}</td>
	       	</tr>
			<tr class="post-writer">
				<td class="tag"><label>작성자</label></td>
	       		<td><input type="text" class="username" name="username" maxlength="10" oninput="checkUsernameLength(this)" required /></td>
			</tr>	       	 	
	        <tr class="post-content">
	        	<td class="tag"><label>내용</label></td>
	       		<td>
	       			<textarea class="content" name="content" maxlength="1000" placeholder="내용을 입력하세요." oninput="updateContentLength(this)" required></textarea>
	       			<p id="contentLengthInfo">0 / 1000자</p>
       			</td>
	        </tr>
	        <tr class="attach-file">
	        	<td class="tag"><label>파일 첨부</label></td>
	        	<td><input type="file" name="uploadFile" multiple onchange="checkFileCount(this)"/></td>
	        </tr>
	       </table>
	       <div class="button-group">
	       	<span id="add"><input type="submit" value="게시글 추가" id="post-button"/></span>
	       </div>
       </form>
       <span><button onclick="location.href='main'">메인으로</button></span>
    </div>
    <script>
    	
	    // 작성자 길이 확인 및 초과시 잘라내기
	    function checkUsernameLength(input) {
	        const maxLength = input.maxLength;
	        if (input.value.length > maxLength) {
	        	alert("작성자는 최대 " + maxLength + "자까지 입력할 수 있습니다.");
	            input.value = input.value.substring(0, maxLength); 
	        }
	    }
	
	    // 게시글 내용 실시간 글자 수 업데이트 및 초과시 잘라내기
	    function updateContentLength(textarea) {
	        const maxLength = textarea.maxLength;
	        const currentLength = textarea.value.length;
	        document.getElementById('contentLengthInfo').innerText = currentLength + " / " + maxLength + "자";
	
	        // 내용이 최대 길이를 초과하면 잘라내고 경고창 표시
	        if (currentLength > maxLength) {
	        	alert("내용은 최대 " + maxLength + "자까지 입력할 수 있습니다.");
	            textarea.value = textarea.value.substring(0, maxLength); 
	            document.getElementById('contentLengthInfo').innerText = maxLength + " / " + maxLength + "자";
	        }
	    }
    
    	// 최대 파일 개수 제한 체크
	    function checkFileCount(input) {
	        if (input.files.length > 3) {
	            alert("최대 3개의 파일만 업로드할 수 있습니다.");
	            input.value = "";
	        }
	    }
	    
	 	
	</script>
</body>
</html>
