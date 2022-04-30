package gestor;

import exception.XMLException;
import modelo.AparcamientoBicicleta;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class EscritorXML {
	private ArrayList<AparcamientoBicicleta> listaAparcamientos = new ArrayList<>();


	public void addAparcamientoBicicleta(AparcamientoBicicleta apb) throws XMLException {
		if(apb == null){
			throw new XMLException("No puedes agregar aparcamientos nulos.");
		}
		listaAparcamientos.add(apb);
	}
	
	public String getListaAparcamientos(){
		if(listaAparcamientos.size() != 0){
			sortListaAparcamientos();
			StringBuilder sb = new StringBuilder();
			for(AparcamientoBicicleta ap: listaAparcamientos){
				sb.append(ap).append("\n");
			}
			return sb.toString();
		}
		return "";
	}

	public void sortListaAparcamientos(){
		Collections.sort(listaAparcamientos);
	}


	public void escribeAparcamientosEnArchivo(String destino, String invalidos) throws XMLException {
		File archivoDestino = setRutaDestino(destino);
		File archivoInvalidos = setRutaInvalidos(invalidos); // no borrar archivo al escribir



	}

	private File setRutaInvalidos(String ruta) throws XMLException {
		if(ruta == null || ruta.length() == 0){
			throw new XMLException("La ruta no es valida.");
		}
		return setFicheroInvalidos(ruta);
	}

	private File setFicheroInvalidos(String ruta) throws XMLException {
		File f = new File(ruta);
		if(f.isDirectory()){
			throw new XMLException("La ruta Destino no es valida.");
		}
		return f;
	}

	private File setRutaDestino(String rutaDestino) throws XMLException {
		if(rutaDestino == null || rutaDestino.length() == 0){
			throw new XMLException("La ruta no es valida.");
		}
		return setFicheroDestino(rutaDestino);
	}

	private File setFicheroDestino(String rutaDestino) throws XMLException {
		File f = new File(rutaDestino);
		if(f.isDirectory()){
			throw new XMLException("La ruta Destino no es valida.");
		}
		return f;
	}
}
