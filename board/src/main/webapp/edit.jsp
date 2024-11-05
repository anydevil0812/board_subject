<%@page import="dto.BoardFileDTO"%>
<%@page import="java.util.List"%>
<%@page import="dao.BoardDAO"%>
<%@page import="dto.BoardDTO"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%

    String numParam = request.getParameter("post_num");
    int postNum = Integer.parseInt(numParam);

    BoardDAO dao = new BoardDAO();
    BoardDTO dto = dao.getReadData(postNum);
	
    List<BoardFileDTO> files = dao.getReadAllFile(postNum);
    
    request.setAttribute("dto", dto);
    request.setAttribute("files", files);
    
%>

<html>
<head>
    <title>게시글 수정</title>
    <link rel="stylesheet" href="./css/write.css"/> 
</head>
<body>
    <h2>게시글 수정</h2>
    <div class="write-container">
        <form action="main" method="post" enctype="multipart/form-data" onsubmit="return validateForm();">
            <input type="hidden" name="post_num" value="${dto.postNum}"/>
            <input type="hidden" name="action" value="update">
            <input type="hidden" id="fileChanged" name="fileChanged" value="false"/>
            <table>
                <tr class="post-title">
                    <td class="tag"><label>제목</label></td>
                    <td>${dto.title}</td>
                </tr>
                <tr class="post-writer">
                    <td class="tag"><label>작성자</label></td>
                    <td>${dto.name}</td>
                </tr>
                <tr class="post-content">
                    <td class="tag"><label>내용</label></td>
                    <td><textarea class="content" name="content" placeholder="내용을 입력하세요." required>${dto.content}</textarea></td>
                </tr>
                <tr class="attach-file">
                    <td class="tag"><label>첨부파일</label></td>
                    <td>
                        <c:if test="${not empty files}">
	                    	<c:forEach var="file" items="${files}">
	                    		<a href="download.jsp?file_id=${file.fileId}">${file.fileName}</a>
	                    	</c:forEach>
                    	</c:if>
                        <input type="file" id="uploadFile" name="uploadFile" multiple onchange="checkFileCount(this)" />
                    </td>
                </tr>
            </table>
            <div class="button-group">
                <span id="add"><input type="submit" value="게시글 수정" id="post-button"/></span>
                <span><button type="button" onclick="location.href='main'">메인으로</button></span>
            </div>
        </form>
    </div>
    <script>
    	// 파일 최대 개수 체크
	    function checkFileCount(input) {
	        if (input.files.length > 3) {
	            alert("최대 3개의 파일만 업로드할 수 있습니다.");
	            input.value = "";
	        }
	    }
	    
	    let isFileChanged = false; 

	    // 파일 입력 필드의 변화 감지
	    function detectFileChange() {
	        const fileInput = document.getElementById('uploadFile');
	        const fileChanged = document.getElementById('fileChanged');
	        fileInput.addEventListener('change', function() {
	            if (fileInput.files.length > 0) {
	                isFileChanged = true; // 새 파일이 선택되었음을 감지
	            } else {
	                isFileChanged = false; // 파일 선택 해제
	            }
	            fileChanged.value = isFileChanged;
	        });
	    }

	    // 폼 제출 시 파일 수정 여부 확인
	    function validateForm() {
	        if (isFileChanged) {
	            return confirm("새 파일이 선택되었습니다. 계속 진행하시겠습니까?");
	        }
	        return true;
	    }

	    window.onload = detectFileChange; // 페이지 로드 시 파일 변화 감지 설정
	</script>
</body>
</html>
