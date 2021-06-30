package com.altamides.config;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogManager {
	private static Logger log;
	private static LogManager instance;
	private LogManager() {
		
		
	}
	public static Logger getLogger() {
		if(instance== null) {
			instance= new LogManager();
			log = initLogger("ProcedureTest");
		}
		return log;
	}
	
	public static Logger initLogger(String string) {
		log = Logger.getLogger("ProcedureTest");
		try {
			// Creating consoleHandler and fileHandler
			Calendar cal = Calendar.getInstance();
			Handler consoleHandler = new ConsoleHandler();
			String date = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR);
			Handler fileHandler = new FileHandler("./procedureTest_".concat(date).concat(".log"));
			SimpleFormatter simpleFormatter = new SimpleFormatter();
			consoleHandler.setFormatter(simpleFormatter);
			fileHandler.setFormatter(simpleFormatter);
			// Assigning handlers to LOGGER object
			log.addHandler(consoleHandler);
			log.addHandler(fileHandler);
			// Setting levels to handlers and LOGGER
			consoleHandler.setLevel(Level.ALL);
			fileHandler.setLevel(Level.ALL);
			log.setLevel(Level.ALL);

		} catch (IOException exception) {
			log.log(Level.SEVERE, "Error occur in FileHandler.", exception);
		}
		return log;
	}
}
