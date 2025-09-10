package com.pim.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pim.enums.DataBaseColumnName;
import com.pim.enums.LogType;
import com.pim.reports.ExtentLogger;

import static com.pim.reports.FrameworkLogger.log;

import java.sql.Connection;

public class DatabaseUtilitiies {

	private static Statement stmt;
	private static Connection con;

	public static void dbSetup(String url, String user, String password){
		//database setup
		try {
			// Make the database connection
			String dbClass = "com.mysql.cj.jdbc.Driver";
			Class.forName(dbClass).newInstance();
			// Get connection to DB
			con = DriverManager.getConnection(url, user, password);
			stmt = con.createStatement();
		}catch(Exception e) {
			e.printStackTrace();
		}  
	} 

	public static ResultSet getQueryResult(String selectStatement)  {
		ResultSet res = null;
		try{
			String query = selectStatement;
			res = stmt.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}//end of getQueryResult.n

	//for single row 
	public static Map<String, String> getOneRowQueryResult(String selectStatement, String ...colnames){
		Map<String, String> map = new HashMap<>();
		ResultSet res = getQueryResult(selectStatement);
		log(LogType.EXTENTANDCONSOLE,"Executing Query : "+selectStatement);
		try {
			while(res.next()) {
				for(String column : colnames) {
					map.put(column, res.getString(column));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log(LogType.EXTENTANDCONSOLE,"Query Result : "+map);

		return map;
	}
	
	//for multiple row
	public static List<Map<String, String>> getMultipleQueryResult(String selectStatement, String ...colnames) {
		//Map<String, String> map = new HashMap<>();
		ArrayList<Map<String, String>> ResultList = new ArrayList<>();
		ResultSet res = getQueryResult(selectStatement);
		log(LogType.EXTENTANDCONSOLE,"Executing Query : "+selectStatement);
		try {
			while(res.next()) {
				Map<String, String> map = new HashMap<>();
				for(String column : colnames) {
					map.put(column, res.getString(column));
				}
				ResultList.add(map);  
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log(LogType.EXTENTANDCONSOLE,"Query Result : "+ResultList);
		return ResultList;
	}

	public static List<String> getSingleColumnData(String selectStatement, String column) {
		// Map<String, String> map = new HashMap<>();
		ArrayList<String> ResultList = new ArrayList<>();
		ResultSet res = getQueryResult(selectStatement);
		try {
			while (res.next()) {
				ResultList.add(res.getString(column));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResultList;
	}
	public static int getUpdatedResult(String updateStatement) throws Exception {
		int res = 0;
		try{
			String query = updateStatement;
			// Get the contents from table on DB
			res = stmt.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
			ExtentLogger.fail("Unable to update content from a table:" + e);
		}
		return res;
	}//end of getUpdatedResult.

	public static int getDeleteResult(String deleteStatement) throws Exception {
		int res = 0;
		try{
			String query = deleteStatement;
			// Get the contents from table on DB
			res = stmt.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
			ExtentLogger.fail("Unable to delete content from a table:" + e);
		}
		return res;
	}//end of getDeleteResult.

	/*public static int getInsertResult(String insertStatement) throws Exception {
    	int res = 0;
        try{
            String query = insertStatement;
            // Get the contents from table on DB
            res = stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
            SeleniumUtilities.logFail(logger,"Unable to update content from a table: " + e);
        }
        return res;
    }//end of getinsertResult*/

	public static int getCreatetableResult(String createStatement) {
		int res = 0;
		try{
			String query = createStatement;
			// Get the contents from table on DB
			res = stmt.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}//end of getCreatetable 


	public static void closingConnection() {
		// Close DB connection
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}//end of tearDown method

}//end of class