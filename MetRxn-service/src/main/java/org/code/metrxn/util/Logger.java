package org.code.metrxn.util;

public class Logger {

	public static void error(String message, Class classname) {
		System.out.println("Error occured in " + classname  );
		System.out.println("Error occured while " + message  );
	}
	
	public static void info( String message, Class className) {
		System.out.println("Info @ " + className + " :" + message );
	}
}
