<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="dao.BoardDAO"%>
<%@page import="dto.BoardDTO"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String saveDir = application.getRealPath("/upload"); 
    int maxSize = 10 * 1024 * 1024; 
    String encoding = "UTF-8";

    MultipartRequest multi = new MultipartRequest(request, saveDir, maxSize, encoding, new DefaultFileRenamePolicy());

    String title = multi.getParameter("title");
    String username = multi.getParameter("username");
    String content = multi.getParameter("content");
    String fileName = multi.getFilesystemName("uploadFile"); 
    
    File file = multi.getFile("uploadFile");
    byte[] fileData = new byte[(int) file.length()];
    FileInputStream fis = new FileInputStream(file);
    
    fis.read(fileData);
    fis.close();
    
    BoardDTO dto = new BoardDTO();
    dto.setName(username);
    dto.setTitle(title);
    dto.setContent(content);
    dto.setFileName(fileName);
    dto.setFileData(fileData);

    BoardDAO dao = new BoardDAO();
    int result = dao.insertData(dto);

    if (result > 0) {
        response.sendRedirect("main.jsp");
    } else {
%>
        <script>
            alert("게시글 추가에 실패했습니다. 다시 시도해 주세요.");
            history.back();
        </script>
<%
    }
%>
