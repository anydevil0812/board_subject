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

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

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
        
        } else if ("post".equals(action)) {
            request.getRequestDispatcher("/post.jsp").forward(request, response);
        
        } else if ("edit".equals(action)) {
            request.getRequestDispatcher("/edit.jsp").forward(request, response);
        
        }  else if ("delete".equals(action)) {
        	int num = Integer.parseInt(request.getParameter("num"));
        	request.setAttribute("num", num);
            request.getRequestDispatcher("/delete.jsp").forward(request, response);
        
        } 
    	
    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String saveDir = getServletContext().getRealPath("/upload"); 
	    int maxSize = 10 * 1024 * 1024; 
	    String encoding = "UTF-8";
		
		MultipartRequest multi = new MultipartRequest(request, saveDir, maxSize, encoding, new DefaultFileRenamePolicy());
		String action = multi.getParameter("action");
		String title = multi.getParameter("title");
	    String username = multi.getParameter("username");
	    String content = multi.getParameter("content");
	    String fileName = multi.getFilesystemName("uploadFile");
	    int num = Integer.valueOf(multi.getParameter("num"));
	    File file = multi.getFile("uploadFile");
	    
	    request.setAttribute("title", title);
        request.setAttribute("username", username);
        request.setAttribute("content", content);
		request.setAttribute("fileName", fileName);
		request.setAttribute("file", file);
		request.setAttribute("num", num);
		System.out.println("Action : " + action);
		
		if("insert".equals(action)) {
            request.getRequestDispatcher("/insert.jsp").forward(request, response);
        
		} else if ("update".equals(action)) {
            request.getRequestDispatcher("/update.jsp").forward(request, response);
        
        } else {
        	doGet(request, response);
        }
		
	}

}
	
    