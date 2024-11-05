<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="dto.BoardFileDTO"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="dao.BoardDAO"%>
<%@page import="dto.BoardDTO"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
	
	int postNum = (int) request.getAttribute("post_num");
	String title = (String) request.getAttribute("title");
	String username = (String) request.getAttribute("username");
	String content = (String) request.getAttribute("content");
	List<String> fileNames = (List<String>) request.getAttribute("fileNames");
	List<byte[]> files = (List<byte[]>) request.getAttribute("files");
	
	BoardDTO dto = new BoardDTO();
	List<BoardFileDTO> fileDtoList = new ArrayList<BoardFileDTO>();
	
	for(int i = 0; i < fileNames.size(); i++){
		String fileName = fileNames.get(i);
		byte[] file = files.get(i);
		
		if (fileName != null && file != null) {
			BoardFileDTO fileDto = new BoardFileDTO();
			fileDto.setPostNum(postNum);
            fileDto.setFileName(fileName);
            fileDto.setFileData(file);
            
            fileDtoList.add(fileDto);
		}
	}
	
	dto.setPostNum(postNum);
    dto.setName(username);
    dto.setTitle(title);
    dto.setContent(content);

    BoardDAO dao = new BoardDAO();
    int result = dao.insertData(dto, fileDtoList);
    
    if (result > 0) {
        response.sendRedirect("main");
    } else {
%>
        <script>
            alert("게시글 추가에 실패했습니다. 다시 시도해 주세요.");
            history.back();
        </script>
<%
    }
%>
