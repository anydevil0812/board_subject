package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	
	 public static Connection connect() throws SQLException, ClassNotFoundException {
		 	String driver ="oracle.jdbc.driver.OracleDriver";
	        String db_url = "jdbc:oracle:thin:@localhost:1521:orcl";
	        String db_id = "SYSTEM";
	        String db_pw = "20241014!";
	        Class.forName(driver);
	        
	        return DriverManager.getConnection(db_url, db_id, db_pw);
	 }

     public static void close(Connection con, Statement stmt, ResultSet rset) {
        try {
            if (rset != null) {
                rset.close();
                rset = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            if (con != null) {
                con.close();
                con = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
     }
     
     public static void close(Statement stmt, Connection con) {
 		try {
 			if(stmt != null) {
 				stmt.close();
 			}
 			if(con != null) {
 				con.close();
 			}
 		} catch (SQLException e) {
 			e.printStackTrace();
 		}
 	}
	
}
