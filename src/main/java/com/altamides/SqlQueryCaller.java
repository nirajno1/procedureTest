package com.altamides;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.altamides.config.LogManager;

public class SqlQueryCaller {
	private Logger log= LogManager.getLogger();
	
	public void print(String sqlQuery,String procName,Connection conn){
		log.log(Level.CONFIG, "Calling with sqlQuery:"+sqlQuery+" Procedure name: "+procName);
		try(PreparedStatement stmt=conn.prepareStatement(sqlQuery)) {
			stmt.setString(1, procName);
			ResultSet rs=stmt.executeQuery();
			StringBuilder sb=new StringBuilder("\n");
			 while (rs.next()) {
	                String line = rs.getString(1);
	                String text=rs.getString(2);
	                sb.append(line).append(" ").append(text);
	        }
	           log.log(Level.CONFIG, sb.toString());
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
