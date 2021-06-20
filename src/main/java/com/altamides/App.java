package com.altamides;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App {
    Logger log= Logger.getLogger(App.class.getName());
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
        String procedure = prop.getProperty("oracle.preprocedure.name");
        String numOutParam = prop.getProperty("number.out.param");
        int numOutParamint = Integer.parseInt(numOutParam);

        DBConfig dbConfig=new DBConfig();
        dbConfig.setConnectionURL(connectionURL);
        dbConfig.setUser(user);
        dbConfig.setPassword(password);
        dbConfig.setDriver(driver);
        dbConfig.setProcedureName(procedure);
        dbConfig.setNumOutParam(numOutParamint);
        return dbConfig;
    }
}
