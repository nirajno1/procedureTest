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
    public Properties getProperties() throws IOException {
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
}
