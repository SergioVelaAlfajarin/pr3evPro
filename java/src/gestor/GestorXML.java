package gestor;

import java.io.File;

import exception.XMLException;
import modelo.AparcamientoBicicleta;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public final class GestorXML {
	private File ficheroOrigen, ficheroDestino;

	private Element raiz;

	public void setInformacionArchivoOrigen(String ruta) throws XMLException {
		setRutaOrigen(ruta);
		setRaiz();
	}

	public void setInformacionArchivoDestino(String ruta) throws XMLException {
		setRutaDestino(ruta);
	}


	private void setRutaOrigen(String rutaOrigen) throws XMLException{
		if(rutaOrigen == null || rutaOrigen.length() == 0){
			throw new XMLException("La ruta no es valida.");
		}
		setFicheroOrigen(rutaOrigen);
	}

	private void setFicheroOrigen(String rutaOrigen)throws XMLException {
		File f = new File(rutaOrigen);
		if(!f.exists() || f.isDirectory()){
			throw new XMLException("La ruta es un directorio o no existe.");
		}
		ficheroOrigen = f;
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

	private void setRaiz() throws XMLException {
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			raiz = db.parse(ficheroOrigen).getDocumentElement();
		} catch (IOException | ParserConfigurationException | SAXException ex) {
			throw new XMLException("Error al leer el archivo XML");
		}
	}


	public String getRutaOrigen() {
		return ficheroOrigen.getAbsolutePath();
	}

	public String getRutaDestino(){
		return ficheroDestino.getAbsolutePath();
	}

	public AparcamientoBicicleta transformElementToAparcamiento(Element n){
		//TODO esto de aqui
		return null;
	}

	public NodeList getNodosRaiz() throws XMLException {
		NodeList nl = raiz.getElementsByTagName("aparcamiento-bicicleta");
		compruebaLongitudValida(nl);
		return nl;
	}

	private void compruebaLongitudValida(NodeList nl) throws XMLException {
		Node cuenta = raiz.getElementsByTagName("rows").item(0);

		if(!(cuenta.getTextContent().equalsIgnoreCase(nl.getLength() + ""))){
			throw new XMLException("El archivo XML es invalido.");
		}
	}
}
