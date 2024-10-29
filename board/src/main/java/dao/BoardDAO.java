package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

			try {
				sql = "INSERT INTO board"
					   + "(num,name,title,content,date,views)"
					+ "VALUES"
					   + "(?,?,?,?,?,1)";
				
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, dto.getNum());
				pstmt.setString(2, dto.getName());
				pstmt.setString(3, dto.getTitle());
				pstmt.setString(4, dto.getContent());
				pstmt.setString(5, dto.getDate());
				
				result = pstmt.executeUpdate(); 

				pstmt.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			return result; 
		}
		
		// 게시글 총 개수 반환(페이지네이션)
		public int getTotalDataCount () {
			
			int totalCount = 0;
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT COUNT(*)"
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

		// 전체 데이터 출력
		public List<BoardDTO> getLists(int start, int end, String searchKey, String searchValue) {
		
			List<BoardDTO> lists = new ArrayList<BoardDTO>();
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {
				
				searchValue = "%" + searchValue + "%";
				
				sql = "SELECT * "
					+ "FROM (";
				sql+= "select rownum rnum, data.* from (";
				sql+= "select num,name,subject,hitcount,";
				sql+= "to_char(created,'YYYY-MM-DD') created ";
				sql+= "from board where " + searchKey + " like ? ";
				sql+= "order by num desc) data) " ;
				sql+= "where rnum>=? and rnum<=?";
				
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, searchValue);
				pstmt.setInt(2, start);
				pstmt.setInt(3, end);
				
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					
					BoardDTO dto = new BoardDTO();
					
					dto.setNum(rs.getInt("num"));
					dto.setName(rs.getString("name"));
					
					lists.add(dto);
				}
				
				rs.close();
				pstmt.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return lists;
		}
		
		// num으로 조회한 한개의 데이터
		public BoardDTO getReadData(int num) {

			BoardDTO dto = null;
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {

				sql = "SELECT *"
					+ "FROM board"
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
				rs.close();
				pstmt.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return dto;
		}

		// 조회수 증가
		public int updateHitCount(int num) {

			int result = 0;
			Connection conn = null;
			PreparedStatement pstmt = null;
			String sql;

			try {

				sql = "UPDATE board"
					+ "SET views=views+1"
					+ "WHERE num=?";
				
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql);

				pstmt.setInt(1, num);

				result = pstmt.executeUpdate();

				pstmt.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		
		// 데이터 수정
		public int updateData(BoardDTO dto) {
			
			int result = 0;
			Connection conn = null;
			PreparedStatement pstmt = null;
			String sql;
			
			try{
				
				sql = "UPDATE board"
					+ "SET name=?, pwd=?, email=?, subject=?, content=?"
					+ "WHERE num=?";
				
				conn = DBUtil.connect();
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, dto.getName());
				
				result = pstmt.executeUpdate();
				
				pstmt.close();
				
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
				
				pstmt.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return result;
			
		}

}