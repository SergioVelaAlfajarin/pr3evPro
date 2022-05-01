package gestor;

import exception.XMLException;
import modelo.AparcamientoBicicleta;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
	
	public String getListaAparcamientos() throws XMLException {
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

	public void sortListaAparcamientos() throws XMLException{
		try{
			Collections.sort(listaAparcamientos);
		}catch(Exception ex){
			throw new XMLException("Error al ordenar la coleccion. Posibles valores nulos.");
		}
	}

	public void escribeAparcamientosEnArchivo(String destino, String invalidos) throws XMLException {
		File archivoDestino = setRutaDestino(destino);
		File archivoInvalidos = setRutaInvalidos(invalidos); // no borrar archivo al escribir

		sortListaAparcamientos();

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			DOMImplementation generador = db.getDOMImplementation();

			//Set informacion basica y raiz
			Document miDoc = generador.createDocument(null, "estaciones", null);
			miDoc.setXmlVersion("1.0");
			miDoc.setXmlStandalone(true);
			Element raiz = miDoc.getDocumentElement();

			for(AparcamientoBicicleta apb: listaAparcamientos){
				if(apb.getPlazas() <= 0){
					escribeEstacionInvalida(archivoInvalidos, apb);
				}else{
					rellenaInformacionXML(apb, miDoc, raiz);
				}
			}

			//Creamos el archivo fuente que queremos guardar y indicamos donde queremos guardar el archivo
			Source fuente = new DOMSource(miDoc);
			Result resultado = new StreamResult(archivoDestino);

			//Escribe el archivo
			Transformer transform = TransformerFactory.newInstance().newTransformer();
			transform.setOutputProperty(OutputKeys.INDENT, "yes");
			transform.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transform.transform(fuente, resultado);
		} catch (ParserConfigurationException ex) {
			throw new XMLException("Imposible generar el documento");
		} catch (TransformerConfigurationException ex) {
			throw new XMLException("Error con el documento");
		} catch (TransformerException ex) {
			throw new XMLException("Error guardando el fichero");
		}
	}

	private void escribeEstacionInvalida(File f, AparcamientoBicicleta apb) {
		/*
		Las estaciones que tienen 0 bicicletas se guardarán en un fichero llamado llevar_bicis.dat, en el que se guardará la
		fecha y la hora registrada (LocalDateTime.now()) y el número de la estación (como cadena alfanumérica).
		Para guardar los datos utilizaremos DataOutputStream.
		Dicho fichero no se borrará nunca, se mantendrá con los datos de las ejecuciones anteriores.
		Todos los ficheros, xml y txt, estarán en la carpeta ficheros.
		 */


	}

	private void rellenaInformacionXML(AparcamientoBicicleta apb, Document miDoc, Element raiz) {
		//Creamos todos los elementos necesarios
		Element etEstacion = miDoc.createElement("estacion");
		Element etCalle = miDoc.createElement("calle");
		Element etBicis = miDoc.createElement("bicis");
		Element etAnclajes = miDoc.createElement("anclajes");

		//Creamos el atributo y Añadir texto al atributo y se lo asignamos a Estacion
		Attr atID = miDoc.createAttribute("id");
		atID.setTextContent(String.valueOf(apb.getId()));
		etEstacion.setAttributeNode(atID);

		//Creamos los nodos de texto
		Text valorCalle = miDoc.createTextNode(apb.getTitle());
		Text valorBicis = miDoc.createTextNode(String.valueOf(apb.getPlazas()));
		Text valorAnclajes = miDoc.createTextNode(String.valueOf(apb.getAnclajes()));

		//asignamos las etiquetas a estacion
		etEstacion.appendChild(etCalle);
		etEstacion.appendChild(etBicis);
		etEstacion.appendChild(etAnclajes);

		//damos un valor a las etiquetas de estacion
		etCalle.appendChild(valorCalle);
		etBicis.appendChild(valorBicis);
		etAnclajes.appendChild(valorAnclajes);

		//añadimos la estacion a la etiqueta raiz
		raiz.appendChild(etEstacion);
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
