<%@page import="dao.BoardDAO"%>
<%@page import="dto.BoardDTO"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
    String numParam = request.getParameter("num");
    int num = Integer.parseInt(numParam);

    BoardDAO dao = new BoardDAO();
    BoardDTO dto = dao.getReadData(num);

    request.setAttribute("dto", dto);
%>

<html>
<head>
    <title>게시글 수정</title>
    <link rel="stylesheet" href="./css/write.css"/> 
</head>
<body>
    <h2>게시글 수정</h2>
    <div class="write-container">
        <form action="main" method="post" enctype="multipart/form-data">
            <input type="hidden" name="num" value="${dto.num}"/>
            <input type="hidden" name="fileName" value="${dto.fileName}"/>
            <input type="hidden" name="fileData" value="${dto.fileData}"/>
            <input type="hidden" name="action" value="update">
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
                        <input type="file" name="uploadFile" id="uploadFile" value="${dto.fileName}" />
                    </td>
                </tr>
            </table>
            <div class="button-group">
                <span id="add"><input type="submit" value="게시글 수정" id="post-button"/></span>
                <span><button type="button" onclick="location.href='main'">메인으로</button></span>
            </div>
        </form>
    </div>
</body>
</html>
