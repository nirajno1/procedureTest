package com.altamides.main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.altamides.Caller;
import com.altamides.config.ConfigurationReader;
import com.altamides.config.ConnectionBuilder;
import com.altamides.config.LogManager;
import com.altamides.model.DBConfig;

public class AppMain {
	private static Logger log= LogManager.getLogger();
	
	public static void main(String[] args) {
		ConfigurationReader configReader=new ConfigurationReader();
		DBConfig dbconfig = null;
		try {
			dbconfig = configReader.getDBConfig();
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			System.exit(1);
		}
		
		ConnectionBuilder connBuilder= new ConnectionBuilder(dbconfig);
		
		try(Connection conn=connBuilder.getConnection()) {
			
			Caller caller=new Caller(conn, dbconfig);
			log.log(Level.FINE,"------------------ Print procedure body : Start ------------------");
			caller.printProcBody();
			log.log(Level.FINE,"------------------ Print procedure body : Finished ------------------");
			log.log(Level.FINE,"------------------ callProcedure : Start ------------------");
			caller.callProcedure(args);
			log.log(Level.FINE,"------------------ callProcedure : Finished ------------------");
			log.log(Level.FINE,"------------------ printDbmsOutputLine : Start ------------------");
			caller.printDbmsOutputLine(args);
			log.log(Level.FINE,"------------------ printDbmsOutputLine : Finished ------------------");
			log.log(Level.FINE,"------------------ callRefProcedure : Start ------------------");
			caller.callRefProcedure(args);
			log.log(Level.FINE,"------------------ callRefProcedure : Finished ------------------");
			
		} catch (SQLException  e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
