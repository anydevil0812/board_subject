package controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import dao.BoardDAO;
import dto.BoardDTO;

@WebServlet("/main")
public class BoardController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

    private BoardDAO dao = new BoardDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	String action = request.getParameter("action");
        
        if (action == null || "main".equals(action)) {
        	
        	int currentPage = 1;
            int pageSize = 2;
            int pageRange = 10;

            if (request.getParameter("page") != null) {
                currentPage = Integer.parseInt(request.getParameter("page"));
            }
            
    		int totalPosts = dao.getTotalPostCount();
            int totalPages = (int) Math.ceil((double) totalPosts / pageSize);

            int startPage = (currentPage - 1) / pageRange * pageRange + 1;
            int endPage = Math.min(startPage + pageRange - 1, totalPages);

            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("startPage", startPage);
            request.setAttribute("endPage", endPage);

            List<BoardDTO> li = dao.getLists((currentPage - 1) * pageSize, pageSize, currentPage);
            request.setAttribute("lists", li);
            
            List<String> timeList = new ArrayList<>();
            for (BoardDTO dto : li) {
                LocalDateTime time = dto.getDate();
                String t = time.toString().replace("T", " ");
                timeList.add(t);
            }
            request.setAttribute("time", timeList);
            request.getRequestDispatcher("/main.jsp").forward(request, response);
            
        } else if ("write".equals(action)) {
            request.getRequestDispatcher("/write.jsp").forward(request, response);
        
        } else if ("search".equals(action)) {
            request.getRequestDispatcher("/search.jsp").forward(request, response);
        
        } else if ("post".equals(action)) {
            request.getRequestDispatcher("/post.jsp").forward(request, response);
        
        } else if ("edit".equals(action)) {
            request.getRequestDispatcher("/edit.jsp").forward(request, response);
        
        } else if ("delete".equals(action)) {
        	int postNum = Integer.parseInt(request.getParameter("post_num"));
        	request.setAttribute("post_num", postNum);
            request.getRequestDispatcher("/delete.jsp").forward(request, response);
        
        } 
    	
    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String attachDir = getServletContext().getRealPath("/upload"); 
	    File saveDir = new File(attachDir);
		int maxSize = 10 * 1024 * 1024; 
	    String encoding = "UTF-8";
		
		int postNum = 0;
		String action = null;
		String title = null;
	    String username = null;
	    String content = null;
	    String fileChanged = null;
	    
	    List<String> fileNames = new ArrayList<>();
	    List<byte[]> files = new ArrayList<>();
	    
	    try {
	    	DiskFileItemFactory factory = new DiskFileItemFactory();
	    	factory.setSizeThreshold(maxSize);
	    	factory.setRepository(saveDir);
	    	
	    	ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = upload.parseRequest(request);
			System.out.println("items : " + items);
			
			for (FileItem item : items) {
			    if (item.isFormField()) { // 파일 or 일반 데이터 판별
			    	
			    	// 일반 필드 처리
			        String fieldName = item.getFieldName(); 
			        String fieldValue = item.getString(encoding); 
			        
			        switch (fieldName) {
			            case "post_num":
			                postNum = Integer.parseInt(fieldValue);
			                break;
			            case "title":
			                title = fieldValue;
			                break;
			            case "username":
			                username = fieldValue;
			                break;
			            case "content":
			                content = fieldValue;
			                break;
			            case "action":
			                action = fieldValue;
			                break;
			            case "fileChanged":
			            	fileChanged = fieldValue;
			            	break;
			        }
			        
			        // 디버그 출력
			        System.out.println("Field name: " + fieldName + ", Field value: " + fieldValue);
			    } else { // 파일 필드 처리
			        String fileName = item.getName();
			        if (fileName != null && !fileName.isEmpty()) {
			            fileNames.add(fileName);
			            files.add(item.get());
			        }
			    }
			}
	    } catch (FileUploadException e) {
			e.printStackTrace();
		}
	    System.out.println("POST_NUM : " + postNum);
	    request.setAttribute("post_num", postNum);
	    request.setAttribute("title", title);
        request.setAttribute("username", username);
        request.setAttribute("content", content);
		request.setAttribute("fileNames", fileNames);
		request.setAttribute("files", files);
		System.out.println("fileChanged1111 : " + fileChanged);
		request.setAttribute("fileChanged", fileChanged);
		
		if("insert".equals(action)) {
            request.getRequestDispatcher("/insert.jsp").forward(request, response);
        
		} else if ("update".equals(action)) {
            request.getRequestDispatcher("/update.jsp").forward(request, response);
        
        } else {
        	doGet(request, response);
        }
		
	}

}
	
    