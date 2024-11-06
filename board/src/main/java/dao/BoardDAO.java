package dao;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import dto.BoardDTO;
import dto.BoardFileDTO;
import util.DBUtil;

public class BoardDAO {
		
		// 게시글 추가
		public int insertData(BoardDTO dto, List<BoardFileDTO> files) {

			int result = 0;
			Connection conn = null;
			PreparedStatement boardPstmt = null;
			PreparedStatement filePstmt = null;
			PreparedStatement plusPstmt = null;
			String boardSql;
			String fileSql;
			String plusSql;
			
			Date date = new Date(System.currentTimeMillis());
			Instant instant = date.toInstant();
			LocalDateTime time = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
			Timestamp timestamp = Timestamp.valueOf(time);
			String fileExist;
			
			if(files == null) {
				fileExist = "N";
			} else {
				fileExist = "Y";
			}
			
			try {
				boardSql = "INSERT INTO board "
							+ "(post_num,name,title,content,create_dt,views,file_exist) "
						 + "VALUES "
							+ "(?,?,?,?,?,1,?)";
				
				fileSql = "INSERT INTO board_file "
							+ "(file_id, post_num, file_name, file_data) "
						+ "VALUES "
							+ "(file_id_seq.NEXTVAL, ?, ?, ?)";
				
				plusSql = "INSERT INTO board_post_num_sequence "
						+ 	"(current_num) "
						+ "VALUES "
						+ 	"(?)";
				
				conn = DBUtil.connect();
				
				boardPstmt = conn.prepareStatement(boardSql);
				String content = dto.getContent();
				
				boardPstmt.setInt(1, dto.getPostNum());
				boardPstmt.setString(2, dto.getName());
				boardPstmt.setString(3, dto.getTitle());
				boardPstmt.setCharacterStream(4, new StringReader(content), content.length());
				boardPstmt.setTimestamp(5, timestamp);
				boardPstmt.setString(6, fileExist);
				
				result = boardPstmt.executeUpdate(); 
				
				if(fileExist.equals("Y")) {
					for(BoardFileDTO file : files) {
						 filePstmt = conn.prepareStatement(fileSql);
						 filePstmt.setInt(1, dto.getPostNum()); 
						 filePstmt.setString(2, file.getFileName());
			             filePstmt.setBytes(3, file.getFileData());
			             filePstmt.executeUpdate();
		            }
				}
				plusPstmt = conn.prepareStatement(plusSql);
				plusPstmt.setInt(1, dto.getPostNum());
				plusPstmt.executeUpdate();
				
				DBUtil.close(boardPstmt, conn);
				DBUtil.close(filePstmt, conn);
				DBUtil.close(plusPstmt, conn);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return result; 
		}
		
		// 게시글 총 개수 반환
		public int getTotalPostCount () {
			
			int totalCount = 0;
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT COUNT(*) "
					+ "FROM board";
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					totalCount = rs.getInt(1);
				}
				
				DBUtil.close(conn, pstmt, rs);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return totalCount;
		}
		
		// 게시글 다음 순번 조회
		public int getNextPostNum() {
			
			int nextPostNum = 0;
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

		    try {
				sql = "SELECT MAX(current_num) "
					+ "FROM board_post_num_sequence";
				
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					nextPostNum = rs.getInt(1) + 1;
				}
				
				DBUtil.close(conn, pstmt, rs);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return nextPostNum;
		}
		

