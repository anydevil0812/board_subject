package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

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
					   + "(name,title,content,create_dt,views) "
					+ "VALUES "
					   + "(?,?,?,?,1)";
				
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, dto.getName());
				pstmt.setString(2, dto.getTitle());
				pstmt.setString(3, dto.getContent());
				pstmt.setTimestamp(4, timestamp);
				
				result = pstmt.executeUpdate(); 

				DBUtil.close(pstmt, conn);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return result; 
		}
		
		// 게시글 총 개수 반환
		public int getTotalDataCount () {
			
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

		// 게시글 페이지네이션
		public List<BoardDTO> getLists(Integer start, Integer size, Integer page, String searchKey, String searchValue) {
			
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

				}
				DBUtil.close(conn, pstmt, rs);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return dto;
		}

		// 조회수 1 증가
		public int updateViewCount(int num) {

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
					+ "SET content=? "
					+ "WHERE num=?";
				
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, dto.getContent());
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
