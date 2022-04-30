package gestor;

import exception.XMLException;
import modelo.AparcamientoBicicleta;

import java.io.File;
import java.util.ArrayList;

public class EscritorXML {
	private File ficheroDestino;
	private ArrayList<AparcamientoBicicleta> listaAparcamientos = new ArrayList<>();


	public void setInformacionArchivoDestino(String ruta) throws XMLException {
		setRutaDestino(ruta);
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

	public void addAparcamientoBicicleta(AparcamientoBicicleta apb) throws XMLException {
		if(apb == null){
			throw new XMLException("No puedes agregar aparcamientos nulos.");
		}
		listaAparcamientos.add(apb);
	}

	public String getRutaDestino(){
		return ficheroDestino.getAbsolutePath();
	}

	public String getListaAparcamientos(){
		StringBuilder sb = new StringBuilder();
		for(AparcamientoBicicleta ap: listaAparcamientos){
			sb.append(ap).append("\n");
		}
		return sb.toString();
	}
}
