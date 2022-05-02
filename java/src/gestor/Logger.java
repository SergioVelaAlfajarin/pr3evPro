package gestor;

import exception.LogException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public abstract class Logger {
	private static File logFile = null;
	private static PrintWriter escritor = null;

	/**
	 * metodo init
	 * <hr/>
	 * inicia el logger en la ruta indicada.
	 *
	 * @param pathToLog ruta donde se guardara todos los logs.
	 * @throws LogException si ha ocurrido un error al iniciar el logger.
	 */
	public static void init(String pathToLog) throws LogException {
		try{
			compruebaPath(pathToLog);
			logFile = new File(pathToLog);
			escritor = new PrintWriter(logFile);
			Logger.print("Comienza la ejecucion");
			Logger.print("Se ha inicializado el logger. Ruta: " + logFile.getAbsolutePath());
		}catch(LogException | FileNotFoundException e){
			throw new LogException("Ha habido un problema con el logger. Codigo Error: 0030xLOIN");
		}
	}

	/**
	 * metodo compruebaPath
	 * <hr/>
	 * Comprueba si p es null o esta vacio. <br/>
	 * Despues comprueba si ya se ha abierto el archivo.
	 * Si se ha abierto, comprueba que la nueva ruta y la anterior son diferentes.
	 * Si son la misma, devuelve una excepcion.
	 * Si no lo son, la actualiza.
	 *
	 * @param p path
	 * @throws LogException error si la ruta no es valida o igual que la anterior.
	 */
	private static void compruebaPath(String p) throws LogException {
		if(p == null || p.length() == 0)
			throw new LogException("La ruta del logger no es valida. Codigo Error: 0048xLOCO");

		if(logFile != null){
			File tmp = new File(p);
			String pathInicial = logFile.getAbsolutePath();
			String pathTmp = tmp.getAbsolutePath();

			if(pathInicial.equalsIgnoreCase(pathTmp)){
				throw new LogException("La ruta del logger es la misma que la anterior. Codigo Error: 0056xLOCO");
			}
		}
	}

	/**
	 * metodo print
	 * <hr/>
	 * Comprueba si el archivo ha sido abierto.
	 * Si esta abierto, escribe la hora actual formateada, y la cadena pasada por parametro
	 *
	 * @param log log a escribir
	 * @throws LogException error si el archivo no ha sido abierto.
	 */
	public static void print(String log) throws LogException{
		if(escritor == null){
			throw new LogException("El logger no esta inicializado. Codigo Error: 0072xLOPR");
		}
		LocalDateTime hora = LocalDateTime.now();
		String cadena = String.format("[%s] -> %s%n", formatHora(hora), log);
		escritor.print(cadena);
		escritor.flush();
	}

	/**
	 * metodo formatHora
	 * <hr/>
	 * Formatea la hora actual para ser imprimida en el log.
	 *
	 * @param hora hora actual.
	 * @return cadena formateada con la hora y fecha actual.
	 */
	private static String formatHora(LocalDateTime hora) {
		return String.format(
				"Y:%04d-M:%02d-D:%02d H:%02d-M:%02d-S:%02d-ML:%s",
				hora.getYear(),
				hora.getMonthValue(),
				hora.getDayOfMonth(),
				hora.getHour(),
				hora.getMinute(),
				hora.getSecond(),
				String.valueOf(hora.getNano()).substring(0,4)
		);
	}

	/**
	 * metodo close
	 * <hr/>
	 * cierra el archivo si ha sido abierto.
	 *
	 * @throws LogException error si se intenta cerra un archivo no abierto.
	 */
	public static void close() throws LogException{
		if(escritor == null){
			throw new LogException("El logger no esta inicializado. Codigo Error: 0110xLOCL");
		}
		escritor.close();
	}
}
