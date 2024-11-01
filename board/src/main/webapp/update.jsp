<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="dao.BoardDAO"%>
<%@page import="dto.BoardDTO"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%

	String content = (String) request.getAttribute("content");
	String fileName = (String) request.getAttribute("fileName");
	File file = (File) request.getAttribute("file");
	int num = (int) request.getAttribute("num");
	
	BoardDTO dto = new BoardDTO();
	
	if (fileName != null && file != null) {
		byte[] fileData = new byte[(int) file.length()];
	    FileInputStream fis = new FileInputStream(file);
	    
	    fis.read(fileData);
	    fis.close();
	    
	    dto.setFileName(fileName);
	    dto.setFileData(fileData);
	}
    
    dto.setContent(content);
    dto.setNum(num);

    BoardDAO dao = new BoardDAO();
    int result = dao.updateData(dto);
    
    if (result > 0) {
        response.sendRedirect("main");
    } else {
%>
        <script>
            alert("게시글 변경에 실패했습니다. 다시 시도해 주세요.");
            history.back();
        </script>
<%
    }
%>
