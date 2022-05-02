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
import java.time.LocalDateTime;
import java.util.*;

public class EscritorXML {
	private final ArrayList<AparcamientoBicicleta> listaAparcamientosValidos = new ArrayList<>();
	private final ArrayList<AparcamientoBicicleta> listaAparcamientosInvalidos = new ArrayList<>();

	/**
	 * Metodo addAparcamientoBicicleta
	 * <hr/>
	 * Añade un AparcamientoBicleta al array principal.
	 *
	 * @param apb AparcamientoBicicleta a añadir.
	 * @throws XMLException Si el objeto es nulo.
	 */
	public void addAparcamientoBicicleta(AparcamientoBicicleta apb) throws XMLException {
		if(apb == null){
			throw new XMLException("No puedes agregar aparcamientos nulos.");
		}
		listaAparcamientosValidos.add(apb);
		Logger.print("AparcamientoBicleta añadido correctamente al array de EscritorXML Id:" + apb.getId());
	}

	/**
	 * metodo escribeAparcamientosEnArchivo
	 * <hr/>
	 * Separa los arrays, eliminando los Aparcamientos invalidos del array principal y
	 * almacenandolos en el array secundario. <br>
	 *
	 * @param rutaXmlNueva Ruta donde se escribira los aparcamientos validos.
	 * @param rutaXmlInvalidos Ruta donde se escribiran los aparcamientos invalidos.
	 * @throws XMLException Si ha habido un problema con la separacion de arrays o la escritura de los mismos.
	 */
	public void escribeAparcamientosEnArchivo(String rutaXmlNueva, String rutaXmlInvalidos) throws XMLException {
		Logger.print("Separando arrays.");
		separaArrays();
		Logger.print("Arrays separados.");
		Logger.print("Escribiendo Aparcamientos validos...");
		escribeAparcamientosValidos(rutaXmlNueva);
		Logger.print("Escribiendo Aparcamientos invalidos...");
		escribeAparcamientosInvalidos(rutaXmlInvalidos);
	}

	/**
	 *  metodo separaArrays
	 *  <hr/>
	 *  Crea un iterator del array principal, y lo recorre.
	 *  Si el numero de plazas del array es nulo o 0, este es eliminado y agregado al array secundario. <br/>
	 *  Una vez separados, el array principal es ordenado por plazas, y despues por calle.
	 * @throws XMLException si hay error en la ordenacion del array principal.
	 */
	private void separaArrays() throws XMLException {
		ListIterator<AparcamientoBicicleta> it = listaAparcamientosValidos.listIterator();
		while(it.hasNext()){
			AparcamientoBicicleta apb = it.next();
			Integer plazas = apb.getPlazas();
			Logger.print("Analizando Aparcamiento con id:" + apb.getId());
			if(plazas == null || plazas <= 0){
				it.remove();
				listaAparcamientosInvalidos.add(apb);
				Logger.print("Aparcamiento no valido. Moviendo al array secundario. Plazas: " + plazas);
			}else{
				Logger.print("Aparcamiento valido. Plazas: " + plazas);
			}
		}
		Logger.print("Ordenando lista de aparcamientos validos.");
		sortListaAparcamientos();
		Logger.print("Lista ordenada.");
	}

	/**
	 * metodo sortListaAparcamientos
	 * <hr/>
	 * Intenta ordenar el array principal.
	 *
	 * @throws XMLException si ocurre algun error en la ordenacion.
	 */
	public void sortListaAparcamientos() throws XMLException{
		try{
			Collections.sort(listaAparcamientosValidos);
		}catch(Exception ex){
			throw new XMLException("Error al ordenar la coleccion. Posibles valores nulos.");
		}
	}

	/**
	 * metodo escribeAparcamientosValidos
	 * <hr/>
	 * Crea un documento XML con la estructura indicada en el enunciado.
	 * Lo rellena con la informacion de los objetos del array Principal.
	 *
	 * @param rutaXmlNueva ruta donde se guardara la salida del nuevo documento xml.
	 * @throws XMLException si ocurre algun error en la creacion o rellenado del nuevo archivo.
	 */
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

