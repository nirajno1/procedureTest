package com.altamides.config;

import static java.lang.System.exit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.altamides.model.DBConfig;

public class ConnectionBuilder {
	 private Logger log= LogManager.getLogger();
	 private DBConfig dbConfig;
	 private Connection conn;
	 public ConnectionBuilder(DBConfig dbConfig) {
		this.dbConfig=dbConfig;
	}

	 public Connection getConnection() {
		 if(conn== null) {
			 conn=createConnection();
		 }
		 return conn;
	 }
	private Connection createConnection() {
		log.log(Level.FINE, "***>>>Connecting to ".concat(dbConfig.getConnectionURL()).concat("<<<***"));
		try {
			Class.forName(dbConfig.getDriver());
			Connection conn = DriverManager.getConnection(dbConfig.getConnectionURL(), dbConfig.getUser(),
					dbConfig.getPassword());
		log.log(Level.FINE, "***>>>Connected to ".concat(dbConfig.getConnectionURL()).concat("<<<***"));
			return conn;
		} catch (ClassNotFoundException | SQLException e) {
			log.log(Level.SEVERE, e.getMessage(),e);
			exit(1);
		}
		return null;
	}

}
