package gestor;

import exception.LogException;
import exception.XMLException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public abstract class Logger {
	private static File logFile = null;
	private static PrintWriter escritor = null;

	public static void init(String pathToLog) throws LogException {
		try{
			compruebaPath(pathToLog);
			logFile = new File(pathToLog);
			escritor = new PrintWriter(logFile);
			Logger.print("Comienza la ejecucion");
			Logger.print("Se ha inicializado el logger. Ruta: " + logFile.getAbsolutePath());
		}catch(LogException | FileNotFoundException e){
			throw new LogException("Ha habido un problema con el logger.");
		}
	}

	private static void compruebaPath(String p) throws LogException {
		if(p == null || p.length() == 0)
			throw new LogException("La ruta del logger no es valida.");

		if(logFile != null){
			File tmp = new File(p);
			String pathInicial = logFile.getAbsolutePath();
			String pathTmp = tmp.getAbsolutePath();

			if(pathInicial.equalsIgnoreCase(pathTmp)){
				throw new LogException("La ruta del logger es la misma que la anterior.");
			}
		}
	}

	public static void print(String log) throws LogException{
		if(escritor == null){
			throw new LogException("El logger no esta inicializado");
		}
		String hora = LocalDateTime.now().toString();
		String cadena = String.format("[%s] -> %s%n", hora, log);
		escritor.print(cadena);
		escritor.flush();
	}

	public static void close() throws LogException{
		if(escritor == null){
			throw new LogException("El logger no esta inicializado");
		}
		escritor.close();
	}
}
