<%@page import="java.io.File"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
	String uploadPath = request.getServletContext().getRealPath("/upload");
	int fileSize = 10 * 1024 * 1024;
	MultipartRequest mpreq = new MultipartRequest(request, uploadPath, fileSize, "utf-8", new DefaultFileRenamePolicy());
	String fileName = mpreq.getFilesystemName("uploadFile");
	
	File file = mpreq.getFile("uploadFile");
	
	
%>
