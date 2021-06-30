package com.altamides;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.altamides.config.LogManager;

import oracle.jdbc.OracleTypes;

public class ProcedureCaller {
	private Logger log= LogManager.getLogger();

	/**
	 * Call procedure 
	 * @param conn Connection
	 * @param procName procedure name
	 * @param inParam value to be passed
	 * @param outParamCount total number of output parameters
	 */
	public void callProcedure(Connection conn, String procName, String inParam, int outParamCount) {
		int inParamCount = 1;
		String callString = createCallString(procName, inParamCount, outParamCount);
		log.log(Level.CONFIG, "Starting application to call procedure: " + procName + " value: " + inParam
				+ " no. of outparam: " + outParamCount +" "+callString);
		executeProcedure(conn, inParam, outParamCount, inParamCount, callString);
	}
	
	/**
	 * Call procedure 
	 * @param conn Connection
	 * @param inputValue 
	 * @param procName procedure name
	 * @param inParam value to be passed
	 * @param outParamCount total number of output parameters
	 */
	public void callDbmsOutputProcedure(Connection conn, String callString, String inputValue) {
		Array array = null;
		callString=createDbmsOutputCallString(callString, inputValue);
		log.log(Level.CONFIG, "Starting application to call " + callString);

		try (CallableStatement callableStatement = conn.prepareCall(callString)) {

			callableStatement.registerOutParameter(1, Types.ARRAY, "DBMSOUTPUT_LINESARRAY");
			callableStatement.execute();
			array = callableStatement.getArray(1);
			log.log(Level.CONFIG,Arrays.asList((Object[]) array.getArray()).toString());
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage(),e);
		} finally {
			if (array != null)
				try {
					array.free();
				} catch (SQLException e) {
					log.log(Level.SEVERE, e.getMessage(),e);
				}
		}
	}

	private void executeProcedure(Connection conn, String inParam, int outParamCount, int inParamCount,
			String callString) {
		try (CallableStatement callableStatement = conn.prepareCall(callString)) {

			if(inParam!=null) {
				callableStatement.setString(1, inParam);
			}
			for(int i=1+inParamCount;i<=outParamCount+inParamCount;i++) {
				callableStatement.registerOutParameter(i, Types.VARCHAR);
			}
			callableStatement.executeQuery();
			
			StringBuilder sb = new StringBuilder();
			for(int i=1+inParamCount;i<=outParamCount+inParamCount;i++) {
				sb.append(callableStatement.getString(i)).append(" "); 
			}
			
			log.log(Level.CONFIG, "Result received -> ".concat(sb.toString()));
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private String createCallString(String procName, int inParamCount, int outParamCount) {
		String sb = "call {0}({1})";
		StringBuilder stb = new StringBuilder();
		for (int i = 0; i < (inParamCount + outParamCount); i++) {
			stb.append(" ?,");
		}
		String qmarks = stb.delete(stb.length() - 1, stb.length()).toString();
		sb = MessageFormat.format(sb, procName, qmarks);
		StringBuilder sbfinal = new StringBuilder();
		sbfinal.append("{").append(" ").append(sb).append('}');
		return sbfinal.toString();

	}
	
	
	/**
	 * Helper method to create call string to print dbms_output.put_line
	 * @param dbConfig
	 * @param value
	 * @return
	 */


	private String createDbmsOutputCallString(String callString,String value) {
		 String str="declare "
				  + "  num integer := 1000;" // Adapt this as needed
				  + "begin "

				  // You have to enable buffering any server output that you may want to fetch
				  + "  dbms_output.enable();"

				  // This might as well be a call to third-party stored procedures, etc., whose
				  // output you want to capture
				  + MessageFormat.format(callString,value)+";"
				  // This is again your call here to capture the output up until now.
				  // The below fetching the PL/SQL TABLE type into a SQL cursor works with Oracle 12c.
				  // In an 11g version, you'd need an auxiliary SQL TABLE type
				  + "  dbms_output.get_lines(?, num);"
				 
				  // Don't forget this or the buffer will overflow eventually
				  + "  dbms_output.disable();"
				  + "end;";
		
		 return str;
	}
	

	
	
	public void callRefProcedure(Connection conn,	String callString, String inParam) {
		log.log(Level.CONFIG, "Calling -> ".concat(callString).concat(" ").concat(inParam));
		try (CallableStatement callableStatement = conn.prepareCall(callString)) {

			if(inParam!=null) {
				callableStatement.setString(1, inParam);
			}
			
			callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
			callableStatement.execute();
			
            // get refcursor and convert it to ResultSet
			StringBuilder sb = new StringBuilder();
            ResultSet resultSet = (ResultSet) callableStatement.getObject(2);
            while (resultSet.next()) {
                String imei = resultSet.getString("IMEI");
                String msisdn = resultSet.getString("MSISDN");
                String imsi = resultSet.getString("IMSI");
                sb.append(imei).append(" ").append(msisdn).append(" ").append(imsi).append(" ");
            }
			
			log.log(Level.CONFIG, "Result received -> ".concat(sb.toString()));
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
}
