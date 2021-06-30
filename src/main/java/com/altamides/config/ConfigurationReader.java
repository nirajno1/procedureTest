package com.altamides.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import com.altamides.model.DBConfig;

/**
 * Hello world!
 *
 */
public class ConfigurationReader {
	private Logger log= LogManager.getLogger();
    private Properties getProperties() throws IOException {
    	try {
    	    log.fine("Reading properties files");
    		File propFileName= new File("./app.properties");
            Properties prop = new Properties();
            InputStream inputStream = new FileInputStream(propFileName);
            if (inputStream != null) {
                prop.load(inputStream);
            }
            log.fine("Properties files loaded successfully");
            return prop;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.severe(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.warning("Unable to load properties files returning null");
        return null;
    }

    public DBConfig getDBConfig() throws IOException {
        Properties prop = null;
        prop = getProperties();
        String connectionURL = prop.getProperty("spring.datasource.url");
        String user = prop.getProperty("spring.datasource.username");
        String password = prop.getProperty("spring.datasource.password");
        String driver = prop.getProperty("spring.datasource.driver-class-name");

        /* properties related to procedure */
        String procedure = prop.getProperty("db.procedure.name");
        String callString = prop.getProperty("db.procedure.callString");
        String numOutParam = prop.getProperty("number.out.param");
        int numOutParamint = Integer.parseInt(numOutParam);
        String sqlQuery=prop.getProperty("db.sql.query");

        DBConfig dbConfig=new DBConfig();
        dbConfig.setConnectionURL(connectionURL);
        dbConfig.setUser(user);
        dbConfig.setPassword(password);
        dbConfig.setDriver(driver);
        dbConfig.setProcedureName(procedure);
        dbConfig.setCallString(callString);
        dbConfig.setNumOutParam(numOutParamint);
        dbConfig.setSqlQuery(sqlQuery);
        dbConfig.setProcedureRefcurCallString(prop.getProperty("db.procedure.refcur.callString"));
        return dbConfig;
    }
    

}
