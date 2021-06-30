package com.altamides.model;

public class DBConfig {
    private String connectionURL ;
    private String user ;
    private String password ;
    private String driver ;
    private String procedureName;
    private String callString;
    private int numOutParam;
    private String sqlQuery;
    private String procedureRefcurCallString;

    public String getConnectionURL() {
        return connectionURL;
    }

    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public int getNumOutParam() {
        return numOutParam;
    }

    public void setNumOutParam(int numOutParam) {
        this.numOutParam = numOutParam;
    }

    public String getCallString() {
        return callString;
    }

    public void setCallString(String callString) {
        this.callString = callString;
    }

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public String getProcedureRefcurCallString() {
		return procedureRefcurCallString;
	}

	public void setProcedureRefcurCallString(String procedureRefcurCallString) {
		this.procedureRefcurCallString = procedureRefcurCallString;
	}

}
