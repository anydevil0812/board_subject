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
import util.DBUtil;

public class BoardDAO {
		
		// 게시글 추가
		public int insertData(BoardDTO dto) {

			int result = 0;
			Connection conn = null;
			PreparedStatement pstmt = null;
			String sql;
			
			Date date = new Date(System.currentTimeMillis());
			Instant instant = date.toInstant();
			LocalDateTime time = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
			Timestamp timestamp = Timestamp.valueOf(time);
			
			try {
				sql = "INSERT INTO board "
					   + "(name,title,content,create_dt,views,file_name,file_data) "
					+ "VALUES "
					   + "(?,?,?,?,1,?,?)";
				
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql);
				String content = dto.getContent();
				
				pstmt.setString(1, dto.getName());
				pstmt.setString(2, dto.getTitle());
				pstmt.setCharacterStream(3, new StringReader(content), content.length());
				pstmt.setTimestamp(4, timestamp);
				pstmt.setString(5, dto.getFileName());
				pstmt.setBytes(6, dto.getFileData());
				
				result = pstmt.executeUpdate(); 

				DBUtil.close(pstmt, conn);

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
					+ "ORDER BY num ASC "
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
					
					dto.setNum(rs.getInt("num"));
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
					+ "WHERE num=?";
				
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);

				rs = pstmt.executeQuery();

				if (rs.next()) { 

					dto = new BoardDTO();
					
					dto.setNum(rs.getInt("num"));
					dto.setName(rs.getString("name"));
					dto.setTitle(rs.getString("title"));
					dto.setContent(rs.getString("content"));
					dto.setDate(LocalDateTime.ofInstant(new Date(rs.getDate("create_dt").getTime()).toInstant(), ZoneId.systemDefault()));
					dto.setViews(rs.getInt("views"));
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
			System.out.println("OPTION : " + option);
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
			
			sql.append(" ORDER BY num ASC ");
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
					
					dto.setNum(rs.getInt("num"));
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
					+ "WHERE num=?";
				
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
		public int updateData(BoardDTO dto) {
			
			int result = 0;
			Connection conn = null;
			PreparedStatement pstmt = null;
			String sql;
			
			try{
				
				sql = "UPDATE board "
					+ "SET content=?, file_name=?, file_data=? "
					+ "WHERE num=?";
				
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, dto.getContent());
				pstmt.setString(2, dto.getFileName());
				pstmt.setBytes(3, dto.getFileData());
				pstmt.setInt(4, dto.getNum());
				
				result = pstmt.executeUpdate();
				
				DBUtil.close(pstmt, conn);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		
		// 데이터 삭제
		public int deleteData(int num) {
			
			int result = 0;
			Connection conn = null;
			PreparedStatement pstmt = null;
			String sql;
			
			try{
				sql = "DELETE FROM board "
					+ "WHERE num=?";
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

}
