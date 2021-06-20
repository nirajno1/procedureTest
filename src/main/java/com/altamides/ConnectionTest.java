package com.altamides;

import javax.swing.*;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;
import java.util.logging.*;

public class ConnectionTest {
    private static Logger log= Logger.getLogger(ConnectionTest.class.getName());

    public void callProcedure(String[] args){

        log.fine("Starting application to call oracle procedure");
        if(args.length>0) {
            App app = new App();
            Properties prop = null;
            try {
                prop = app.getProperties();
            } catch (IOException e) {
                log.severe(e.getMessage());
            }

            /* propertieds reltated to connection */
            String connectionURL = prop.getProperty("spring.datasource.url");
            String user = prop.getProperty("spring.datasource.username");
            String password = prop.getProperty("spring.datasource.password");
            String driver = prop.getProperty("spring.datasource.driver-class-name");

            /* properties related to procedure */
            String runSP = prop.getProperty("oracle.preprocedure.name");
            String numOutParam = prop.getProperty("number.out.param");
            log.config("Procedure name:".concat(runSP).concat(" and out param number:").concat(numOutParam));
            int numOutParamint = Integer.parseInt(numOutParam);

            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                log.severe(e.getMessage());
            }
            try (Connection conn = DriverManager.getConnection(connectionURL, user, password);
                 CallableStatement callableStatement = conn.prepareCall(runSP)) {
                log.info("***************************************************************");
                String imei=args[0];
                log.config("calling with "+imei);
                callableStatement.setString(1, imei);
                for (int i = 1; i <= numOutParamint; i++) {
                    callableStatement.registerOutParameter(i + 1, Types.VARCHAR);
                }
                log.info("***************************************************************");


                callableStatement.execute();
                String msisdn = callableStatement.getString(2);
                String imsi = callableStatement.getString(3);

                log.config("Result received -> msisdn: ".concat(msisdn).concat("  imsi: ").concat(imsi));
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }else{
            log.severe("Pass IMEI code");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        initLogger();
        ConnectionTest conntest=new ConnectionTest();
        conntest.callProcedure(args);
    }
    private static void initLogger() {
        Handler consoleHandler = null;
        Handler fileHandler = null;
        try {
            //Creating consoleHandler and fileHandler
            consoleHandler = new ConsoleHandler();
            fileHandler = new FileHandler("./callSP.log");
            SimpleFormatter  simpleFormatter = new SimpleFormatter();
            consoleHandler.setFormatter(simpleFormatter);
            fileHandler.setFormatter(simpleFormatter);
            //Assigning handlers to LOGGER object
            log.addHandler(consoleHandler);
            log.addHandler(fileHandler);


            //Setting levels to handlers and LOGGER
            consoleHandler.setLevel(Level.ALL);
            fileHandler.setLevel(Level.ALL);
            log.setLevel(Level.ALL);

            log.config("Configuration done.");

            //Console handler removed
            log.removeHandler(consoleHandler);

            log.log(Level.FINE, "Finer logged");
        } catch (IOException exception) {
            log.log(Level.SEVERE, "Error occur in FileHandler.", exception);
        }

        log.finer("Finest example on LOGGER handler completed.");
    }
}

