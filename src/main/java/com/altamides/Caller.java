package com.altamides;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.altamides.config.LogManager;
import com.altamides.model.DBConfig;

public class Caller {
	private Logger log= LogManager.getLogger();
	private Connection conn;
	private DBConfig dbConfig;
	
	public Caller(Connection conn,DBConfig dbConfig) {
		this.conn=conn;
		this.dbConfig=dbConfig;
	}
	/**
	 * print procedure body (Meta data)
	 * @param procName name of procedure
	 */
	public void printProcBody() {
		log.log(Level.FINE, "Starting application to print oracle procedure");
		String sqlQuery=dbConfig.getSqlQuery();
		String procName=dbConfig.getProcedureName();
		SqlQueryCaller printProc=new SqlQueryCaller();

		printProc.print(sqlQuery,procName, conn);

		log.log(Level.FINE, "finished application to print oracle procedure");
	}
	
	
	/**
	 * Calling procedure with in and out parameter
	 * @param args
	 */
	public void callProcedure(String[] args) {
		log.log(Level.FINE, "Starting application to call oracle procedure");
		if (args.length > 0) {
			String inputValue = args[0];
		
				ProcedureCaller pm = new ProcedureCaller();
				pm.callProcedure(conn, dbConfig.getProcedureName(), inputValue, dbConfig.getNumOutParam());
		} else {
			log.log(Level.SEVERE, "Pass IMEI code");
		}
	}

	
	public void printDbmsOutputLine(String[] args) {
		log.log(Level.FINE, "Starting application to print dbms_output.put_line");
		if (args.length > 0) {
			String inputValue = args[0];
			String callString=dbConfig.getCallString();
			
				ProcedureCaller pm = new ProcedureCaller();
				pm.callDbmsOutputProcedure(conn, callString,inputValue);
			
		} else {
			log.log(Level.SEVERE, "Pass IMEI code");
		}
	}
	
	public void callRefProcedure(String[] args) {
		log.log(Level.FINE, "Starting application to print dbms_output.put_line");
		if (args.length > 0) {
			String inputValue = args[0];
			String callString=dbConfig.getProcedureRefcurCallString();
		
				ProcedureCaller pm = new ProcedureCaller();
				pm.callRefProcedure(conn, callString,inputValue);
		
		} else {
			log.log(Level.SEVERE, "Pass IMEI code");
		}
	}
	

	
}
