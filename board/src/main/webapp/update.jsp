<%@page import="java.util.ArrayList"%>
<%@page import="dto.BoardFileDTO"%>
<%@page import="java.util.List"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="dao.BoardDAO"%>
<%@page import="dto.BoardDTO"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
	
	String fileChangedParam = (String) request.getAttribute("fileChanged");
	System.out.println("fileChangedParam : " + fileChangedParam);
	boolean isFileChanged = "true".equals(fileChangedParam);

	int postNum = (int) request.getAttribute("post_num");
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
	
    dto.setContent(content);
    dto.setPostNum(postNum);

    BoardDAO dao = new BoardDAO();
    System.out.println("isFileChanged : " + isFileChanged);
    int result = dao.updateData(dto, fileDtoList, isFileChanged);
    System.out.println("AAAAAAAAAAAAAAAAAAAA");
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