	/**
	 * metodo rellenaInformacionXML
	 * <hr/>
	 * Crea un tag que cuelga desde la raiz, y lo rellena con la informacion del objeto apb.
	 *
	 * @param apb objeto que contiene la informacion a rellenar.
	 * @param miDoc documento que contendra la tag creada.
	 * @param raiz raiz del documento.
	 */
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

	/**
	 * metodo escribeAparcamientosInvalidos
	 * <hr/>
	 * este metodo escribira los aparcamientos que contiene el array secundario.
	 * Como son invalidos, se escribira solamente su id y la fecha actual de cuando fueron escritos.
	 * El contenido del archivo no sera reiniciado en cada ejecucion. <br/>
	 * Por ulitmo, antes de escribir en el archivo, comprobara si este ya tiene contenido.
	 * Si el archivo tiene algun aparcamiento y este tambien se encuentre en el array, sera eliminado del mismo,
	 * para evitar entradas repetidas en el archivo.
	 *
	 * @param rutaXmlInvalidos ruta del archivo de invalidos.
	 * @throws XMLException si ocurre algun error en la escritura del archivo.
	 */
	private void escribeAparcamientosInvalidos(String rutaXmlInvalidos) throws XMLException {
		compruebaSiExisteEnArchivo(rutaXmlInvalidos);

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

	/**
	 * metodo compruebaSiExisteEnArchivo
	 * <hr/>
	 * Intenta leer el archivo.
	 * si lo lee, lo analiza linea por linea.
	 * Si hay algun aparcamiento con el mismo id que algun aparcamiento del array, este es eliminado.
	 *
	 * @param rutaXmlInvalidos ruta del archivo de aparcamientos invalidos.
	 */
	private void compruebaSiExisteEnArchivo(String rutaXmlInvalidos){
		try(Scanner sc = new Scanner(new File(rutaXmlInvalidos))){
			while(sc.hasNext()){
				String linea = sc.nextLine();
				Integer idLinea = extraeIDLinea(linea);

				listaAparcamientosInvalidos.removeIf(apb -> idLinea.equals(apb.getId()));
			}
		} catch (Exception e){
			//throw new XMLException("No se ha podido encontrar el archivo de invalidos");
			//logger
		}
	}

	/**
	 * metodo extraeIDLinea
	 * <hr/>
	 * Extra el id de la linea leida. dividira la linea por :, y luego obtendra la posicion de la primera coma.
	 * Extraera la cadena desde el principio hasta la primera coma.
	 * Hara un parseInt, y si este falla esque la cadena extraida no era un numero.
	 *
	 * @param l linea leida del archivo.
	 * @return id extraida en formato numerico.
	 * @throws XMLException si ocurre algun error en la extracion o en el parseInt.
	 */
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

	/**
	 * metodo obtenerPosicionComa
	 * <hr/>
	 * obtiene la primera coma encontrada en la linea pasada por parametro.
	 * @param c cadena a analizar
	 * @return numero indicando la posicion de la coma. si no hay ninguna, devolvera -1.
	 */
	private int obtenerPosicionComa(String c) {
		for(int i=0; i<c.length(); i++) {
			if(c.charAt(i) == ',')
				return i;
		}
		return -1;
	}

	/**
	 * metodo setRutaDestino
	 * <hr/>
	 * comprueba que la ruta no es nula ni cadena vacia.
	 *
	 * @param rutaDestino ruta del archivo donde se escribiran los aparcamientos validos.
	 * @return objeto File abierto en esa ruta.
	 * @throws XMLException Error si la ruta no es valida.
	 */
	private File setRutaDestino(String rutaDestino) throws XMLException {
		if(rutaDestino == null || rutaDestino.length() == 0){
			throw new XMLException("La ruta no es valida.");
		}
		return setFicheroDestino(rutaDestino);
	}

	/**
	 * metodo setFicheroDestino
	 * <hr/>
	 * crea un objeto file con la ruta validada.
	 *
	 * @param rutaDestino ruta del archivo validada previamente.
	 * @return objeto File en la ruta validada.
	 * @throws XMLException Error si la ruta apunta a un directorio y no a un fichero.
	 */
	private File setFicheroDestino(String rutaDestino) throws XMLException {
		File f = new File(rutaDestino);
		if(f.isDirectory()){
			throw new XMLException("La ruta Destino no es valida.");
		}
		return f;
	}

}
