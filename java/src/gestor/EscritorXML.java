package gestor;

import exception.XMLException;
import modelo.AparcamientoBicicleta;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class EscritorXML {
	private File ficheroDestino,ficheroInvalidos;
	private ArrayList<AparcamientoBicicleta> listaAparcamientos = new ArrayList<>();


	public void setInformacionArchivoDestino(String ruta) throws XMLException {
		setRutaDestino(ruta);
	}

	public void setInformacionArchivoInvalidos(String ruta) throws XMLException {
		setRutaInvalidos(ruta);
	}

	public void addAparcamientoBicicleta(AparcamientoBicicleta apb) throws XMLException {
		if(apb == null){
			throw new XMLException("No puedes agregar aparcamientos nulos.");
		}
		listaAparcamientos.add(apb);
	}


	private void setRutaInvalidos(String ruta) throws XMLException {
		if(ruta == null || ruta.length() == 0){
			throw new XMLException("La ruta no es valida.");
		}
		setFicheroInvalidos(ruta);
	}

	private void setFicheroInvalidos(String ruta) throws XMLException {
		File f = new File(ruta);
		if(f.exists() || f.isDirectory()){
			throw new XMLException("La ruta Destino no es valida.");
		}
		ficheroInvalidos = f;
	}


	private void setRutaDestino(String rutaDestino) throws XMLException {
		if(rutaDestino == null || rutaDestino.length() == 0){
			throw new XMLException("La ruta no es valida.");
		}
		setFicheroDestino(rutaDestino);
	}

	private void setFicheroDestino(String rutaDestino) throws XMLException {
		File f = new File(rutaDestino);
		if(f.exists() || f.isDirectory()){
			throw new XMLException("La ruta Destino no es valida.");
		}
		ficheroDestino = f;
	}

	public String getRutaDestino(){
		return ficheroDestino.getAbsolutePath();
	}

	public String getListaAparcamientos(){
		if(listaAparcamientos.size() != 0){
			Collections.sort(listaAparcamientos);

			StringBuilder sb = new StringBuilder();
			for(AparcamientoBicicleta ap: listaAparcamientos){
				sb.append(ap).append("\n");
			}
			return sb.toString();
		}
		return "";
	}



}
