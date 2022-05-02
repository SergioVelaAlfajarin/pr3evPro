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
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

public class EscritorXML {
	private final ArrayList<AparcamientoBicicleta> listaAparcamientosValidos = new ArrayList<>();
	private final ArrayList<AparcamientoBicicleta> listaAparcamientosInvalidos = new ArrayList<>();

	public void addAparcamientoBicicleta(AparcamientoBicicleta apb) throws XMLException {
		if(apb == null){
			throw new XMLException("No puedes agregar aparcamientos nulos.");
		}
		listaAparcamientosValidos.add(apb);
	}

	public void escribeAparcamientosEnArchivo(String rutaXmlNueva, String rutaXmlInvalidos) throws XMLException {
		separaArrays();
		escribeAparcamientosValidos(rutaXmlNueva);
		escribeAparcamientosInvalidos(rutaXmlInvalidos);
	}

	private void separaArrays() throws XMLException {
		ListIterator<AparcamientoBicicleta> it = listaAparcamientosValidos.listIterator();
		while(it.hasNext()){
			AparcamientoBicicleta apb = it.next();
			if(apb.getPlazas() <= 0){
				it.remove();
				listaAparcamientosInvalidos.add(apb);
			}
		}
		sortListaAparcamientos();
	}

	public void sortListaAparcamientos() throws XMLException{
		try{
			Collections.sort(listaAparcamientosValidos);
		}catch(Exception ex){
			throw new XMLException("Error al ordenar la coleccion. Posibles valores nulos.");
		}
	}

	private void escribeAparcamientosValidos(String rutaXmlNueva) throws XMLException {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			DOMImplementation generador = db.getDOMImplementation();

			//Set informacion basica y raiz
			Document miDoc = generador.createDocument(null, "estaciones", null);
			miDoc.setXmlVersion("1.0");
			miDoc.setXmlStandalone(true);
			Element raiz = miDoc.getDocumentElement();

			//rellena informacion donde corresponda
			for(AparcamientoBicicleta apb: listaAparcamientosValidos){
				rellenaInformacionXML(apb, miDoc, raiz);
			}

			//Creamos el archivo fuente que queremos guardar y indicamos donde queremos guardar el archivo
			Source fuente = new DOMSource(miDoc);
			Result resultado = new StreamResult(setRutaDestino(rutaXmlNueva));

			//Escribe el archivo
			Transformer transform = TransformerFactory.newInstance().newTransformer();
			transform.setOutputProperty(OutputKeys.INDENT, "yes");
			transform.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transform.transform(fuente, resultado);
		} catch (ParserConfigurationException ex) {
			throw new XMLException("Imposible generar el documento.");
		} catch (TransformerConfigurationException ex) {
			throw new XMLException("Error con el documento.");
		} catch (TransformerException ex) {
			throw new XMLException("Error guardando el fichero.");
		} catch (IOException e) {
			throw new XMLException("Ha ocurrido un error inesperado.");
		}
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

	private void escribeAparcamientosInvalidos(String rutaXmlInvalidos) throws XMLException {
		try{
			compruebaSiExisteEnArchivo(rutaXmlInvalidos);
		}catch(XMLException ex){

		}


		try (DataOutputStream dos = new DataOutputStream(
				new FileOutputStream(rutaXmlInvalidos, true))
		){
			for(AparcamientoBicicleta apb: listaAparcamientosInvalidos){
				String cadena = String.format(
						"AparcamientoBicicleta {ID:%s, Date:%s}%n",
						apb.getId(),
						LocalDateTime.now()
				);
				dos.writeUTF(cadena);
				dos.flush();
			}
		} catch (IOException e) {
			throw new XMLException("Error al escribir el fichero de invalidos.");
		}
	}

	private void compruebaSiExisteEnArchivo(String rutaXmlInvalidos) throws XMLException {
		try(Scanner sc = new Scanner(new File(rutaXmlInvalidos))){
			while(sc.hasNext()){
				String linea = sc.nextLine();
				Integer idLinea = extraeIDLinea(linea);

				listaAparcamientosInvalidos.removeIf(apb -> idLinea.equals(apb.getId()));
			}
		} catch (Exception e){
			throw new XMLException("No se ha podido encontrar el archivo de invalidos");
		}
	}

	private Integer extraeIDLinea(String l) throws XMLException {
		String[] lineaSeparada = l.split(":");
		int posicionComa = obtenerPosicionComa(lineaSeparada[1]);
		if(posicionComa <= 0){
			throw new XMLException("Error al leer el archivo de invalidos.");
		}
		String id = lineaSeparada[1].substring(0,posicionComa);
		try{
			return Integer.parseInt(id);
		}catch (NumberFormatException ex){
			throw new XMLException("Error al leer el archivo de invalidos");
		}
	}

	private int obtenerPosicionComa(String linea) {
		for(int i=0; i<linea.length(); i++) {
			if(linea.charAt(i) == ',')
				return i;
		}
		return -1;
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
