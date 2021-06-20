package com.altamides;

import java.io.IOException;
import java.sql.*;
import java.util.logging.*;

import static java.lang.System.exit;

public class ConnectionTest {
    private static final Logger log = Logger.getLogger(ConnectionTest.class.getName());

    public void callProcedure(String[] args) {
        logMessgae(Level.FINE,"Starting application to call oracle procedure");
        if (args.length > 0) {
            DBConfig dbConfig = getDBConfig();
            logMessgae(Level.CONFIG, "Procedure name:".concat(dbConfig.getProcedureName())
                    .concat(" and out param number:") + dbConfig.getNumOutParam());
            try {
                Class.forName(dbConfig.getDriver());
            } catch (ClassNotFoundException e) {
                logMessgae(Level.SEVERE, e.getMessage());
                exit(1);
            }
            try (Connection conn = DriverManager.getConnection(dbConfig.getConnectionURL(), dbConfig.getUser(), dbConfig.getPassword());
                 CallableStatement callableStatement = conn.prepareCall(dbConfig.getProcedureName())) {

                logMessgae(Level.INFO, "***************************************************************");
                String imei = args[0];
                logMessgae(Level.CONFIG, "calling with ".concat(imei));
                callableStatement.setString(1, imei);
                for (int i = 1; i <= dbConfig.getNumOutParam(); i++) {
                    callableStatement.registerOutParameter(i + 1, Types.VARCHAR);
                }
                callableStatement.execute();
                String msisdn = callableStatement.getString(2);
                String imsi = callableStatement.getString(3);
                logMessgae(Level.CONFIG, "Result received -> msisdn: ".concat(msisdn).concat("  imsi: ").concat(imsi));
                logMessgae(Level.INFO, "***************************************************************");

            } catch (SQLException e) {
                logMessgae(Level.SEVERE, e.getMessage());
            }
        } else {
            logMessgae(Level.SEVERE, "Pass IMEI code");
        }
    }

    private void logMessgae(Level level, String message) {
        if (Level.CONFIG.equals(level) ) {
            log.config(message);
        } else if (Level.INFO.equals(level) ) {
            log.info(message);
        } else if (Level.FINE.equals(level) ) {
            log.fine(message);
        } else if (Level.SEVERE.equals(level)) {
            log.severe(message);
        }
    }

    private DBConfig getDBConfig() {
        DBConfig dbConfig = null;
        try {
            App app = new App();
            dbConfig = app.getDBConfig();
        } catch (IOException e) {
            log.severe(e.getMessage());
            exit(1);
        }
        return dbConfig;
    }

    public static void main(String[] args) {
        ConnectionTest conntest = new ConnectionTest();
        conntest.initLogger();
        conntest.callProcedure(args);
    }

    private void initLogger() {
        try {
            //Creating consoleHandler and fileHandler
            Handler consoleHandler = new ConsoleHandler();
            Handler fileHandler = new FileHandler("./procedureCallApp.log");
            SimpleFormatter simpleFormatter = new SimpleFormatter();
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
            //log.removeHandler(consoleHandler);

            //log.log(Level.FINE, "Finer logged");
        } catch (IOException exception) {
            log.log(Level.SEVERE, "Error occur in FileHandler.", exception);
        }
    }
}

