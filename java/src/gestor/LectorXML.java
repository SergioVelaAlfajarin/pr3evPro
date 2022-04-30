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

public final class LectorXML {
	private File ficheroOrigen;
	private Element raiz;

	public void setInformacionArchivoOrigen(String ruta) throws XMLException {
		setRutaOrigen(ruta);
		setRaiz();
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


	public AparcamientoBicicleta transformElementToAparcamiento(Element elmnt) throws XMLException {
		String[] listaTagNames = new String[]{"id", "title", "tipo", "plazas", "anclajes", "lastUpdated","icon"};
		String[] informacionAparcamiento = new String[listaTagNames.length];
		for(int i=0; i<listaTagNames.length; i++){
			NodeList nodes = elmnt.getElementsByTagName(listaTagNames[i]);
			if(nodes.getLength() != 1){
				throw new XMLException("La estrucutra del archivo xml es incorrecta.");
			}
			informacionAparcamiento[i] = nodes.item(0).getTextContent();
		}
		return new AparcamientoBicicleta(informacionAparcamiento);
	}


}