		// 게시글 리스트 조회
		public List<BoardDTO> getLists(Integer start, Integer size, Integer page) {
			
			Integer offset = null;
			Integer fetch = null;
			
			if (size != null) {
				fetch = size;
			}
			if (page != null && size != null) {
				offset = (page - 1) * size;
			}
			
			List<BoardDTO> li = new ArrayList<BoardDTO>();
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {
				
				sql = "SELECT * "
					+ "FROM board "
					+ "ORDER BY post_num ASC "
					+ "OFFSET ? ROWS "
					+ "FETCH NEXT ? ROWS ONLY ";
				
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, offset);
				pstmt.setInt(2, fetch);
				
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					
					BoardDTO dto = new BoardDTO();
					Timestamp ts = rs.getTimestamp("create_dt");
					
					dto.setPostNum(rs.getInt("post_num"));
					dto.setName(rs.getString("name"));
					dto.setTitle(rs.getString("title"));
					dto.setDate(ts.toLocalDateTime());
					dto.setViews(rs.getInt("views"));
					li.add(dto);
				}
				DBUtil.close(conn, pstmt, rs);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return li;
		}
		
		// num으로 특정 게시글 하나 조회
		public BoardDTO getReadData(int num) {

			BoardDTO dto = null;
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {

				sql = "SELECT * "
					+ "FROM board "
					+ "WHERE post_num=?";
				
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);

				rs = pstmt.executeQuery();

				if (rs.next()) { 

					dto = new BoardDTO();
					
					dto.setPostNum(rs.getInt("post_num"));
					dto.setName(rs.getString("name"));
					dto.setTitle(rs.getString("title"));
					dto.setContent(rs.getString("content"));
					dto.setDate(LocalDateTime.ofInstant(new Date(rs.getDate("create_dt").getTime()).toInstant(), ZoneId.systemDefault()));
					dto.setViews(rs.getInt("views"));
					
				}
				DBUtil.close(conn, pstmt, rs);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return dto;
		}
		
		// 특정 게시글의 첨부 파일들 조회
		public List<BoardFileDTO> getReadAllFile(int num) {

			List<BoardFileDTO> li = new ArrayList<BoardFileDTO>();
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {

				sql = "SELECT * "
					+ "FROM board_file "
					+ "WHERE post_num=?";
				
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);

				rs = pstmt.executeQuery();

				while(rs.next()) { 

					BoardFileDTO dto = new BoardFileDTO();
					
					dto.setFileId(rs.getInt("file_id"));
					dto.setPostNum(rs.getInt("post_num"));
					dto.setFileName(rs.getString("file_name"));
					dto.setFileData(rs.getBytes("file_data"));
					
					li.add(dto);
					
				}
				DBUtil.close(conn, pstmt, rs);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return li;
		}
		
		// 특정 파일 조회
		public BoardFileDTO getReadFileById(int fileId) {
			
			BoardFileDTO dto = null;
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {

				sql = "SELECT * "
					+ "FROM board_file "
					+ "WHERE file_id=?";
				
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, fileId);

				rs = pstmt.executeQuery();

				if (rs.next()) { 

					dto = new BoardFileDTO();
					
					dto.setFileId(rs.getInt("file_id"));
					dto.setPostNum(rs.getInt("post_num"));
					dto.setFileName(rs.getString("file_name"));
					dto.setFileData(rs.getBytes("file_data"));
					
				}
				DBUtil.close(conn, pstmt, rs);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return dto;
		}
		
		// 게시글 리스트 검색 결과 총 개수 반환
		public int searchListCount(String keyword, String option) {
			
			int totalCount = 0;
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			String[] keywords = keyword.split(" ");
			int len = keywords.length;
			StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM board WHERE ");
			
			for(int i = 0; i < len; i++) {
				sql.append(option + " Like ?");
				
				if(i < len -1) {
					sql.append(" OR ");
				}
			}
			
			try {
				
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql.toString());
				
				for(int i = 0; i < len; i++) {
			        pstmt.setString(i + 1, "%" + keywords[i] + "%");
			    }
				
				rs = pstmt.executeQuery();
				if(rs.next()) {
					totalCount = rs.getInt(1);
				}
				DBUtil.close(conn, pstmt, rs);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return totalCount;
		}
		
		// 게시글 리스트 검색 페이지네이션
		public List<BoardDTO> searchList(String keyword, String option, Integer start, Integer size, Integer page) {
			
			Integer offset = null;
			Integer fetch = null;
			
			if (size != null) {
				fetch = size;
			}
			if (page != null && size != null) {
				offset = (page - 1) * size;
			}
			
			List<BoardDTO> li = new ArrayList<BoardDTO>();
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			String[] keywords = keyword.split(" ");
			int len = keywords.length;
			StringBuilder sql = new StringBuilder("SELECT * FROM board WHERE ");
			
			for(int i = 0; i < len; i++) {
				sql.append(option + " Like ?");
				
				if(i < len -1) {
					sql.append(" OR ");
				}
			}
			
			sql.append(" ORDER BY post_num ASC ");
			sql.append("OFFSET ? ROWS ");
			sql.append("FETCH NEXT ? ROWS ONLY");
			
			try {
				
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql.toString());
				
				for (int i = 0; i < len; i++) {
			        pstmt.setString(i + 1, "%" + keywords[i] + "%");
			    }
				
				pstmt.setInt(len + 1, offset);
				pstmt.setInt(len + 2, fetch);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					
					BoardDTO dto = new BoardDTO();
					Timestamp ts = rs.getTimestamp("create_dt");
					
					dto.setPostNum(rs.getInt("post_num"));
					dto.setName(rs.getString("name"));
					dto.setTitle(rs.getString("title"));
					dto.setDate(ts.toLocalDateTime());
					dto.setViews(rs.getInt("views"));
					li.add(dto);
				}
				DBUtil.close(conn, pstmt, rs);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return li;
		}

		// 조회수 1 증가
		public int plusViewCount(int num) {

			int result = 0;
			Connection conn = null;
			PreparedStatement pstmt = null;
			String sql;

			try {

				sql = "UPDATE board "
					+ "SET views=views+1 "
					+ "WHERE post_num=?";
				
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql);

				pstmt.setInt(1, num);
				result = pstmt.executeUpdate();

				DBUtil.close(pstmt, conn);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		
		// 게시글 수정
		public int updateData(BoardDTO dto, List<BoardFileDTO> files, boolean isFileChanged,
							  int deletedFileCount, String[] deletedFileIds, String[] remainingFileIds) {
			
			int result = 0;
			int fileCount = getReadAllFile(dto.getPostNum()).size();
			Connection conn = null;
			PreparedStatement boardPstmt = null;
			PreparedStatement filePstmt1 = null;
			PreparedStatement filePstmt2 = null;
			PreparedStatement filePstmt3 = null;
			String boardSql;
			String fileDeleteSql;
			String noFileSql;
			String fileInsertSql;
			
			try{
				
				boardSql = "UPDATE board "
						 + "SET content=? "
						 + "WHERE post_num=?";
				
				fileDeleteSql = "DELETE FROM board_file "
							  + "WHERE file_id=?";
				
				noFileSql = "UPDATE board "
						  + "SET file_exist=? "
						  + "WHERE post_num=?";
				
				fileInsertSql = "INSERT INTO board_file "
							  	+ "(file_id, post_num, file_name, file_data) "
							  + "VALUES "
							  	+ "(file_id_seq.NEXTVAL, ?, ?, ?)";
				
				conn = DBUtil.connect();
				
				boardPstmt = conn.prepareStatement(boardSql);
				boardPstmt.setString(1, dto.getContent());
				boardPstmt.setInt(2, dto.getPostNum());
				
				result = boardPstmt.executeUpdate();

				if(isFileChanged) {
					
					if(deletedFileCount != 0) {
						for(String deletedFileId : deletedFileIds) {
							filePstmt1 = conn.prepareStatement(fileDeleteSql);
							filePstmt1.setInt(1, Integer.parseInt(deletedFileId));
							filePstmt1.executeUpdate();
						}
					}
					
					if(fileCount == deletedFileCount && fileCount >= files.size()) {
						
						filePstmt2 = conn.prepareStatement(noFileSql);
						filePstmt2.setString(1, "N");
						filePstmt2.setInt(2, dto.getPostNum());
						filePstmt2.executeUpdate();
						
					} else {
						
						for(BoardFileDTO file : files) {
							filePstmt3 = conn.prepareStatement(fileInsertSql);
							filePstmt3.setInt(1, dto.getPostNum()); 
							filePstmt3.setString(2, file.getFileName());
							filePstmt3.setBytes(3, file.getFileData());
							filePstmt3.executeUpdate();
			            }
					}
				}
				DBUtil.close(boardPstmt, conn);
				DBUtil.close(filePstmt1, null);
				DBUtil.close(filePstmt2, null);
				DBUtil.close(filePstmt3, null);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		
		// 데이터 삭제
		public int deleteData(int postNum) {
			
			int result = 0;
			Connection conn = null;
			PreparedStatement pstmt = null;
			String boardSql;
			
			try{
				boardSql = "DELETE FROM board "
						 + "WHERE post_num=?";
				
				conn = DBUtil.connect();
				
				pstmt = conn.prepareStatement(boardSql);
				pstmt.setInt(1, postNum);
				
				result = pstmt.executeUpdate();
				
				DBUtil.close(pstmt, conn);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

}
