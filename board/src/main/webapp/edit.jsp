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
            <input type="hidden" id="deletedFileCount" name="deletedFileCount" value="0"/> 
            <input type="hidden" id="deletedFileIds" name="deletedFileIds" value=""/>
    		<input type="hidden" id="remainingFileIds" name="remainingFileIds" value=""/>
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
	                    		<div class="file-row" id="file-${file.fileId}">
		                    		<a href="download.jsp?file_id=${file.fileId}">${file.fileName}</a>
		                    		<input type="checkbox" name="deleteFile" value="${file.fileId}" class="delete-checkbox" data-file-id="${file.fileId}"/> 삭제
	                    		</div>
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
    	
    	let isFileChanged = false; 
    	const existingFileNames = [];
    	
        document.querySelectorAll('.file-row a').forEach((fileLink) => {
            existingFileNames.push(fileLink.textContent);
        });
    	
		// 삭제된 파일 수 추적
	    function deleteFile(fileId) {
	    	const deletedFileCount = document.getElementById('deletedFileCount');
	    	const currentDeletedCount = parseInt(deletedFileCount.value);
	    	deletedFileCount.value = currentDeletedCount + 1; // 삭제된 파일 수 증가
	    	isFileChanged = true;
	    }
    
    	// 파일 최대 개수 체크
	    function checkFileCount(input) {
    		
	    	const maxFiles = 3;
            const currentFiles = input.files; // 새로 선택한 파일 수
            const deletedFiles = parseInt(document.getElementById('deletedFileCount').value); // 삭제를 위해 체크한 파일 수
            const existingFiles = document.querySelectorAll('.file-row').length; // 기존 게시물 첨부 파일 수

            const totalFiles = currentFiles + existingFiles - deletedFiles;

            if(totalFiles > maxFiles) {
                alert("최대 3개의 파일만 업로드할 수 있습니다.");
                input.value = ""; // 선택 초기화
                return;
            }
            
        	// 중복된 파일 이름 체크
            for(let i = 0; i < currentFiles.length; i++) {
            	const fileName = currentFiles[i].name;
                if(existingFileNames.includes(fileName)) {
                    alert(`파일 ${'${fileName}'}는 이미 존재하는 파일입니다.`);
                    input.value = "";
                    return;
                }
            }
	    }

	    // 파일 입력 필드의 변화 감지
	    function detectFileChange() {
	        const fileInput = document.getElementById('uploadFile');
	        const fileChanged = document.getElementById('fileChanged');
	        fileInput.addEventListener('change', function() {
	            if (fileInput.files.length > 0) {
	                isFileChanged = true; 
	            } else {
	                isFileChanged = false; 
	            }
	            fileChanged.value = isFileChanged;
	        });
	    }
		
	    // 삭제된 파일이 있는지 확인
	    function detectDeletedFiles() {
	        const deletedFiles = document.querySelectorAll('.delete-checkbox:checked');
	        if (deletedFiles.length > 0) {
	            isFileChanged = true;
	            fileChanged.value = isFileChanged;
	        }
	    }
	    
		// 삭제된 파일 ID와 남은 파일 ID를 수집
	    function collectFileIds() {
			
	        const deletedFileIds = [];
	        const remainingFileIds = [];
	        
	        // 삭제된 파일 체크박스에서 선택된 파일의 ID를 deletedFileIds에 추가
	        const deleteCheckboxes = document.querySelectorAll('.delete-checkbox:checked');
	        deleteCheckboxes.forEach(function(checkbox) {
	            deletedFileIds.push(checkbox.value);
	        });

	        // 삭제하지 않은 파일 ID를 remainingFileIds에 추가
	        const allCheckboxes = document.querySelectorAll('.delete-checkbox');
	        allCheckboxes.forEach(function(checkbox) {
	            if (!checkbox.checked) {
	                remainingFileIds.push(checkbox.value);
	            }
	        });

	     	// 조건에 따라 hidden input에 값 설정 또는 제거
	        const deletedFileIdsInput = document.getElementById('deletedFileIds');
	        const remainingFileIdsInput = document.getElementById('remainingFileIds');
	        
	        if (deletedFileIds.length > 0) {
	            deletedFileIdsInput.value = deletedFileIds.join(',');
	            isFileChanged = true;
	        } else {
	            deletedFileIdsInput.parentNode.removeChild(deletedFileIdsInput);
	        }
	        
	        if (remainingFileIds.length > 0) {
	            remainingFileIdsInput.value = remainingFileIds.join(',');
	        } else {
	            remainingFileIdsInput.parentNode.removeChild(remainingFileIdsInput);
	        }
	     
	    }
	    
	    // 폼 제출 시 파일 수정 여부 확인
	    function validateForm() {
	    	collectFileIds();
	    	detectDeletedFiles();
	    	console.log(isFileChanged);
	        if (isFileChanged) {
	            return confirm("파일이 수정되었거나 삭제되었습니다. 계속 진행하시겠습니까?");
	        }
	        return true;
	    }
	    
	 	// 파일을 삭제하려고 선택했을 때 해당 파일을 숨기기
	    function toggleFileVisibility(fileId) {
	    	const fileRow = document.getElementById('file-' + fileId);
            if (fileRow) {
            	fileRow.style.display = 'none';
            }
        }

	    window.onload = function() {
            detectFileChange();

            // 각 체크박스에 대해 이벤트 리스너를 추가하여 파일 삭제 함수 및 표시 변경 실행
            const checkboxes = document.querySelectorAll('.delete-checkbox');
            checkboxes.forEach(function(checkbox) {
                checkbox.addEventListener('click', function() {
                    const fileId = this.getAttribute('data-file-id');
                    toggleFileVisibility(fileId);  
                    deleteFile(fileId);  
                });
            });
        }
	</script>
</body>
</html>
