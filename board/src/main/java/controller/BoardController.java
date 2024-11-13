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
import dto.BoardFileDTO;

@WebServlet("/main")
public class BoardController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

    private BoardDAO dao = new BoardDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	String action = request.getParameter("action");
        
        if (action == null || "main".equals(action)) {
        	
        	int currentPage = 1;
            int pageSize = 5;
            int pageRange = 5;

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
        
        } else if ("reply".equals(action)) {
            request.getRequestDispatcher("/reply.jsp").forward(request, response);
        
        } else if ("search".equals(action)) {
            request.getRequestDispatcher("/search.jsp").forward(request, response);
        
        } else if ("post".equals(action)) {
            request.getRequestDispatcher("/post.jsp").forward(request, response);
        
        } else if ("edit".equals(action)) {
            request.getRequestDispatcher("/edit.jsp").forward(request, response);
        
        } else if ("delete".equals(action)) {
        	int postNum = Integer.parseInt(request.getParameter("post_num"));
            int result = dao.deleteData(postNum);
        	
            sendResultPage(request, response, result, "삭제");
        } 
    	
    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String attachDir = getServletContext().getRealPath("/upload"); 
	    File saveDir = new File(attachDir);
	    int maxRequestSize = 15 * 1024 * 1024; 
	    int maxFileSize = 10 * 1024 * 1024;    
	    int maxContentSize = 4 * 1024 * 1024;  
	    String encoding = "UTF-8";
		
		int postNum = 0;
		String parentNum = null;
		int deletedFileCount = 0;
		String action = null;
		String title = null;
	    String username = null;
	    String content = null;
	    String deletedFileIds = null;
	    String remainingFileIds = null;
	    String[] deletedFileIdsArr = null;
	    String[] remainingFileIdsArr = null;
	    boolean isFileChanged = false;
	    
	    List<String> fileNames = new ArrayList<>();
	    List<byte[]> files = new ArrayList<>();
	    
	    try {
	    	DiskFileItemFactory factory = new DiskFileItemFactory();
	    	factory.setSizeThreshold(maxContentSize); // content 크기 조절(4MB)
	    	factory.setRepository(saveDir);
	    	
	    	ServletFileUpload upload = new ServletFileUpload(factory);
	    	upload.setFileSizeMax(maxFileSize); // 파일 크기 조절(10MB)
	    	upload.setSizeMax(maxRequestSize); // 전체 요청 크기 조절 (15MB)
			List<FileItem> items = upload.parseRequest(request);
			
			for (FileItem item : items) {
			    if (item.isFormField()) { // 파일 or 일반 데이터 판별
			    	
			    	// 일반 필드 처리
			        String fieldName = item.getFieldName(); 
			        String fieldValue = item.getString(encoding); 
			        
			        switch (fieldName) {
			            case "post_num":
			                postNum = Integer.parseInt(fieldValue);
			                break;
			            case "parent_num":
			            	parentNum = fieldValue;
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
			            	isFileChanged = Boolean.parseBoolean(fieldValue);
			            	break;
			            case "deletedFileCount":
			            	deletedFileCount = Integer.parseInt(fieldValue);
			            	break;
			            case "deletedFileIds":
			            	deletedFileIds = fieldValue;
			                break;
			            case "remainingFileIds":
			            	remainingFileIds = fieldValue;
			                break;
			        }
			    } else { // 파일 필드 처리
			        String fileName = item.getName();
			        if (fileName != null && !fileName.isEmpty()) {
			            
			        	String filePath = attachDir + File.separator + fileName; 
	                    File uploadedFile = new File(filePath);
	                    item.write(uploadedFile);
	                    
			        	fileNames.add(fileName);
			            files.add(item.get());
			        }
			    }
			}
			
			if(deletedFileIds != null) {
				deletedFileIdsArr = deletedFileIds.split(",");
			}
			
			if(remainingFileIds != null) {
				remainingFileIdsArr = remainingFileIds.split(",");
			}
			
	    } catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		if("insert".equals(action)) {
			
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
		    if(parentNum != null) {
		    	dto.setParentNum(Integer.parseInt(parentNum));
		    }
		    
		    int result = dao.insertData(dto, fileDtoList);
		    sendResultPage(request, response, result, "작성");
        
		} else if ("update".equals(action)) {
			
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

		    int result = dao.updateData(dto, fileDtoList, isFileChanged, deletedFileCount,
		    							deletedFileIdsArr, remainingFileIdsArr);
			
		    sendResultPage(request, response, result, "수정");
        
        } else {
        	doGet(request, response);
        }
		
	}
	
	protected void sendResultPage(HttpServletRequest request, HttpServletResponse response, int result, String type) throws ServletException, IOException {
		
		if (result > 0) {
        	request.setAttribute("successMessage", "게시글 " + type + "에 성공했습니다.");
            request.getRequestDispatcher("/success.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "게시글 " + type + "에 실패했습니다.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
	}

}
	
    