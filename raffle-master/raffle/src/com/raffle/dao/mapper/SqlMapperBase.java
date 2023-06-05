package com.raffle.dao.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SqlMapperBase {
	protected boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int columns = rsmd.getColumnCount();
	    for (int x = 1; x <= columns; x++) 
	    {
	    	String name = rsmd.getColumnLabel(x);
	    	
	        if (columnName.equals(name)) { 
	            return true;
	        }
	    }
	    return false;
	}
}
