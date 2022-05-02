package gestor;

import exception.LogException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public abstract class Logger {
	private static File logFile = null;
	private static PrintWriter escritor = null;

	private static boolean valido;

	public static void init(String pathToLog){
		compruebaPath(pathToLog);
		if(!valido){
			try {
				logFile = new File(pathToLog);
				escritor = new PrintWriter(logFile);
				valido = true;
			} catch (FileNotFoundException e) {
				valido = false;
			}
		}
	}

	private static void compruebaPath(String p) throws LogException {
		if(p == null || p.length() == 0)
			throw new LogException("La ruta del logger no es valida.");
	}

	public static void printLog(String log){

	}
}
