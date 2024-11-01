<%@page import="java.net.URLEncoder"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.IOException"%>
<%@page import="javax.servlet.ServletOutputStream"%>
<%@page import="javax.servlet.http.HttpServletResponse"%>
<%@page import="dao.BoardDAO"%>
<%@page import="dto.BoardDTO"%>
<%@page import="java.sql.SQLException"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String numParam = request.getParameter("num");
    int num = Integer.parseInt(numParam);
    
    BoardDAO dao = new BoardDAO();
    BoardDTO dto = dao.getReadData(num);
    
    String fileName = dto.getFileName();
    byte[] fileData = dto.getFileData();
    String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
    
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
    response.setContentLength(fileData.length);

    try (ServletOutputStream sos = response.getOutputStream()) {
        sos.write(fileData);
        sos.flush();
    } catch (IOException e) {
        e.printStackTrace();
    }
%>
